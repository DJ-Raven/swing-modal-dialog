package raven.modal.drawer;

import raven.modal.option.Option;

import java.awt.*;

/**
 * @author Raven
 */
public interface DrawerBuilder {

    void build(DrawerPanel drawerPanel);

    Component getHeader();

    Component getHeaderSeparator();

    Component getMenu();

    Component getFooter();

    Option getOption();

    int getDrawerWidth();

    int getDrawerCompactWidth();

    int getOpenDrawerAt();

    boolean openDrawerAtScale();

    void drawerOpenChanged(boolean isOpen);
}
