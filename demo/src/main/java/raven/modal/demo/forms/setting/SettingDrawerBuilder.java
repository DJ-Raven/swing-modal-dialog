package raven.modal.demo.forms.setting;

import raven.modal.demo.forms.setting.tabb.Appearance;
import raven.modal.drawer.item.Item;
import raven.modal.drawer.item.MenuItem;
import raven.modal.drawer.menu.MenuEvent;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;

public class SettingDrawerBuilder extends SimpleDrawerBuilder {

    public SettingDrawerBuilder(MenuEvent event) {
        super(createSimpleMenuOption(event));
        footer = null;
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData()
                .setTitle("Settings");
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData();
    }

    public static MenuOption createSimpleMenuOption(MenuEvent event) {

        // create simple menu option
        MenuOption simpleMenuOption = new MenuOption();

        MenuItem items[] = new MenuItem[]{
                new Item("General", "setting.svg")
                        .subMenu("Appearance", Appearance.class)
                        .subMenu("User profile")
                        .subMenu("Contact"),
                new Item("Folder", "folder.svg"),
                new Item("Schedule", "schedule.svg"),
                new Item.Label("OTHER"),
                new Item("Plugin", "plugin.svg")
        };

        simpleMenuOption.setMenuItemAutoSelectionMode(MenuOption.MenuItemAutoSelectionMode.SELECT_SUB_MENU_LEVEL);
        simpleMenuOption.addMenuEvent(event);

        simpleMenuOption.setMenus(items)
                .setBaseIconPath("raven/modal/demo/icons/setting")
                .setIconScale(0.4f);

        return simpleMenuOption;
    }
}
