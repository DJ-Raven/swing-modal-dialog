package raven.modal.demo.component.swingpack;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.swingpack.JMultiSelectComboBox;
import raven.swingpack.multiselect.DefaultMultiSelectItemRenderer;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class IconMultiSelectItemRenderer extends DefaultMultiSelectItemRenderer {

    public static void apply(JMultiSelectComboBox<?> multiSelect) {
        multiSelect.setItemRenderer(new IconMultiSelectItemRenderer());
    }

    private IconMultiSelectItemRenderer() {
    }

    @Override
    public Component getMultiSelectItemRendererComponent(JMultiSelectComboBox<?> multiSelect, Object value, boolean isPressed, boolean hasFocus, boolean removableFocus, int index) {
        super.getMultiSelectItemRendererComponent(multiSelect, value, isPressed, hasFocus, removableFocus, index);
        if (value != null) {
            Icon icon = getItemIcon(value);
            setIcon(icon);
        } else {
            setIcon(null);
        }
        return this;
    }

    public Icon getItemIcon(Object item) {
        URL url = getClass().getResource("/raven/modal/demo/icons/mutiselect/" + item.toString().toLowerCase() + ".svg");
        if (url == null) {
            return null;
        }
        return new FlatSVGIcon(url).derive(16, 16);
    }
}