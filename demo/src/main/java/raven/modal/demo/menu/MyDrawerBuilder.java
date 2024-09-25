package raven.modal.demo.menu;

import com.formdev.flatlaf.FlatClientProperties;
import raven.modal.demo.Demo;
import raven.modal.demo.forms.*;
import raven.modal.demo.system.AllForms;
import raven.modal.demo.system.Form;
import raven.modal.demo.system.FormManager;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.data.MenuItem;
import raven.modal.drawer.menu.*;
import raven.modal.drawer.renderer.DrawerStraightDotLineStyle;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;
import raven.extras.AvatarIcon;
import raven.modal.drawer.simple.header.SimpleHeaderStyle;

import javax.swing.*;
import java.util.Arrays;

public class MyDrawerBuilder extends SimpleDrawerBuilder {

    private MenuOption menuOption;

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        AvatarIcon icon = new AvatarIcon(getClass().getResource("/raven/modal/demo/drawer/image/profile.png"), 60, 60, 999);
        return new SimpleHeaderData()
                .setIcon(icon)
                .setTitle("Ra Ven")
                .setDescription("raven@gmail.com")
                .setHeaderStyle(new SimpleHeaderStyle() {
                    @Override
                    public void styleProfile(JLabel label) {
                        // apply border style to avatar label
                        label.putClientProperty(FlatClientProperties.STYLE, "" +
                                "border:2,2,2,2,$Component.borderColor,2,999");
                    }
                });
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
                .setTitle("Swing Modal Dialog")
                .setDescription("Version " + Demo.DEMO_VERSION);
    }

    @Override
    public MenuOption getSimpleMenuOption() {
        if (menuOption == null) {
            MenuItem items[] = new MenuItem[]{
                    new Item.Label("MAIN"),
                    new Item("Dashboard", "dashboard.svg", FormDashboard.class),
                    new Item.Label("SWING UI"),
                    new Item("Forms", "forms.svg")
                            .subMenu("Input", FormInput.class)
                            .subMenu("Table", FormTable.class)
                            .subMenu("Responsive Layout", FormResponsiveLayout.class),
                    new Item("Components", "components.svg")
                            .subMenu("Modal", FormModal.class)
                            .subMenu("Toast", FormToast.class)
                            .subMenu("Date Time", FormDateTime.class)
                            .subMenu("Avatar Icon", FormAvatarIcon.class),
                    new Item("Email", "email.svg")
                            .subMenu("Inbox")
                            .subMenu(
                                    new Item("Group Read")
                                            .subMenu("Read 1")
                                            .subMenu("Read 2")
                                            .subMenu(
                                                    new Item("Group Item")
                                                            .subMenu("Item 1")
                                                            .subMenu("Item 2")
                                                            .subMenu("Item 3")
                                                            .subMenu("Item 4")
                                                            .subMenu("Item 5")
                                                            .subMenu("Item 6")
                                            )
                                            .subMenu("Read 3")
                                            .subMenu("Read 4")
                                            .subMenu("Read 5")
                            )
                            .subMenu("Compost"),
                    new Item("Chat", "chat.svg"),
                    new Item("Calendar", "calendar.svg"),
                    new Item.Label("OTHER"),
                    new Item("Plugin", "plugin.svg")
                            .subMenu("Plugin 1")
                            .subMenu("Plugin 2")
                            .subMenu("Plugin 3"),
                    new Item("Setting", "setting.svg", FormSetting.class),
                    new Item("About", "about.svg"),
                    new Item("Logout", "logout.svg")
            };

            MenuOption simpleMenuOption = new MenuOption();

            simpleMenuOption.setMenuStyle(new MenuStyle() {

                @Override
                public void styleMenu(JComponent component) {
                    component.putClientProperty(FlatClientProperties.STYLE, ""
                            + "background:$Drawer.background");
                }

                @Override
                public void styleMenuItem(JButton menu, int[] index, boolean isMainItem) {
                    if (isMainItem) {
                        menu.putClientProperty(FlatClientProperties.STYLE, "" +
                                "selectedForeground:$Component.accentColor;" +
                                "selectedBackground:null;");
                    }
                }
            });
            simpleMenuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());
            simpleMenuOption.setMenuItemAutoSelectionMode(MenuOption.MenuItemAutoSelectionMode.SELECT_SUB_MENU_LEVEL);
            simpleMenuOption.addMenuEvent(new MenuEvent() {
                @Override
                public void selected(MenuAction action, int[] index) {
                    System.out.println("Drawer menu selected " + Arrays.toString(index));
                    Class<?> itemClass = action.getItem().getItemClass();
                    int i = index[0];
                    if (i == 8) {
                        action.consume();
                        FormManager.showAbout();
                        return;
                    } else if (i == 9) {
                        action.consume();
                        FormManager.logout();
                        return;
                    }
                    if (itemClass == null || !Form.class.isAssignableFrom(itemClass)) {
                        action.consume();
                        return;
                    }
                    Class<? extends Form> formClass = (Class<? extends Form>) itemClass;
                    FormManager.showForm(AllForms.getForm(formClass));
                }
            });

            simpleMenuOption.setMenus(items)
                    .setBaseIconPath("raven/modal/demo/drawer/icon")
                    .setIconScale(0.45f);

            menuOption = simpleMenuOption;
        }
        return menuOption;
    }

    @Override
    public int getDrawerWidth() {
        return 275;
    }

    @Override
    public int getOpenDrawerAt() {
        return 1000;
    }

    @Override
    public boolean openDrawerAtScale() {
        return false;
    }

    @Override
    public void build(DrawerPanel drawerPanel) {
        drawerPanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Drawer.background");
    }
}
