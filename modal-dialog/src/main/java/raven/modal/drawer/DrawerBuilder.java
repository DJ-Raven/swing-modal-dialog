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

    int getOpenDrawerAt();

    boolean openDrawerAtScale();
}
