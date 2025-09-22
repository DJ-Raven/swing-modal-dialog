package test;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatSystemProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.component.pagination.DefaultPaginationItemRenderer;
import raven.modal.demo.component.pagination.Page;
import raven.modal.demo.component.pagination.Pagination;
import raven.modal.utils.FlatLafStyleUtils;
import test.base.BaseFrame;
import test.pagination.PaginationAnimation;

import java.awt.*;

public class TestPagination extends BaseFrame {

    public TestPagination() {
        super("Test Toast");
        setLayout(new MigLayout("al center center,wrap,gap 15", "[center]"));

        Pagination pagination = new Pagination(11, 0, 50);
        Pagination pagination1 = new Pagination(11, 0, 50);
        Pagination pagination2 = new Pagination(11, 0, 50);
        Pagination pagination3 = new Pagination(11, 0, 50);

        PaginationAnimation paginationAnimation = new PaginationAnimation(11, 0, 50);

        pagination1.setItemRenderer(new DefaultPaginationItemRenderer() {
            @Override
            public Component getPaginationItemRendererComponent(Pagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
                super.getPaginationItemRendererComponent(pagination, page, isSelected, isPressed, hasFocus, index);
                FlatLafStyleUtils.appendStyle(this, "" +
                        "arc:0;");
                return this;
            }
        });

        pagination2.setItemRenderer(new DefaultPaginationItemRenderer() {
            @Override
            public Component getPaginationItemRendererComponent(Pagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
                super.getPaginationItemRendererComponent(pagination, page, isSelected, isPressed, hasFocus, index);
                FlatLafStyleUtils.appendStyle(this, "" +
                        "arc:999;");
                return this;
            }
        });

        pagination3.setItemGap(1);
        pagination3.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:$Component.borderColor;" +
                "border:1,1,1,1,$Component.borderColor,,1;");
        pagination3.setItemRenderer(new DefaultPaginationItemRenderer() {
            @Override
            public Component getPaginationItemRendererComponent(Pagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
                super.getPaginationItemRendererComponent(pagination, page, isSelected, isPressed, hasFocus, index);
                FlatLafStyleUtils.appendStyle(this, "" +
                        "arc:0;" +
                        "borderWidth:0;" +
                        "focusWidth:0;");
                return this;
            }
        });

        add(pagination);
        add(pagination1);
        add(pagination2);
        add(pagination3);
        add(paginationAnimation);
    }

    public static void main(String[] args) {
        System.setProperty(FlatSystemProperties.UI_SCALE, "100%");
        installLaf();
        EventQueue.invokeLater(() -> new TestPagination().setVisible(true));
    }
}
