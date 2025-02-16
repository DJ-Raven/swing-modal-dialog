package raven.swingpack.dropper;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.UIScale;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.swingpack.dropper.event.FileDropperEvent;
import raven.swingpack.layout.ResponsiveLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Raven
 */
public class PanelDropper extends JPanel implements ActionListener {

    private Component dropPlaceholder;
    private final FileDropper fileDropper;
    private JScrollPane scrollPane;
    private JPanel panelItem;
    private final List<Item> dropFiles = new ArrayList<>();
    private ResponsiveLayout responsiveLayout;

    public PanelDropper(FileDropper fileDropper) {
        this.fileDropper = fileDropper;
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,al center,gapy 10", "[fill]"));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");
        responsiveLayout = new ResponsiveLayout(ResponsiveLayout.JustifyContent.START);
        panelItem = new JPanel(responsiveLayout) {
            @Override
            public Color getBackground() {
                return fileDropper.getBackground();
            }
        };
        setDropPlaceholder(createDefaultDropPlaceholder());

        scrollPane = createScroll();
        scrollPane.setViewportView(panelItem);
        add(scrollPane);
    }

    public void setDropPlaceholder(Component dropPlaceholder) {
        if (this.dropPlaceholder != null) {
            remove(this.dropPlaceholder);
            uninstallDropPlaceholderEvent(this.dropPlaceholder);
        }
        this.dropPlaceholder = dropPlaceholder;
        if (this.dropPlaceholder != null) {
            add(this.dropPlaceholder, 0);
            installDropPlaceholderEvent(this.dropPlaceholder);
        }
    }

    public Component getDropPlaceholder() {
        return dropPlaceholder;
    }

    private JScrollPane createScroll() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackInsets:0,3,0,3;" +
                "thumbInsets:0,3,0,3;" +
                "trackArc:$ScrollBar.thumbArc;");
        scrollPane.getVerticalScrollBar().setOpaque(false);
        return scrollPane;
    }

    private Component createDefaultDropPlaceholder() {
        return new DefaultFileDropPlaceholder();
    }

    private void installDropPlaceholderEvent(Component component) {
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            button.addActionListener(this);
        }
    }

    private void uninstallDropPlaceholderEvent(Component component) {
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            button.removeActionListener(this);
        }
    }

    protected boolean prepareFile(List<File> files) {
        boolean added = false;
        for (File file : files) {
            FileDropperEvent event = new FileDropperEvent(fileDropper, file);
            fileDropper.fireFileDragEnter(event);
            if (event.getType() != FileDropperEvent.REJECT) {
                Item item = new Item(file);
                panelItem.add(item);
                added = true;
            }
        }
        if (added) {
            refresh();

            // scroll to visible item
            int count = panelItem.getComponentCount();
            if (count > 0) {
                SwingUtilities.invokeLater(() -> {
                    panelItem.scrollRectToVisible(panelItem.getComponent(count - 1).getBounds());
                });
            }
        }
        return added;
    }

    protected void drop(Point location) {
        List<Item> list = new ArrayList<>();
        for (Component com : panelItem.getComponents()) {
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
                    panelItem.remove(i);
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
            panelItem.add(item);
        }
        refresh();
    }

    protected void removeFile(int index) {
        panelItem.remove(index);
    }

    protected void removeAllFile() {
        panelItem.removeAll();
        refresh();
    }

    protected void refresh() {
        panelItem.repaint();
        revalidate();
    }

    protected void exit() {
        for (Component com : panelItem.getComponents()) {
            if (com instanceof Item) {
                Item item = (Item) com;
                if (item.getType() == Type.PREPARE) {
                    panelItem.remove(com);
                }
            }
        }
        refresh();
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

    @Override
    public void actionPerformed(ActionEvent event) {
        // change event source to fileDropper
        ActionEvent newEvent = new ActionEvent(fileDropper, event.getID(), event.getActionCommand(), event.getWhen(), event.getModifiers());
        fileDropper.fireDropPlaceholderSelected(newEvent);
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
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "background:null;");
            setLayout(new BorderLayout());
            fileItem = new FileItem(action -> {
                if (action == 0) {
                    fileDropper.fireFileView(new FileDropperEvent(this, file));
                } else if (action == 1) {
                    FileDropperEvent event = new FileDropperEvent(this, file);
                    fileDropper.fireFileDelete(event);
                    if (event.getType() != FileDropperEvent.REJECT) {
                        fileDropper.getModel().removeAt(panelItem.getComponentZOrder(this));
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
            Rectangle rec = scrollPane.getViewport().getViewRect();
            float scrollX = scrollPane.getX() - rec.x;
            float scrollY = scrollPane.getY() - rec.y;
            float x = insets.left + dropLocation.x + (scrollX + getX() - dropLocation.x) * animate;
            float y = insets.top + dropLocation.y + (scrollY + getY() - dropLocation.y) * animate;
            if (rec.getY() > getY()) {
                g2.setClip(scrollPane.getBounds());
            }
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
