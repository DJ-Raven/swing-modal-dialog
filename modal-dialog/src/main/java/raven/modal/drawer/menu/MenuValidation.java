package raven.modal.drawer.menu;

/**
 * @author Raven
 */
public class MenuValidation {

    protected boolean keepMenuValidationIndex;
    protected boolean removeLabelWhenEmptyMenu;

    public MenuValidation() {
        this(true, true);
    }

    public MenuValidation(boolean keepMenuValidationIndex, boolean removeLabelWhenEmptyMenu) {
        this.keepMenuValidationIndex = keepMenuValidationIndex;
        this.removeLabelWhenEmptyMenu = removeLabelWhenEmptyMenu;
    }

    public boolean menuValidation(int[] index) {
        return true;
    }

    public boolean labelValidation(int index) {
        return true;
    }
}
