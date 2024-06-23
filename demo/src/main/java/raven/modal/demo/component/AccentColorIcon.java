package raven.modal.demo.component;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.icons.FlatAbstractIcon;
import com.formdev.flatlaf.util.ColorFunctions;

import javax.swing.*;
import java.awt.*;

public class AccentColorIcon extends FlatAbstractIcon {

    private final String colorKey;

    public AccentColorIcon(String colorKey) {
        super(16, 16, null);
        this.colorKey = colorKey;
    }

    @Override
    protected void paintIcon(Component c, Graphics2D g) {
        Color color = UIManager.getColor(colorKey);
        if (color == null)
            color = Color.lightGray;
        else if (!c.isEnabled()) {
            color = FlatLaf.isLafDark()
                    ? ColorFunctions.shade(color, 0.5f)
                    : ColorFunctions.tint(color, 0.6f);
        }

        g.setColor(color);
        g.fillRoundRect(1, 1, width - 2, height - 2, 5, 5);
        g.dispose();
    }
}
