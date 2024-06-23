package raven.modal.option;

/**
 * This class for SimpleModalBorder option
 *
 * @author Raven
 */
public class ModalBorderOption {

    public boolean isUseScroll() {
        return useScroll;
    }

    private boolean useScroll;

    public ModalBorderOption setUseScroll(boolean useScroll) {
        this.useScroll = useScroll;
        return this;
    }
}
