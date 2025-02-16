package raven.swingpack.dropper;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.UIScale;
import raven.extras.AvatarIcon;
import raven.swingpack.dropper.event.FileDropperEvent;
import raven.swingpack.layout.ResponsiveLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Raven
 */
public class PanelDropper extends JPanel {

    private final FileDropper fileDropper;
    private final List<Item> dropFiles = new ArrayList<>();
    private ResponsiveLayout responsiveLayout;

    public PanelDropper(FileDropper fileDropper) {
        this.fileDropper = fileDropper;
        init();
    }

    private void init() {
        responsiveLayout = new ResponsiveLayout(ResponsiveLayout.JustifyContent.START);
        setLayout(responsiveLayout);
    }

    protected void prepareFile(List<File> files) {
        boolean added = false;
        for (File file : files) {
            Item item = new Item(file);
            add(item);
            added = true;
        }
        if (added) {
            repaint();
            revalidate();

            // scroll to visible item
            int count = getComponentCount();
            if (count > 0) {
                SwingUtilities.invokeLater(() -> {
                    scrollRectToVisible(getComponent(count - 1).getBounds());
                });
            }
        }
    }

    protected void drop(Point location) {
        List<Item> list = new ArrayList<>();
        for (Component com : getComponents()) {
            if (com instanceof Item) {
                Item item = (Item) com;
                if (item.getType() == Type.PREPARE) {
                    list.add(item);
                }
            }
        }
        if (!list.isEmpty()) {
            Item[] items = new Item[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Item item = list.get(i);
                item.drop(location);
                items[i] = item;
            }
            AnimationItem.drop(this, items, object -> {
                for (Item i : object) {
                    i.dropped();
                    FileDropperEvent event = new FileDropperEvent(i, i.file);
                    fileDropper.fireFileDropped(event);
                    remove(i);
                    dropFiles.remove(i);
                    refresh();
                    if (event.getType() != FileDropperEvent.REJECT) {
                        fileDropper.getModel().add(i.file);
                    }
                }
            });
        }
    }

    protected void addFileAsDropped(File... files) {
        for (File file : files) {
            Item item = new Item(file);
            item.createAndDrop();
            add(item);
        }
        repaint();
        revalidate();
    }

    protected void removeFile(int index) {
        remove(index);
    }

    protected void refresh() {
        repaint();
        revalidate();
    }

    protected void exit() {
        for (Component com : getComponents()) {
            if (com instanceof Item) {
                Item item = (Item) com;
                if (item.getType() == Type.PREPARE) {
                    remove(com);
                }
            }
        }
        repaint();
        revalidate();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintDropFile(g);
    }

    private void paintDropFile(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        FlatUIUtils.setRenderingHints(g2);
        for (int i = 0; i < dropFiles.size(); i++) {
            Item item = dropFiles.get(i);
            if (item != null) {
                item.paintIcon(g2);
            }
        }
        g2.dispose();
    }

    public class Item extends JPanel {

        private final File file;
        private Type type = Type.PREPARE;
        private FileItem fileItem;
        private Point dropLocation;
        private float animate;
        private AvatarIcon icon;

        public Item(File file) {
            this.file = file;
            setLayout(new BorderLayout());
            fileItem = new FileItem(action -> {
                if (action == 0) {
                    fileDropper.fireFileView(new FileDropperEvent(this, file));
                } else if (action == 1) {
                    FileDropperEvent event = new FileDropperEvent(this, file);
                    fileDropper.fireFileDelete(event);
                    if (event.getType() != FileDropperEvent.REJECT) {
                        fileDropper.getModel().removeAt(PanelDropper.this.getComponentZOrder(this));
                    }
                }
            });
            add(fileItem);
        }

        protected void drop(Point dropLocation) {
            Dimension iconSize = fileItem.getIconSize();
            int x = dropLocation.x - iconSize.width / 2;
            int y = dropLocation.y - iconSize.height / 2;
            this.dropLocation = new Point(x, y);
            icon = new AvatarIcon(file.getAbsolutePath(), UIScale.unscale(iconSize.width), UIScale.unscale(iconSize.height), 10);
            type = Type.DROP;
            dropFiles.add(this);
        }

        protected void createAndDrop() {
            Dimension iconSize = fileItem.getIconSize();
            icon = new AvatarIcon(file.getAbsolutePath(), UIScale.unscale(iconSize.width), UIScale.unscale(iconSize.height), 10);
            dropped();
        }

        protected void dropped() {
            fileItem.dropped(icon);
            type = Type.COMPLETE;
        }

        protected void paintIcon(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Insets insets = fileItem.getInsets();
            float x = insets.left + dropLocation.x + (getX() - dropLocation.x) * animate;
            float y = insets.top + dropLocation.y + (getY() - dropLocation.y) * animate;
            g2.translate(x, y);
            icon.paintIcon(PanelDropper.this, g2, 0, 0);
            g2.dispose();
        }

        protected Type getType() {
            return type;
        }

        public File getFile() {
            return file;
        }
    }

    protected static class AnimationItem {
        private final Component parent;
        private final Item[] items;
        private final Consumer<Item[]> end;
        private Animator animator;

        protected AnimationItem(Component parent, Item[] items, Consumer<Item[]> end) {
            this.parent = parent;
            this.items = items;
            this.end = end;
        }

        protected void start() {
            animator = new Animator(350, new Animator.TimingTarget() {
                @Override
                public void timingEvent(float fraction) {
                    for (Item item : items) {
                        item.animate = fraction;
                    }
                    parent.repaint();
                }

                @Override
                public void end() {
                    end.accept(items);
                }
            });
            animator.setInterpolator(CubicBezierEasing.STANDARD_EASING);
            animator.start();
        }

        protected static void drop(Component parent, Item[] items, Consumer<Item[]> end) {
            new AnimationItem(parent, items, end).start();
        }
    }

    protected enum Type {
        PREPARE, DROP, COMPLETE
    }
}
