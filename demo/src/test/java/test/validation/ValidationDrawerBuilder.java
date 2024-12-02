package test.validation;

import com.formdev.flatlaf.FlatClientProperties;
import raven.extras.AvatarIcon;
import raven.modal.demo.forms.*;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.data.MenuItem;
import raven.modal.drawer.menu.*;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeader;
import raven.modal.drawer.simple.header.SimpleHeaderData;

import javax.swing.*;
import java.util.Arrays;

public class ValidationDrawerBuilder extends SimpleDrawerBuilder {

    private static ValidationDrawerBuilder instance;

    public static ValidationDrawerBuilder getInstance() {
        if (instance == null) {
            instance = new ValidationDrawerBuilder();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        boolean updateMenuItem = this.user == null || this.user.getRole() != user.getRole();
        this.user = user;

        // change drawer header
        SimpleHeader header = (SimpleHeader) getHeader();
        header.setSimpleHeaderData(createHeaderData(user));

        if (updateMenuItem) {
            // rebuild for update the menu item
            rebuildMenu();
        }
    }

    private User user;

    private ValidationDrawerBuilder() {
        super(createSimpleMenuOption());
    }

    private SimpleHeaderData createHeaderData(User user) {
        if (user == null) {
            return new SimpleHeaderData()
                    .setTitle("No User")
                    .setDescription("No description");
        } else {
            AvatarIcon icon = new AvatarIcon(user.getProfile(), 50, 50, 3.5f);
            icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
            icon.setBorder(2, 2);
            icon.setBorderColor(new AvatarIcon.BorderColor(UIManager.getColor("Component.accentColor"), 0.7f));

            return new SimpleHeaderData()
                    .setIcon(icon)
                    .setTitle(user.getName())
                    .setDescription(user.getDescription());
        }
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return createHeaderData(user);
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData();
    }

    @Override
    public int getOpenDrawerAt() {
        return 1;
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

    private static MenuOption createSimpleMenuOption() {
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
        simpleMenuOption.setMenus(items);
        simpleMenuOption.setMenuValidation(new MenuValidation() {

            private boolean checkMenu(int[] index, int[] indexHide) {
                if (index.length == indexHide.length) {
                    for (int i = 0; i < index.length; i++) {
                        if (index[i] != indexHide[i]) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }

            @Override
            public boolean menuValidation(int[] index) {
                if (instance == null || instance.getUser() == null) {
                    return false;
                }
                if (!instance.getUser().isAdmin()) {
                    boolean act
                            // `Form`->`Table`
                            = checkMenu(index, new int[]{1, 1})
                            // `Calendar`
                            && checkMenu(index, new int[]{5})
                            // `Email`->`Group Read`->`Group Item->`Item 4`
                            && checkMenu(index, new int[]{3, 1, 2, 3});
                    return act;
                }
                return true;
            }
        });

        simpleMenuOption.setMenuStyle(new MenuStyle() {

            @Override
            public void styleMenu(JComponent component) {
                component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
            }
        });
        simpleMenuOption.setMenuItemAutoSelectionMode(MenuOption.MenuItemAutoSelectionMode.SELECT_SUB_MENU_LEVEL);

        simpleMenuOption.addMenuEvent(new MenuEvent() {
            @Override
            public void selected(MenuAction action, int[] index) {
                System.out.println("Drawer menu selected " + Arrays.toString(index));
            }
        });

        simpleMenuOption.setMenus(items)
                .setBaseIconPath("raven/modal/demo/drawer/icon")
                .setIconScale(0.45f);

        return simpleMenuOption;
    }
}
