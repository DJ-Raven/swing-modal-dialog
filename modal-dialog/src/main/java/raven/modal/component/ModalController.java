package raven.modal.component;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import net.miginfocom.swing.MigLayout;
import raven.modal.option.BorderOption;
import raven.modal.option.Option;
import raven.modal.slider.PanelSlider;
import raven.modal.slider.SimpleTransition;
import raven.modal.slider.SliderTransition;
import raven.modal.utils.ImageSnapshots;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * @author Raven
 */
public class ModalController extends JPanel {

    private final ModalContainerLayer modalContainerLayer;
    private final ModalContainer modalContainer;
    private Modal modal;
    private PanelSlider panelSlider;
    private Option option;
    private Animator animator;
    private boolean showing;
    private float animated;
    private boolean display;
    private Image snapshotsImage;

    private Stack<Modal> modalStack;
    private Consumer onBackAction;

    public ModalController(ModalContainerLayer modalContainerLayer, ModalContainer modalContainer, Option option) {
        this.modalContainerLayer = modalContainerLayer;
        this.modalContainer = modalContainer;
        this.option = option;
        init();
    }

    private void init() {
        // create mouse event to block the component
        addMouseListener(new MouseAdapter() {
        });

        Insets shadowSize = option.getBorderOption().getShadowSize();
        int minimumSize = ModalUtils.maximumInsets(shadowSize);
        setLayout(new MigLayout("fill,insets 0", "[fill," + minimumSize + "::]", "[fill," + minimumSize + "::]"));
        setOpaque(false);

        panelSlider = new PanelSlider(createSliderLayoutSize());
        initBorder();
        add(panelSlider);
    }

    private PanelSlider.PaneSliderLayoutSize createSliderLayoutSize() {
        return (container, component) -> modalContainer.getModalComponentSize(component, container);
    }

    private void initBorder() {
        if (option == null) {
            return;
        }
        BorderOption borderOption = option.getBorderOption();
        Border border = borderOption.createBorder();
        if (border != null) {
            setBorder(border);
        }
    }

    private void pushStack(Modal modal) {
        if (modalStack == null) {
            modalStack = new Stack<>();
        }
        modalStack.push(modal);
    }

    public void addModal(Modal modal) {
        display = false;
        modal.setController(this);
        panelSlider.addSlide(modal, null);
        this.modal = modal;
    }

    public void pushModal(Modal modal) {
        installModalComponent(modal);
        if (modal instanceof SimpleModalBorder) {
            SimpleModalBorder simpleModalBorder = (SimpleModalBorder) modal;
            simpleModalBorder.applyBackButton(getOnBackAction());
        }
        pushStack(this.modal);
        this.modal = modal;
        modal.setController(this);
        int sliderDuration = option.getSliderDuration();
        SliderTransition sliderTransition = sliderDuration > 0 ? SimpleTransition.get(SimpleTransition.SliderType.FORWARD) : null;
        panelSlider.addSlide(modal, sliderTransition, sliderDuration);
    }

    public void popModal() {
        if (modalStack != null && !modalStack.isEmpty()) {
            Modal component = modalStack.pop();
            this.modal = component;
            int sliderDuration = option.getSliderDuration();
            SliderTransition sliderTransition = sliderDuration > 0 ? SimpleTransition.get(SimpleTransition.SliderType.BACK) : null;
            panelSlider.addSlide(component, sliderTransition, sliderDuration);
        }
    }

    public void showModal() {
        setFocusCycleRoot(true);
        installModalComponent(modal);
        startAnimator(true);
    }

    public void closeModal() {
        if (showing) {
            startAnimator(false);
        }
    }

    public void close() {
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

    public void removeModal() {
        panelSlider.remove(modal);
    }

    public void addModal() {
        if (panelSlider != null) {
            panelSlider.add(modal);
        }
    }

    public void startAnimator(boolean show) {
        if (option.isAnimationEnabled()) {
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
                        modalContainer.showSnapshot();
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
                        modalContainer.hideSnapshot();
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

    private void modalOpened() {
        SwingUtilities.invokeLater(() -> {
            initFocus();
            modal.modalOpened();
        });
    }

    private void initFocus() {
        modal.requestFocus();
    }

    private void installModalComponent(Modal modal) {
        // install the modal component for the first show
        if (!modal.isInstalled()) {
            modal.installComponent();
            modal.setInstalled(true);
        }

        // apply component orientation
        ComponentOrientation orientation = modalContainerLayer.getRootPaneContainer().getRootPane().getComponentOrientation();
        modal.applyComponentOrientation(orientation);
    }

    private void remove() {
        modalContainerLayer.removeContainer(modalContainer);
        modalContainerLayer.repaint();
        modalContainerLayer.revalidate();
    }

    @Override
    public Color getBackground() {
        if (panelSlider == null) {
            return super.getBackground();
        }
        return panelSlider.getBackground();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        initBorder();
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

    public Option getOption() {
        return option;
    }

    public float getAnimated() {
        return animated;
    }

    public ModalContainer getModalContainer() {
        return modalContainer;
    }

    public ModalContainerLayer getModalContainerLayer() {
        return modalContainerLayer;
    }

    private Consumer getOnBackAction() {
        if (onBackAction == null) {
            onBackAction = o -> {
                popModal();
            };
        }
        return onBackAction;
    }
}
