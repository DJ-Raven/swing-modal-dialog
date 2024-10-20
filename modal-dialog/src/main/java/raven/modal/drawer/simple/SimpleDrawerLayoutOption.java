package raven.modal.drawer.simple;

import raven.modal.drawer.menu.MenuOption;
import raven.modal.option.LayoutOption;
import raven.modal.utils.DynamicSize;

/**
 * @author Raven
 */
public class SimpleDrawerLayoutOption extends LayoutOption {

    public DynamicSize getCompactSize() {
        return compactSize;
    }

    private final SimpleDrawerBuilder drawerBuilder;

    private DynamicSize compactSize = new DynamicSize(-1, -1);

    public SimpleDrawerLayoutOption(SimpleDrawerBuilder drawerBuilder) {
        this.drawerBuilder = drawerBuilder;
    }

    public SimpleDrawerLayoutOption setCompactSize(Number width, Number height) {
        this.compactSize = new DynamicSize(width, height);
        return this;
    }

    @Override
    public DynamicSize getSize() {
        if (drawerBuilder.isDrawerOpen() && drawerBuilder.getSimpleMenuOption().getMenuOpenMode() == MenuOption.MenuOpenMode.COMPACT) {
            return compactSize;
        }
        return super.getSize();
    }

    public DynamicSize getFullSize() {
        return super.getSize();
    }
}
