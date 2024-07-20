package raven.modal.demo.component;

import com.formdev.flatlaf.icons.FlatAbstractIcon;
import raven.extras.AvatarIcon;

import java.awt.*;

public class AvatarBorderColorIcon extends FlatAbstractIcon {

    private AvatarIcon.BorderColor borderColor;

    public AvatarBorderColorIcon(AvatarIcon.BorderColor borderColor) {
        super(16, 16, null);
        this.borderColor = borderColor;
    }

    @Override
    protected void paintIcon(Component component, Graphics2D g) {
        borderColor.paint(g, 1, 1, width - 2, height - 2);
        g.fillRoundRect(1, 1, width - 2, height - 2, 5, 5);
        g.dispose();
    }
}
