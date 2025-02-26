package raven.modal.component;

import com.formdev.flatlaf.ui.FlatLineBorder;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class ModalLineBorder extends FlatLineBorder {

    public ModalLineBorder(int borderWidth, Color borderColor, int arc) {
        super(new Insets(borderWidth, borderWidth, borderWidth, borderWidth), borderColor, borderWidth, arc);
    }

    @Override
    public Color getLineColor() {
        Color lineColor = super.getLineColor();
        if (lineColor != null) {
            return lineColor;
        }
        Color color = UIManager.getColor("Component.borderColor");
        return color;
    }
}
