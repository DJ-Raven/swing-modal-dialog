package raven.modal.drawer.simple;

import com.formdev.flatlaf.FlatClientProperties;
import raven.modal.drawer.DrawerBuilder;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.menu.AbstractMenuElement;
import raven.modal.drawer.menu.DrawerMenu;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.drawer.simple.footer.LightDarkButtonFooter;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeader;
import raven.modal.drawer.simple.header.SimpleHeaderData;
import raven.modal.option.LayoutOption;
import raven.modal.option.Location;
import raven.modal.option.Option;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public abstract class SimpleDrawerBuilder implements DrawerBuilder {

    protected final MenuOption menuOption;
    protected AbstractMenuElement header;
    protected JSeparator headerSeparator;
    protected JScrollPane menuScroll;
    protected DrawerMenu menu;
    protected AbstractMenuElement footer;
    protected Option option;
    protected boolean isOpen;

    public SimpleDrawerBuilder(MenuOption menuOption) {
        this.menuOption = menuOption;
        init();
    }

    private void init() {
        header = new SimpleHeader(getSimpleHeaderData());
        MenuOption simpleMenuOption = getSimpleMenuOption();
        menu = new DrawerMenu(simpleMenuOption);
        menuScroll = createScroll(menu);
        footer = new LightDarkButtonFooter(getSimpleFooterData());
        option = new Option();
        option.setDuration(300);
        option.getBorderOption()
                .setRound(0);
        LayoutOption layoutOption = new SimpleDrawerLayoutOption(this)
                .setCompactSize(getDrawerCompactWidth(), 1f)
                .setSize(getDrawerWidth(), 1f)
                .setMargin(0)
                .setAnimateDistance(-0.7f, 0)
                .setLocation(Location.LEADING, Location.TOP);
        option.setLayoutOption(layoutOption);
    }

    protected JScrollPane createScroll(JComponent component) {
        JScrollPane scroll = new JScrollPane(component);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        String background = FlatLafStyleUtils.getStyleValue(component, "background", "null");
        scroll.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:" + background);
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        scroll.getHorizontalScrollBar().setUnitIncrement(10);
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "width:6;" +
                "trackArc:999;" +
                "thumbInsets:0,0,0,3;" +
                "trackInsets:0,0,0,3;" +
                "background:" + background);
        if (!background.equals("null")) {
            FlatLafStyleUtils.appendStyleIfAbsent(scroll.getVerticalScrollBar(), "" +
                    "track:" + background);
        }
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    @Override
    public Component getHeader() {
        return header;
    }

    @Override
    public Component getHeaderSeparator() {
        return headerSeparator;
    }

    @Override
    public Component getMenu() {
        return menuScroll;
    }

    @Override
    public Component getFooter() {
        return footer;
    }

    @Override
    public Option getOption() {
        return option;
    }

    @Override
    public int getDrawerWidth() {
        return 270;
    }

    @Override
    public int getDrawerCompactWidth() {
        return 80;
    }

    @Override
    public int getOpenDrawerAt() {
        return -1;
    }

    @Override
    public boolean openDrawerAtScale() {
        return true;
    }

    @Override
    public void drawerOpenChanged(boolean isOpen) {
        this.isOpen = isOpen;
        MenuOption.MenuOpenMode menuOpenMode = getMenuOpenMode();
        menu.setMenuOpenMode(menuOpenMode);
        header.setMenuOpenMode(menuOpenMode);
        footer.setMenuOpenMode(menuOpenMode);
    }

    public void drawerOpenChanged() {
        drawerOpenChanged(isOpen);
    }

    public void build(DrawerPanel drawerPanel) {
    }

    public void rebuildMenu() {
        if (menu != null) {
            menu.rebuildMenu();
            menu.setMenuOpenMode(getMenuOpenMode());
        }
    }

    public DrawerMenu getDrawerMenu() {
        return menu;
    }

    public boolean isDrawerOpen() {
        return isOpen;
    }

    private MenuOption.MenuOpenMode getMenuOpenMode() {
        if (isOpen) {
            return getSimpleMenuOption().getMenuOpenMode();
        } else {
            return MenuOption.MenuOpenMode.FULL;
        }
    }

    public final MenuOption getSimpleMenuOption() {
        return menuOption;
    }

    public abstract SimpleHeaderData getSimpleHeaderData();

    public abstract SimpleFooterData getSimpleFooterData();
}
