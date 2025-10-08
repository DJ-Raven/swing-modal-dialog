package raven.extras.pagination;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public class DefaultPaginationItemRenderer extends JButton implements PaginationItemRenderer {

    protected boolean isSelected;

    @Override
    public Component getPaginationItemRendererComponent(Pagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
        this.isSelected = isSelected;
        setEnabled(true);
        if (page.getType() == Page.Type.PAGE) {
            setIcon(null);
            setText(page.getValue() + "");
        } else if (page.getType() == Page.Type.ELLIPSIS) {
            setIcon(null);
            setText("...");
        } else if (page.getType() == Page.Type.PREVIOUS) {
            setIcon(new FlatSVGIcon("raven/extras/icon/back.svg", 0.25f)
                    .setColorFilter(new FlatSVGIcon.ColorFilter(color -> getForeground())));
            setText("");
            if (!pagination.hasPrevious() && !pagination.isLoop()) {
                setEnabled(false);
            }
        } else if (page.getType() == Page.Type.NEXT) {
            setIcon(new FlatSVGIcon("raven/extras/icon/next.svg", 0.25f)
                    .setColorFilter(new FlatSVGIcon.ColorFilter(color -> getForeground())));
            setText("");
            if (!pagination.hasNext() && !pagination.isLoop()) {
                setEnabled(false);
            }
        }

        putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:3,3,3,3;");
        getModel().setRollover(hasFocus);
        getModel().setPressed(isPressed);
        return this;
    }

    @Override
    public boolean isDefaultButton() {
        return isSelected;
    }
}
