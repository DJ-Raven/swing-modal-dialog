package raven.swingpack.dropper;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.utils.CustomAnimation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;

/**
 * @author Raven
 */
public class FileItem extends JPanel {

    private Consumer<Integer> actionCallback;
    private JLayeredPane layeredPane;
    private final Dimension iconSize = new Dimension(100, 100);
    private FileAction fileAction;
    private UploadProgress uploadProgress;

    public FileItem(Consumer<Integer> actionCallback) {
        this.actionCallback = actionCallback;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 0", "[fill]", "[fill]"));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "border:5,5,5,5,,0,15;" +
                "[light]background:shade($Panel.background,5%);" +
                "[dark]background:tint($Panel.background,5%);");

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new ItemLayout(iconSize.width, iconSize.height));
        add(layeredPane);
    }

    public void dropped(AvatarIcon icon) {
        layeredPane.add(createFileAction());
        layeredPane.add(new JLabel(icon));
        repaint();
        revalidate();
    }

    public Dimension getIconSize() {
        return iconSize;
    }

    private FileAction createFileAction() {
        if (fileAction == null) {
            fileAction = new FileAction();
        }
        return fileAction;
    }

    private UploadProgress createUploadProgress() {
        if (uploadProgress == null) {
            uploadProgress = new UploadProgress();
        }
        return uploadProgress;
    }

    private class FileAction extends JComponent {

        private final Component[] comAction = new Component[2];
        private CustomAnimation animation;
        private float animate;

        private JLabel lbView;
        private JLabel lbDelete;

        public FileAction() {
            setLayout(new MigLayout("al center center"));
            MouseAdapter mouseEvent = new MouseAdapter() {

                private int pressedIndex = -1;

                @Override
                public void mousePressed(MouseEvent e) {
                    pressedIndex = getComponentIndex(e.getPoint());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    int index = getComponentIndex(e.getPoint());
                    if (index == pressedIndex) {
                        actionCallback.accept(index);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setShow(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setShow(false);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (getComponentIndex(e.getPoint()) >= 0) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            };
            addMouseListener(mouseEvent);
            addMouseMotionListener(mouseEvent);
            addDelete();
            addView();
        }

        private int getComponentIndex(Point point) {
            for (int i = 0; i < comAction.length; i++) {
                Component com = comAction[i];
                if (com != null) {
                    if (com.getBounds().contains(point)) {
                        return i;
                    }
                }
            }
            return -1;
        }

        protected void addView() {
            lbView = new JLabel(createIcon("view.svg"));
            add(lbView, 0);
            comAction[0] = lbView;
        }

        protected void addDelete() {
            lbDelete = new JLabel(createIcon("delete.svg"));
            add(lbDelete);
            comAction[1] = lbDelete;
        }

        private Icon createIcon(String icon) {
            return new FlatSVGIcon("raven/swingpack/icons/" + icon, 0.4f)
                    .setColorFilter(new FlatSVGIcon.ColorFilter(color -> Color.decode("#f5f4f4")));
        }

        protected void setShow(boolean show) {
            if (animation == null) {
                animation = new CustomAnimation(animate -> {
                    this.animate = animate;
                    repaint();
                });
            }
            animation.run(show);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            FlatUIUtils.setRenderingHints(g2);
            float arc = UIScale.scale(10);
            g2.setComposite(AlphaComposite.SrcOver.derive(0.5f * animate));
            g2.setColor(new Color(0, 0, 0));
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
            super.paintComponent(g);
        }

        @Override
        public void paint(Graphics g) {
            ((Graphics2D) g).setComposite(AlphaComposite.SrcOver.derive(animate));
            super.paint(g);
        }
    }

    private static class UploadProgress extends JComponent {

        public UploadProgress() {
            setLayout(new MigLayout("wrap,al center bottom", "[center]"));
            JProgressBar progressBar = new JProgressBar();
            progressBar.setValue(50);
            add(new JLabel("Uploading ..."));
            add(progressBar, "width 80%");
        }
    }

    private static class ItemLayout implements LayoutManager {

        private final int width;
        private final int height;

        public ItemLayout(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(UIScale.scale(width), UIScale.scale(height));
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                int width = parent.getWidth();
                int height = parent.getHeight();
                int count = parent.getComponentCount();
                for (int i = 0; i < count; i++) {
                    parent.getComponent(i).setBounds(0, 0, width, height);
                }
            }
        }
    }
}
