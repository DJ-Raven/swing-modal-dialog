package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.component.pagination.PaginationAnimation;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;
import raven.modal.utils.FlatLafStyleUtils;
import raven.swingpack.JPagination;
import raven.swingpack.pagination.DefaultPaginationItemRenderer;
import raven.swingpack.pagination.Page;
import raven.swingpack.pagination.PaginationItemRenderer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SystemForm(name = "Pagination", description = "pagination user interface component")
public class FormPagination extends Form {

    public FormPagination() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx", "[fill]"));
        add(createInfo());
        add(createOptions());
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel("Pagination");
        JTextPane text = new JTextPane();
        text.setText("A pagination component helps users move between pages of content, making it easier to view large amounts of data step by step.");
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createOptions() {
        JPanel panel = new JPanel(new MigLayout("wrap 1,fillx", "[fill]", "[fill]"));
        // no options

        panel.add(createPagination());
        return panel;
    }

    private Component createPagination() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Example"));

        // default
        JPagination defaultPagination = new JPagination(10, 1, 50);

        // circle pagination
        JPagination circlePagination = new JPagination(10, 1, 50);
        circlePagination.setItemRenderer(new DefaultPaginationItemRenderer() {
            @Override
            public Component getPaginationItemRendererComponent(JPagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
                super.getPaginationItemRendererComponent(pagination, page, isSelected, isPressed, hasFocus, index);
                FlatLafStyleUtils.appendStyle(this, "" +
                        "arc:999;");
                return this;
            }
        });

        // custom no border
        JPagination paginationNoBorder = new JPagination(10, 1, 50);
        paginationNoBorder.setItemGap(0);
        paginationNoBorder.setItemRenderer(new DefaultPaginationItemRenderer() {
            @Override
            public Component getPaginationItemRendererComponent(JPagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
                super.getPaginationItemRendererComponent(pagination, page, isSelected, isPressed, hasFocus, index);
                FlatLafStyleUtils.appendStyle(this, "" +
                        "arc:0;" +
                        "borderWidth:0;" +
                        "focusWidth:0;");
                return this;
            }
        });

        // custom animation
        JPagination paginationAnimation = new PaginationAnimation(10, 1, 50);

        // loop animation
        JPagination paginationLoop = new PaginationAnimation(7, 1, 7);
        paginationLoop.setLoop(true);
        paginationLoop.setItemSize(new Dimension(15, 15));
        paginationLoop.setItemGap(5);
        paginationLoop.setItemRenderer(new AnimatedLoopItemRenderer(paginationLoop));

        panel.add(new JLabel("Default:"));
        panel.add(defaultPagination, "gapy n 10");

        panel.add(new JLabel("Circle:"));
        panel.add(circlePagination, "gapy n 10");

        panel.add(new JLabel("No Border:"));
        panel.add(paginationNoBorder, "gapy n 10");

        panel.add(new JLabel("Custom with Animation:"));
        panel.add(paginationAnimation, "gapy n 10");

        panel.add(new JLabel("Custom with Animation loop:"));
        panel.add(paginationLoop);
        return panel;
    }

    private static class AnimatedLoopItemRenderer extends DefaultPaginationItemRenderer {

        private final PaginationItemRenderer oldRenderer;

        private AnimatedLoopItemRenderer(JPagination pagination) {
            oldRenderer = pagination.getItemRenderer();
        }

        @Override
        public Component getPaginationItemRendererComponent(JPagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
            JButton button = (JButton) oldRenderer.getPaginationItemRendererComponent(pagination, page, isSelected, isPressed, hasFocus, index);

            button.setText("");
            FlatLafStyleUtils.appendStyle(button, "" +
                    "background:$ProgressBar.background;" +
                    "arc:999;");
            return button;
        }

        @Override
        public void updateUI() {
            super.updateUI();
            if (oldRenderer instanceof JComponent) {
                SwingUtilities.updateComponentTreeUI((JComponent) oldRenderer);
            }
        }
    }
}
