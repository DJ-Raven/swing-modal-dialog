package raven.modal.component;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import raven.modal.option.Option;
import raven.modal.slider.PanelSlider;
import raven.modal.utils.ImageSnapshots;

import javax.swing.border.Border;
import java.awt.*;

/**
 * @author Raven
 */
public class ModalController extends AbstractModalController {

    private final AbstractModalContainerLayer modalContainerLayer;
    private final ModalContainer modalContainer;
    private boolean display;
    private boolean showing;
    private Animator animator;
    private float animated;
    private Image snapshotsImage;

    public ModalController(AbstractModalContainerLayer modalContainerLayer, ModalContainer modalContainer, Option option) {
        super(option);
        this.modalContainerLayer = modalContainerLayer;
        this.modalContainer = modalContainer;
    }

    public void startAnimator(boolean show) {
        if (option.isAnimationEnabled() && (show == true || option.isAnimationOnClose())) {
            if (animator == null) {
                animator = new Animator(option.getDuration(), new Animator.TimingTarget() {
                    @Override
                    public void timingEvent(float v) {
                        animated = showing ? v : 1f - v;
                        modalContainer.repaint();
                        modalContainer.getModalLayout().setAnimate(animated);
                        modalContainer.doLayout();
                    }

                    @Override
                    public void begin() {
                        modalContainerLayer.animatedBegin();
                        Border border = getBorder();
                        if (border != null) {
                            snapshotsImage = ImageSnapshots.createSnapshotsImage(panelSlider, ModalController.this, getBorder());
                        } else {
                            snapshotsImage = ImageSnapshots.createSnapshotsImage(panelSlider, 0);
                        }
                        panelSlider.setVisible(false);
                        display = true;
                    }

                    @Override
                    public void end() {
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
        modalContainerLayer.removeContainer(modalContainer);
    }

    @Override
    public void paint(Graphics g) {
        if (display) {
            if (snapshotsImage != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(animated));

                float scaleValue = option.getLayoutOption().getAnimateScale();
                if (scaleValue != 0) {
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

                // draw snapshots image
                g2.drawImage(snapshotsImage, 0, 0, null);
                g2.dispose();
            } else {
                super.paint(g);
            }
        }
    }

    public float getAnimated() {
        return animated;
    }
}
