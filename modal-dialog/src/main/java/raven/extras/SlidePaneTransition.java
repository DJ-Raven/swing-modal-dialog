package raven.extras;

import raven.modal.slider.SimpleTransition;
import raven.modal.slider.SliderTransition;

/**
 * Transition class for SlidePane
 *
 * @author Raven
 */
public abstract class SlidePaneTransition extends SliderTransition {

    public static SliderTransition create(Type type) {
        if (type == Type.DEFAULT) {
            return SimpleTransition.get(SimpleTransition.SliderType.DEFAULT);
        } else if (type == Type.BACK) {
            return SimpleTransition.get(SimpleTransition.SliderType.BACK);
        } else if (type == Type.FORWARD) {
            return SimpleTransition.get(SimpleTransition.SliderType.FORWARD);
        } else if (type == Type.ZOOM_IN) {
            return SimpleTransition.get(SimpleTransition.SliderType.ZOOM_IN);
        } else if (type == Type.ZOOM_OUT) {
            return SimpleTransition.get(SimpleTransition.SliderType.ZOOM_OUT);
        } else if (type == Type.TOP_DOWN) {
            return SimpleTransition.get(SimpleTransition.SliderType.TOP_DOWN);
        } else if (type == Type.DOWN_TOP) {
            return SimpleTransition.get(SimpleTransition.SliderType.DOWN_TOP);
        }
        return null;
    }

    public enum Type {
        DEFAULT, BACK, FORWARD, ZOOM_IN, ZOOM_OUT, TOP_DOWN, DOWN_TOP
    }
}
