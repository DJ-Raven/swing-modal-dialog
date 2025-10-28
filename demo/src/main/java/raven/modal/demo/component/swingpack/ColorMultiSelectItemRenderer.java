package raven.modal.demo.component.swingpack;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.ColorFunctions;
import raven.swingpack.JMultiSelectComboBox;
import raven.swingpack.multiselect.DefaultMultiSelectItemRenderer;
import raven.swingpack.multiselect.MultiSelectCellRenderer;

import javax.swing.*;
import java.awt.*;

public class ColorMultiSelectItemRenderer extends DefaultMultiSelectItemRenderer {

    public static void apply(JMultiSelectComboBox<?> multiSelect) {
        multiSelect.setItemRenderer(new ColorMultiSelectItemRenderer());
        multiSelect.setRenderer(new ColorCellRenderer());
    }

    private ColorMultiSelectItemRenderer() {
    }

    public static Color getItemColor(Object item) {
        if (item instanceof ColorItem) {
            return ((ColorItem) item).getBaseOnSystemMode();
        }
        return null;
    }

    @Override
    public Component getMultiSelectItemRendererComponent(JMultiSelectComboBox<?> multiSelect, Object value, boolean isPressed, boolean hasFocus, boolean removableFocus, int index) {
        super.getMultiSelectItemRendererComponent(multiSelect, value, isPressed, hasFocus, removableFocus, index);
        Color color = getItemColor(value);
        setForeground(color == null ? multiSelect.getForeground() : color);
        return this;
    }

    @Override
    protected void paintOverflow(Graphics2D g2, int width, int height, int arc) {
        if (option.item instanceof Integer) {
            // create item color
            int value = (int) option.item;
            if (value == 1) {
                g2.setColor(getBackground(option.multiSelect.getSelectedItemAt(0)));
            } else if (value > 1) {
                // create gradient color
                Object startItem = option.multiSelect.getSelectedItemAt(0);
                Object endItem = option.multiSelect.getSelectedItemAt(value - 1);
                g2.setPaint(new GradientPaint(0, 0, getBackground(startItem), width, 0, getBackground(endItem)));
            }
        }
        super.paintOverflow(g2, width, height, arc);
    }

    @Override
    protected Color getBackground(Object item) {
        if (item instanceof ColorItem) {
            Color color = ((ColorItem) item).getBaseOnSystemMode();
            if (option.isPressed && !option.removableFocus) {
                return ColorFunctions.fade(color, 0.36f);
            } else if (option.hasFocus) {
                return ColorFunctions.fade(color, 0.28f);
            } else {
                return ColorFunctions.fade(color, 0.2f);
            }
        }
        return super.getBackground(item);
    }

    @Override
    protected Color getRemovableIconColor(boolean pressed, boolean focus) {
        if (option.item instanceof ColorItem) {
            Color color = ((ColorItem) option.item).getBaseOnSystemMode();
            if (pressed) {
                return color;
            } else {
                return ColorFunctions.fade(color, 0.8f);
            }
        }
        return super.getRemovableIconColor(pressed, focus);
    }

    private static class ColorCellRenderer extends MultiSelectCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Color color = getItemColor(value);
            setForeground(color == null ? list.getForeground() : color);
            return this;
        }
    }

    public static class ColorItem {

        public String getName() {
            return name;
        }

        public Color getLightColor() {
            return lightColor;
        }

        public Color getDarkColor() {
            return darkColor;
        }

        private final String name;
        private final Color lightColor;
        private final Color darkColor;

        public ColorItem(String name, String lightHex, String darkHex) {
            this.name = name;
            lightColor = Color.decode(lightHex);
            darkColor = Color.decode(darkHex);
        }

        public Color getBaseOnSystemMode() {
            return FlatLaf.isLafDark() ? darkColor : lightColor;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}