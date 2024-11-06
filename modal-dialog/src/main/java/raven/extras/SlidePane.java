package raven.extras;

import raven.modal.slider.PanelSlider;

import java.awt.*;

/**
 * @author Raven
 */
public class SlidePane extends PanelSlider {

    public SlidePane() {
        super(createDefaultSlideLayoutSize());
    }

    public SlidePane(PanelSlider.PaneSliderLayoutSize paneSliderLayoutSize) {
        super(paneSliderLayoutSize);
    }

    private static PanelSlider.PaneSliderLayoutSize createDefaultSlideLayoutSize() {
        return (container, component) -> container.getSize();
    }

    public void addSlide(Component component) {
        super.addSlide(component, null);
    }

    public void addSlide(Component component, SlidePaneTransition transition) {
        super.addSlide(component, transition);
    }

    public void addSlide(Component component, SlidePaneTransition.Type type) {
        super.addSlide(component, SlidePaneTransition.create(type));
    }

    public void addSlide(Component component, SlidePaneTransition transition, int duration) {
        super.addSlide(component, transition, duration);
    }

    public void addSlide(Component component, SlidePaneTransition.Type type, int duration) {
        super.addSlide(component, SlidePaneTransition.create(type), duration);
    }
}
