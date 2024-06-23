package raven.modal.toast;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.ColorFunctions;
import raven.modal.toast.option.ToastStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Raven
 */
class ToastContent extends JComponent {

    private ToastPanel.ToastData toastData;

    protected ToastContent(LayoutManager layout, ToastPanel.ToastData toastData) {
        this.toastData = toastData;
        setLayout(layout);
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Container parent = getParent();
        if (parent == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            if (toastData.getOption().getStyle().getBackgroundType() == ToastStyle.BackgroundType.GRADIENT) {
                ToastPanel.ThemesData themesData = toastData.getThemes();

                // gradient paint base on the parent component
                Rectangle rec = FlatUIUtils.addInsets(new Rectangle(0, 0, getWidth(), getHeight()), parent.getInsets());
                Color color = ColorFunctions.mix(Color.decode(FlatLaf.isLafDark() ? themesData.getColors()[1] : themesData.getColors()[0]), parent.getBackground(), 0.3f);
                g2.setPaint(new GradientPaint(rec.x, 0, color, (rec.x + rec.width) * 0.8f, 0, parent.getBackground()));
                g2.fill(new Rectangle2D.Float(0, 0, getWidth(), getHeight()));
            } else {
                g2.setColor(parent.getBackground());
                g2.fill(new Rectangle2D.Float(0, 0, getWidth(), getHeight()));
            }
        } finally {
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
