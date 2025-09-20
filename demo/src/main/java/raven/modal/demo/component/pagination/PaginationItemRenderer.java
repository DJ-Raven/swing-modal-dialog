package raven.modal.demo.component.pagination;

import java.awt.*;

public interface PaginationItemRenderer {

    Component getPaginationItemRendererComponent(Pagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index);
}
