package raven.modal.demo.component.pagination;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public class DefaultPaginationItemRenderer extends JButton implements PaginationItemRenderer {

    @Override
    public Component getPaginationItemRendererComponent(Pagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
        if (page.getType() == Page.Type.PAGE) {
            setIcon(null);
            setText(page.getValue() + "");
        } else if (page.getType() == Page.Type.ELLIPSIS) {
            setIcon(null);
            setText("...");
        } else if (page.getType() == Page.Type.PREVIOUS) {
            setIcon(new FlatSVGIcon("raven/modal/demo/icons/pagination/back.svg", 0.25f)
                    .setColorFilter(new FlatSVGIcon.ColorFilter(color -> getForeground())));
            setText("");
        } else if (page.getType() == Page.Type.NEXT) {
            setIcon(new FlatSVGIcon("raven/modal/demo/icons/pagination/next.svg", 0.25f)
                    .setColorFilter(new FlatSVGIcon.ColorFilter(color -> getForeground())));
            setText("");
        }

        String style = "margin:5,5,5,5;";
        if (isSelected) {
            style += "foreground:$Button.default.foreground;";
            if (isPressed) {
                style += "background:$Button.default.pressedBackground;";
            } else {
                style += "background:$Button.default.background;";
            }
        } else if (isPressed) {
            style += "background:$Button.pressedBackground;";
        }
        if (hasFocus) {
            style += "borderColor:$Button.focusedBorderColor;";
            if (!isPressed && !isSelected) {
                style += "background:$Button.hoverBackground;";
            }
        }
        putClientProperty(FlatClientProperties.STYLE, style);
        return this;
    }
}
