package raven.modal.demo.themes;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ListCellTitledBorder implements Border {

    private final JList list;
    private final String title;

    ListCellTitledBorder(JList list, String title) {
        this.list = list;
        this.title = title;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        FontMetrics fm = c.getFontMetrics(list.getFont());
        int titleWidth = fm.stringWidth(title);
        int titleHeight = fm.getHeight();

        // fill background
        g.setColor(list.getBackground());
        g.fillRect(x, y, width, titleHeight);

        int gap = UIScale.scale(4);
        Graphics2D g2 = (Graphics2D) g.create();

        try {
            FlatUIUtils.setRenderingHints(g2);
            g2.setColor(UIManager.getColor("Label.disabledForeground"));

            // paint separator line
            int sepWidth = (width - titleWidth) / 2 - (gap * 2);
            if (sepWidth > 0) {
                int sy = y + Math.round(titleHeight / 2f);
                float sepHeight = UIScale.scale(1f);

                g2.fill(new Rectangle2D.Float(x + gap, sy, sepWidth, sepHeight));
                g2.fill(new Rectangle2D.Float(x + width - gap - sepWidth, sy, sepWidth, sepHeight));
            }

            // draw title
            int xt = x + ((width - titleWidth) / 2);
            int yt = y + fm.getAscent();

            FlatUIUtils.drawString(list, g2, title, xt, yt);
        } finally {
            g2.dispose();
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int height = c.getFontMetrics(list.getFont()).getHeight();
        return new Insets(height, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}