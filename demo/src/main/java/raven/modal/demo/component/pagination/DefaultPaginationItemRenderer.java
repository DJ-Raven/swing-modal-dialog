package raven.modal.demo.component.pagination;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public class DefaultPaginationItemRenderer extends JButton implements PaginationItemRenderer {

    protected boolean isSelected;

    @Override
    public Component getPaginationItemRendererComponent(Pagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
        this.isSelected = isSelected;
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

        putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:5,5,5,5;");
        getModel().setRollover(hasFocus);
        getModel().setPressed(isPressed);
        return this;
    }

    @Override
    public boolean isDefaultButton() {
        return isSelected;
    }
}
