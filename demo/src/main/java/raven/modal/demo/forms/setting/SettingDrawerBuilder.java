package raven.modal.demo.forms.setting;

import raven.modal.demo.forms.setting.tabb.Appearance;
import raven.modal.drawer.item.Item;
import raven.modal.drawer.item.MenuItem;
import raven.modal.drawer.menu.AbstractMenuElement;
import raven.modal.drawer.menu.MenuEvent;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.drawer.renderer.DrawerNoneLineStyle;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;

public class SettingDrawerBuilder extends SimpleDrawerBuilder {

    public SettingDrawerBuilder(MenuEvent event) {
        super(createSimpleMenuOption(event));
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData()
                .setTitle("Settings");
    }

    @Override
    public AbstractMenuElement createFooter() {
        return null;
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return null;
    }

    public static MenuOption createSimpleMenuOption(MenuEvent event) {

        // create simple menu option
        MenuOption simpleMenuOption = new MenuOption();

        MenuItem items[] = new MenuItem[]{
                new Item("General")
                        .subMenu("Appearance", Appearance.class)
                        .subMenu("User profile")
                        .subMenu("Contact"),
                new Item("Folder"),
                new Item("Schedule"),
                new Item.Label("OTHER"),
                new Item("Plugin")
        };

        simpleMenuOption.addMenuEvent(event);
        simpleMenuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerNoneLineStyle());

        simpleMenuOption.setMenus(items);

        return simpleMenuOption;
    }
}
