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
import raven.modal.drawer.simple.footer.LightDarkButtonFooter;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;
import raven.extras.AvatarIcon;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MyDrawerBuilder extends SimpleDrawerBuilder {

    private final int SHADOW_SIZE = 12;

    public MyDrawerBuilder() {
        super(createSimpleMenuOption());
        LightDarkButtonFooter lightDarkButtonFooter = (LightDarkButtonFooter) footer;
        lightDarkButtonFooter.addModeChangeListener(isDarkMode -> {
            // event for light dark mode changed
        });
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        AvatarIcon icon = new AvatarIcon(getClass().getResource("/raven/modal/demo/drawer/image/profile.png"), 50, 50, 3.5f);
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        icon.setBorder(2, 2);

        changeAvatarIconBorderColor(icon);

        UIManager.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("lookAndFeel")) {
                changeAvatarIconBorderColor(icon);
            }
        });

        return new SimpleHeaderData()
                .setIcon(icon)
                .setTitle("Ra Ven")
                .setDescription("raven@gmail.com");
    }

    private void changeAvatarIconBorderColor(AvatarIcon icon) {
        icon.setBorderColor(new AvatarIcon.BorderColor(UIManager.getColor("Component.accentColor"), 0.7f));
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
                .setTitle("Swing Modal Dialog")
                .setDescription("Version " + Demo.DEMO_VERSION);
    }


    public static MenuOption createSimpleMenuOption() {

        // create simple menu option
        MenuOption simpleMenuOption = new MenuOption();

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
                        .subMenu("Avatar Icon", FormAvatarIcon.class)
                        .subMenu("Slide Pane", FormSlidePane.class),
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

        simpleMenuOption.setMenuStyle(new MenuStyle() {

            @Override
            public void styleMenu(JComponent component) {
                component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
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

        return simpleMenuOption;
    }

    @Override
    public int getDrawerWidth() {
        return 270 + SHADOW_SIZE;
    }

    @Override
    public int getDrawerCompactWidth() {
        return 80 + SHADOW_SIZE;
    }

    @Override
    public int getOpenDrawerAt() {
        return 1000;
    }

    @Override
    public Option getOption() {
        Option option = super.getOption();
        option.setOpacity(0.3f);
        option.getBorderOption()
                .setShadowSize(new Insets(0, 0, 0, SHADOW_SIZE));
        return option;
    }

    @Override
    public boolean openDrawerAtScale() {
        return false;
    }

    @Override
    public void build(DrawerPanel drawerPanel) {
        drawerPanel.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
    }

    private static String getDrawerBackgroundStyle() {
        return "" +
                "[light]background:tint($Panel.background,20%);" +
                "[dark]background:tint($Panel.background,5%);";
    }
}
