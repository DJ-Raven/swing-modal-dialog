package raven.modal.component;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.UIScale;
import net.miginfocom.swing.MigLayout;
import raven.modal.option.Option;
import raven.modal.slider.PanelSlider;
import raven.modal.slider.SimpleTransition;
import raven.modal.utils.ImageSnapshots;

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

        setLayout(new MigLayout("fill,insets 0", "[fill,5::]", "[fill,5::]"));
        setOpaque(false);
        if (option.getBorderWidth() > 0) {
            setBorder(new OutlineBorder(option.getBorderWidth(), option.getRound(), option.getBorderColor()));
        }
        panelSlider = new PanelSlider((int) (option.getRound() / 2f));
        add(panelSlider);
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
            simpleModalBorder.createBackButton(getOnBackAction());
        }
        pushStack(this.modal);
        this.modal = modal;
        modal.setController(this);
        modal.grabFocus();
        panelSlider.addSlide(modal, SimpleTransition.get(option.isAnimationEnabled() ? SimpleTransition.SliderType.FORWARD : null));
    }

    public void popModal() {
        if (modalStack != null && !modalStack.isEmpty()) {
            Modal component = modalStack.pop();
            this.modal = component;
            modal.grabFocus();
            panelSlider.addSlide(component, SimpleTransition.get(option.isAnimationEnabled() ? SimpleTransition.SliderType.BACK : null));
        }
    }

    public void showModal() {
        setFocusCycleRoot(true);
        installModalComponent(modal);
        modal.grabFocus();
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
                        snapshotsImage = ImageSnapshots.createSnapshotsImage(panelSlider, getOptionRound());
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
            } else {
                animated = 0;
                remove();
            }
        }
    }

    private void installModalComponent(Modal modal) {
        // install the modal component for the first show
        if (!modal.isInstalled()) {
            modal.installComponent();
            modal.setInstalled(true);
        }
    }

    private void remove() {
        modalContainerLayer.removeContainer(modalContainer);
        modalContainerLayer.repaint();
        modalContainerLayer.revalidate();
    }

    private float getOptionRound() {
        return option.getRound() - option.getBorderWidth() * 2f;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Insets insets = getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);
        FlatUIUtils.setRenderingHints(g2d);
        g2d.setColor(getBackground());
        float arc = UIScale.scale(getOptionRound());
        g2d.setComposite(AlphaComposite.SrcOver.derive(animated));
        FlatUIUtils.paintComponentBackground(g2d, x, y, width, height, 0, arc);
        g2d.dispose();
        super.paintComponent(g);
    }

    @Override
    public void paint(Graphics g) {
        if (display) {
            if (snapshotsImage != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(animated));
                Insets insets = getInsets();
                // draw snapshots image
                g2.drawImage(snapshotsImage, insets.left, insets.top, null);
                Border border = getBorder();
                if (border != null) {
                    getBorder().paintBorder(this, g2, 0, 0, getWidth(), getHeight());
                }
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
