package raven.modal.demo.menu;

import com.formdev.flatlaf.FlatClientProperties;
import raven.modal.demo.system.AllForms;
import raven.modal.demo.system.FormManager;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.data.MenuItem;
import raven.modal.drawer.menu.*;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;
import raven.extras.AvatarIcon;
import raven.modal.drawer.simple.header.SimpleHeaderStyle;

import javax.swing.*;
import java.util.Arrays;

public class MyDrawerBuilder extends SimpleDrawerBuilder {

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
                .setDescription("Version 1.0.0");
    }

    @Override
    public MenuOption getSimpleMenuOption() {

        MenuItem items[] = new MenuItem[]{
                new Item.Label("MAIN"),
                new Item("Dashboard", "dashboard.svg"),
                new Item.Label("SWING UI"),
                new Item("Forms", "forms.svg")
                        .subMenu("Input")
                        .subMenu("Table")
                        .subMenu("Responsive Layout"),
                new Item("Components", "components.svg")
                        .subMenu("Modal")
                        .subMenu("Toast")
                        .subMenu("Date Time")
                        .subMenu("Avatar Icon"),
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
                new Item("Setting", "setting.svg"),
                new Item("Logout", "logout.svg")
        };

        MenuOption simpleMenuOption = new MenuOption();
        simpleMenuOption.setMenuStyle(new MenuStyle() {

            @Override
            public void styleMenu(JComponent component) {
                component.putClientProperty(FlatClientProperties.STYLE, ""
                        + "background:$Drawer.background");
            }
        });
        simpleMenuOption.addMenuEvent(new MenuEvent() {
            @Override
            public void selected(MenuAction action, int[] index) {
                System.out.println("Drawer menu selected " + Arrays.toString(index));
                if (index.length == 1) {
                    int i = index[0];
                    if (i == 0) {
                        FormManager.showForm(AllForms.getFormDashboard());
                    } else if (i == 7) {
                        FormManager.showForm(AllForms.getFormSetting());
                    } else if (i == 8) {
                        FormManager.logout();
                    }
                } else if (index.length == 2) {
                    int i = index[0];
                    int j = index[1];
                    if (i == 1) {
                        if (j == 0) {
                            FormManager.showForm(AllForms.getFormInput());
                        } else if (j == 1) {
                            FormManager.showForm(AllForms.getFormTable());
                        } else if (j == 2) {
                            FormManager.showForm(AllForms.getFormResponsiveLayout());
                        }
                    } else if (i == 2) {
                        if (j == 0) {
                            FormManager.showForm(AllForms.getFormModal());
                        } else if (j == 1) {
                            FormManager.showForm(AllForms.getFormToast());
                        } else if (j == 2) {
                            FormManager.showForm(AllForms.getFormDateTime());
                        } else if (j == 3) {
                            FormManager.showForm(AllForms.getFormAvatarIcon());
                        }
                    }
                }
            }
        });

        simpleMenuOption.setMenus(items)
                .setBaseIconPath("raven/modal/demo/drawer/icon")
                .setIconScale(0.45f);
        return simpleMenuOption;
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
