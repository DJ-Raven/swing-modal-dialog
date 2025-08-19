package raven.modal.demo.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatComboBoxUI;

import javax.swing.*;
import java.awt.*;

public class EmbeddedComboBox<T> extends JComboBox<T> {

    private JComponent embeddedComponent;

    public EmbeddedComboBox(T[] items) {
        super(items);
    }

    public EmbeddedComboBox() {
        setUI(new EmbeddedComboBoxUI());
    }

    @Override
    public void updateUI() {
        super.updateUI();
        EmbeddedComboBoxUI newUI = new EmbeddedComboBoxUI();
        if (embeddedComponent != null) {
            newUI.setEmbeddedComponent(embeddedComponent);
        }
        setUI(newUI);
    }

    public void setEmbedded(JComponent component) {
        ((EmbeddedComboBoxUI) getUI()).setEmbeddedComponent(embeddedComponent = component);
    }

    private static class EmbeddedComboBoxUI extends FlatComboBoxUI {

        private final JPanel panel;

        EmbeddedComboBoxUI() {
            panel = new JPanel();
            panel.setOpaque(false);
        }

        public void setEmbeddedComponent(JComponent component) {
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            if (component != null) {
                component.putClientProperty(FlatClientProperties.STYLE_CLASS, "inTextField");
                if (component instanceof JButton || component instanceof JToggleButton) {
                    component.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
                    if (!component.isCursorSet()) {
                        component.setCursor(Cursor.getDefaultCursor());
                    }
                } else if (component instanceof JToolBar) {
                    for (Component child : component.getComponents()) {
                        if (child instanceof JComponent) {
                            ((JComponent) child).putClientProperty(FlatClientProperties.STYLE_CLASS, "inTextField");
                        }
                    }
                    if (!component.isCursorSet()) {
                        component.setCursor(Cursor.getDefaultCursor());
                    }
                }
                panel.add(component, BorderLayout.CENTER);
            }
            panel.revalidate();
            panel.repaint();
        }

        @Override
        protected void installComponents() {
            super.installComponents();
            comboBox.add(panel);
        }

        @Override
        protected void uninstallComponents() {
            super.uninstallComponents();
            comboBox.remove(panel);
        }

        @Override
        protected LayoutManager createLayoutManager() {
            return new ComboBoxLayoutManager() {

                @Override
                public void layoutContainer(Container parent) {
                    super.layoutContainer(parent);
                    Insets insets = parent.getInsets();
                    Dimension arrowSize = arrowButton.getSize();
                    Dimension panelSize = panel.getPreferredSize();
                    int availableWidth = parent.getWidth() - (insets.left + insets.right) - arrowSize.width;
                    int availableHeight = parent.getHeight() - (insets.top + insets.bottom);
                    if (parent.getComponentOrientation().isLeftToRight()) {
                        panel.setBounds(
                                Math.max(insets.left + availableWidth - panelSize.width, 0),
                                insets.top + (availableHeight - panelSize.height) / 2,
                                Math.min(panelSize.width, availableWidth),
                                panelSize.height
                        );
                    } else {
                        panel.setBounds(
                                insets.left + arrowSize.width,
                                insets.top + (availableHeight - panelSize.height) / 2,
                                Math.min(panelSize.width, availableWidth),
                                panelSize.height
                        );
                    }
                }
            };
        }

        @Override
        protected Rectangle rectangleForCurrentValue() {
            Rectangle rectangle = super.rectangleForCurrentValue();
            rectangle.width -= panel.getWidth();
            if (!comboBox.getComponentOrientation().isLeftToRight()) {
                rectangle.x += panel.getWidth();
            }
            return rectangle;
        }
    }
}
