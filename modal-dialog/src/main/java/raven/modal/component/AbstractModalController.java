package raven.modal.component;

import net.miginfocom.swing.MigLayout;
import raven.modal.option.BorderOption;
import raven.modal.option.Option;
import raven.modal.slider.PanelSlider;
import raven.modal.slider.SimpleTransition;
import raven.modal.slider.SliderTransition;
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
public abstract class AbstractModalController extends JPanel implements ControllerAction {

    protected final Option option;

    protected Modal modal;
    protected PanelSlider panelSlider;
    protected Stack<Modal> modalStack;
    protected Consumer<?> onBackAction;

    public AbstractModalController(Option option) {
        this.option = option;
        init();
    }

    private void init() {
        boolean movable = option.getLayoutOption().isMovable();
        if (movable) {
            MouseAdapter mouseListener = createMouseMovableListener();
            addMouseListener(mouseListener);
            addMouseMotionListener(mouseListener);
        } else {
            // create mouse event to block the component
            addMouseListener(new MouseAdapter() {
            });
        }

        Insets shadowSize = option.getBorderOption().getShadowSize();
        int minimumSize = ModalUtils.maximumInsets(shadowSize);
        setLayout(new MigLayout("fill,insets 0", "[fill," + minimumSize + "::]", "[fill," + minimumSize + "::]"));
        setOpaque(false);

        panelSlider = new PanelSlider(createSliderLayoutSize());
        panelSlider.setRequestFocusAfterSlide(true);
        panelSlider.setUseSlideAsBackground(true);
        panelSlider.setOpaque(true);
        initBorder();
        add(panelSlider);
    }

    protected void initBorder() {
        if (option == null) {
            return;
        }
        BorderOption borderOption = option.getBorderOption();
        if (!option.isHeavyWeight()) {
            Border border = borderOption.createBorder();
            if (border != null) {
                setBorder(border);
            }
        } else {
            int borderWidth = borderOption.getBorderWidth();
            if (borderWidth > 0 && !ModalUtils.isShadowAndRoundBorderSupport()) {
                // border width painted with round window border
                // but if windows round border not support we set the border width here
                setBorder(new ModalLineBorder(borderWidth, borderOption.getBorderColor(), 0));
            }
        }
    }

    protected void installModalComponent(Modal modal) {
        // install the modal component for the first show
        if (!modal.isInstalled()) {
            modal.installComponent();
            modal.setInstalled(true);
        }
        onModalComponentInstalled();
    }

    protected void modalOpened() {
        SwingUtilities.invokeLater(() -> {
            initFocus();
            modal.modalOpened();
        });
    }

    protected void initFocus() {
        modal.requestFocus();
    }

    private void pushStack(Modal modal) {
        if (modalStack == null) {
            modalStack = new Stack<>();
        }
        modalStack.push(modal);
    }

    public void initModal(Modal modal) {
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
        modal.setId(this.modal.getId());
        pushStack(this.modal);
        this.modal = modal;
        modal.setController(this);
        int sliderDuration = option.isHeavyWeight() ? 0 : option.getSliderDuration();
        ComponentOrientation orientation = getComponentOrientation();
        SliderTransition sliderTransition = sliderDuration > 0 ? SimpleTransition.get(SimpleTransition.SliderType.FORWARD) : null;
        if (modal.getComponentOrientation().isLeftToRight() != orientation.isLeftToRight()) {
            modal.applyComponentOrientation(orientation);
        }
        panelSlider.addSlide(modal, sliderTransition, sliderDuration, () -> modalOpened());
    }

    @Override
    public void popModal() {
        if (modalStack != null && !modalStack.isEmpty()) {
            Modal component = modalStack.pop();
            this.modal = component;
            int sliderDuration = option.isHeavyWeight() ? 0 : option.getSliderDuration();
            SliderTransition sliderTransition = sliderDuration > 0 ? SimpleTransition.get(SimpleTransition.SliderType.BACK) : null;
            panelSlider.addSlide(component, sliderTransition, sliderDuration, () -> modalOpened());
        }
    }

    public void addModal() {
        if (panelSlider != null) {
            panelSlider.add(modal);
        }
    }

    public void showModal() {
        installModalComponent(modal);
        onShowing();
    }

    public void removeModal() {
        panelSlider.remove(modal);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        initBorder();
    }

    @Override
    public Color getBackground() {
        if (panelSlider == null) {
            return super.getBackground();
        }
        return panelSlider.getBackground();
    }

    private Consumer<?> getOnBackAction() {
        if (onBackAction == null) {
            onBackAction = o -> {
                popModal();
            };
        }
        return onBackAction;
    }

    public Option getOption() {
        return option;
    }

    public String getId() {
        return modal.getId();
    }

    public abstract ModalContainer getModalContainer();

    protected abstract PanelSlider.PaneSliderLayoutSize createSliderLayoutSize();

    protected abstract MouseAdapter createMouseMovableListener();

    protected abstract void onModalComponentInstalled();

    protected abstract void onShowing();
}
