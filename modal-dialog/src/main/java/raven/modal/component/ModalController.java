package raven.modal.component;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.option.Option;
import raven.modal.slider.PanelSlider;
import raven.modal.utils.ImageSnapshots;
import raven.modal.utils.ModalMouseMovableListener;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.image.VolatileImage;
import java.util.logging.Logger;

/**
 * @author Raven
 */
public class ModalController extends AbstractModalController {

    private static final Logger LOGGER = Logger.getLogger(ModalController.class.getName());

    private final AbstractModalContainerLayer modalContainerLayer;
    private final ModalContainer modalContainer;
    private boolean display;
    private boolean showing;
    private Animator animator;
    private float animated;
    private double systemScaleFactor;
    private Image snapshotsImage;
    // Cached during animation to avoid per-frame MigLayout tree traversal
    private Dimension animationPreferredSize;
    private Dimension animationMinimumSize;
    private Rectangle animationStartRect;
    private Rectangle animationEndRect;
    // Reusable buffer for live-render + scale to avoid sub-pixel text artifacts
    private VolatileImage liveScaleBuffer;

    public ModalController(AbstractModalContainerLayer modalContainerLayer, ModalContainer modalContainer, Option option) {
        super(option);
        this.modalContainerLayer = modalContainerLayer;
        this.modalContainer = modalContainer;
    }

    protected boolean isUseAnimator(boolean show) {
        return option.isAnimationEnabled()
                && (show || option.isAnimationOnClose()
        );
    }

    public void startAnimator(boolean show) {
        if (isUseAnimator(show)) {
            if (animator == null) {
                animator = new Animator(option.getDuration(), new Animator.TimingTarget() {
                    @Override
                    public void timingEvent(float v) {
                        animated = showing ? v : 1f - v;
                        modalContainer.repaint();
                        modalContainer.getModalLayout().setAnimate(animated);
                        if (animationStartRect != null && animationEndRect != null) {
                            int x = animationStartRect.x + (int)((animationEndRect.x - animationStartRect.x) * animated);
                            int y = animationStartRect.y + (int)((animationEndRect.y - animationStartRect.y) * animated);
                            setBounds(x, y, animationEndRect.width, animationEndRect.height);
                        } else {
                            // Fallback: rects were not pre-computed (begin() failed to set them).
                            // This branch should never fire under normal operation.
                            LOGGER.warning("ModalController: animationStartRect/EndRect is null in timingEvent — falling back to doLayout()");
                            modalContainer.doLayout();
                        }
                    }

                    @Override
                    public void begin() {
                        // Cache preferred/minimum size once so timingEvent()
                        // never calls into MigLayout during the animation loop.
                        animationPreferredSize = superGetPreferredSize();
                        animationMinimumSize = superGetMinimumSize();
                        
                        raven.modal.option.LayoutOption layoutOption = option.getLayoutOption();
                        animationStartRect = raven.modal.layout.OptionLayoutUtils.getLayoutLocation(
                                modalContainer, modalContainer.getOwner(), ModalController.this, 0f, layoutOption);
                        animationEndRect = raven.modal.layout.OptionLayoutUtils.getLayoutLocation(
                                modalContainer, modalContainer.getOwner(), ModalController.this, 1f, layoutOption);
                        
                        modalContainerLayer.animatedBegin();
                        systemScaleFactor = UIScale.getSystemScaleFactor(getGraphicsConfiguration());
                        if (option.isSnapshotAnimationEnabled()) {
                            Border border = getBorder();
                            if (border != null) {
                                snapshotsImage = ImageSnapshots.createSnapshotsImage(panelSlider, ModalController.this, getBorder(), systemScaleFactor);
                            } else {
                                snapshotsImage = ImageSnapshots.createSnapshotsImage(panelSlider, 0);
                            }
                            panelSlider.setVisible(false);
                        } else {
                            snapshotsImage = null;
                            panelSlider.setVisible(true);
                        }
                        display = true;
                    }

                    @Override
                    public void end() {
                        // Release size cache so post-animation layouts use real values.
                        animationPreferredSize = null;
                        animationMinimumSize = null;
                        animationStartRect = null;
                        animationEndRect = null;
                        modalContainerLayer.animatedEnd();
                        if (!showing) {
                            remove();
                        } else {
                            panelSlider.setVisible(true);
                            modalOpened();
                        }
                        if (snapshotsImage != null) {
                            snapshotsImage.flush();
                            snapshotsImage = null;
                        }
                        if (liveScaleBuffer != null) {
                            liveScaleBuffer.flush();
                            liveScaleBuffer = null;
                        }
                    }
                });
                animator.setInterpolator(CubicBezierEasing.STANDARD_EASING);
            }
            if (animator.isRunning()) {
                animator.stop();
            }
            this.showing = show;
            if (showing) {
                animated = 0;
            } else {
                animated = 1;
            }
            animator.start();
        } else {
            this.showing = show;
            if (showing) {
                animated = 1;
                display = true;
                modalContainer.getModalLayout().setAnimate(animated);
                modalOpened();
            } else {
                animated = 0;
                remove();
            }
        }
    }

    @Override
    public ModalContainer getModalContainer() {
        return modalContainer;
    }

    @Override
    protected PanelSlider.PaneSliderLayoutSize createSliderLayoutSize() {
        return (container, component) -> modalContainer.getModalComponentSize(component, container);
    }

    @Override
    protected MouseAdapter createMouseMovableListener() {
        return new ModalMouseMovableListener(this) {
            @Override
            protected Container getParent() {
                return modalContainer;
            }

            @Override
            protected Component getOwner() {
                return modalContainer.getOwner();
            }

            @Override
            protected void updateLayout() {
                modalContainer.revalidate();
            }
        };
    }

    @Override
    protected void onModalComponentInstalled() {
        ComponentOrientation orientation = modalContainer.getComponentOrientation();
        setComponentOrientation(orientation);
        if (modal.getComponentOrientation().isLeftToRight() != orientation.isLeftToRight()) {
            modal.applyComponentOrientation(orientation);
        }
    }

    @Override
    protected void onShowing() {
        setFocusCycleRoot(true);
        startAnimator(true);
    }

    @Override
    public void closeModal() {
        if (showing) {
            startAnimator(false);
        }
    }

    public void closeImmediately() {
        if (option.isAnimationEnabled()) {
            if (animator != null && animator.isRunning()) {
                showing = false;
                animator.stop();
            } else {
                remove();
            }
        } else {
            remove();
        }
    }

    protected void remove() {
        modalContainer.uninstallOption();
        modalContainerLayer.remove(this);
    }

    @Override
    public void paint(Graphics g) {
        if (display) {
            if (snapshotsImage != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(animated));
                try {
                    // draw snapshots image
                    if (systemScaleFactor > 1) {
                        HiDPIUtils.paintAtScale1x(g2, 0, 0, 100, 100, // width and height are not used
                                (g2d, x2, y2, width2, height2, scaleFactor2) -> {
                                    float scaleValue = option.getLayoutOption().getAnimateScale();
                                    if (scaleValue != 0) {
                                        scaleGraphics(g2d, scaleValue);
                                    }
                                    g2d.drawImage(snapshotsImage, x2, y2, null);
                                });
                    } else {
                        float scaleValue = option.getLayoutOption().getAnimateScale();
                        if (scaleValue != 0) {
                            scaleGraphics(g2, scaleValue);
                        }
                        g2.drawImage(snapshotsImage, 0, 0, null);
                    }
                } finally {
                    g2.dispose();
                }
            } else {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(animated));
                try {
                    float scaleValue = option.getLayoutOption().getAnimateScale();
                    if (scaleValue != 0) {
                        // Rasterize to off-screen buffer first, then scale the image.
                        // Applying Graphics2D.scale() directly to live Swing components
                        // causes sub-pixel text rendering artifacts.
                        Image buffer = getOrCreateLiveScaleBuffer();
                        if (buffer != null) {
                            Graphics2D bg = (Graphics2D) buffer.getGraphics();
                            try {
                                bg.setBackground(new Color(0, 0, 0, 0));
                                bg.clearRect(0, 0, getWidth(), getHeight());
                                super.paint(bg);
                            } finally {
                                bg.dispose();
                            }
                            if (systemScaleFactor > 1) {
                                HiDPIUtils.paintAtScale1x(g2, 0, 0, 100, 100,
                                        (g2d, x2, y2, w2, h2, sf) -> {
                                            scaleGraphics(g2d, scaleValue);
                                            g2d.drawImage(buffer, x2, y2, null);
                                        });
                            } else {
                                scaleGraphics(g2, scaleValue);
                                g2.drawImage(buffer, 0, 0, null);
                            }
                        } else {
                            // fallback if buffer creation fails
                            scaleGraphics(g2, scaleValue);
                            super.paint(g2);
                        }
                    } else {
                        super.paint(g2);
                    }
                } finally {
                    g2.dispose();
                }
            }
        }
    }

    private void scaleGraphics(Graphics2D g2, float scaleValue) {
        float minScale = 1f - scaleValue;

        float scale = minScale + (scaleValue * animated);
        float width = getWidth();
        float height = getHeight();

        // calculate the center position after scaling
        float scaledWidth = (width * scale);
        float scaledHeight = (height * scale);
        float x = (width - scaledWidth) / 2f;
        float y = (height - scaledHeight) / 2f;
        g2.translate(x, y);
        g2.scale(scale, scale);
    }

    private VolatileImage getOrCreateLiveScaleBuffer() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return null;
        if (liveScaleBuffer == null || liveScaleBuffer.getWidth() != w || liveScaleBuffer.getHeight() != h) {
            if (liveScaleBuffer != null) {
                liveScaleBuffer.flush();
            }
            liveScaleBuffer = createVolatileImage(w, h);
        }
        return liveScaleBuffer;
    }

    public float getAnimated() {
        return animated;
    }

    @Override
    public Dimension getPreferredSize() {
        if (animationPreferredSize != null) {
            return new Dimension(animationPreferredSize);
        }
        return super.getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        if (animationMinimumSize != null) {
            return new Dimension(animationMinimumSize);
        }
        return super.getMinimumSize();
    }

    // Call super directly to capture the real layout sizes at animation start.
    private Dimension superGetPreferredSize() {
        return super.getPreferredSize();
    }

    private Dimension superGetMinimumSize() {
        return super.getMinimumSize();
    }
}
