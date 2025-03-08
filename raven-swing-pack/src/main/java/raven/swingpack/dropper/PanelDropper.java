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
        setLayout(new MigLayout("novisualpadding,wrap,fillx,al center,gapy 10", "[fill]"));
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
                "width:6;" +
                "trackInsets:0,0,0,0;" +
                "thumbInsets:0,0,0,0;" +
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
                    FileDropperEvent event = new FileDropperEvent(i, i.file);
                    fileDropper.fireFileDropped(event);
                    dropFiles.remove(i);
                    if (event.getType() == FileDropperEvent.REJECT) {
                        panelItem.remove(i);
                        refresh();
                        continue;
                    }
                    if (event.getFileProgress() != null) {
                        i.progress(event.getFileProgress());
                    } else {
                        i.dropped();
                        fileDropper.getModel().add(i.file);
                    }
                }
            });
        }
    }

    protected synchronized void addFileAsDropped(File beforeOf, File[] files) {
        synchronized (panelItem.getTreeLock()) {
            if (files == null) return;
            int index = getIndexOf(beforeOf);
            for (File file : files) {
                if (isExist(file)) continue;
                Item item = new Item(file);
                item.createAndDrop();
                panelItem.add(item, index);
                index++;
            }
            movePrepared();
            refresh();
        }
    }

    protected void removeFile(File[] files) {
        if (files == null) return;
        for (File file : files) {
            removeFile(file);
        }
        refresh();
    }

    protected void removeFile(File file) {
        if (file == null) return;
        for (Component com : panelItem.getComponents()) {
            if (((Item) com).getFile() == file) {
                panelItem.remove(com);
                return;
            }
        }
    }

    protected void removeAllFile() {
        panelItem.removeAll();
        refresh();
    }

    protected void refresh() {
        panelItem.repaint();
        revalidate();
    }

    private int getIndexOf(File file) {
        if (file == null) {
            return panelItem.getComponentCount();
        }
        for (Component com : panelItem.getComponents()) {
            Item item = (Item) com;
            if (item.getType() == Type.COMPLETE && item.getFile() == file) {
                return panelItem.getComponentZOrder(com);
            }
        }
        return panelItem.getComponentCount();
    }

    private void movePrepared() {
        // move the prepared component to last index
        for (Component com : panelItem.getComponents()) {
            if (((Item) com).getType() == Type.PREPARE) {
                panelItem.setComponentZOrder(com, panelItem.getComponentCount() - 1);
            }
        }
    }

    protected boolean isExist(File file) {
        for (Component com : panelItem.getComponents()) {
            Item item = (Item) com;
            if (item.getFile() == file) {
                return true;
            }
        }
        return false;
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

    public JPanel getPanelItem() {
        return panelItem;
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
                if (action == -1) {
                    // file progress done
                    dropped();
                    fileDropper.getModel().add(getProgressIndex(), file);
                } else if (action == 0) {
                    // file action view
                    fileDropper.fireFileView(new FileDropperEvent(this, file));
                } else if (action == 1) {
                    // file action delete
                    FileDropperEvent event = new FileDropperEvent(this, file);
                    fileDropper.fireFileDelete(event);
                    if (event.getType() != FileDropperEvent.REJECT) {
                        fileDropper.getModel().removeAt(panelItem.getComponentZOrder(this));
                    }
                }
            });
            add(fileItem);
        }

        private int getProgressIndex() {
            synchronized (panelItem.getTreeLock()) {
                int index = fileDropper.getModel().getSize();
                for (int i = panelItem.getComponentZOrder(this) + 1; i < panelItem.getComponentCount(); i++) {
                    if (((Item) panelItem.getComponent(i)).type == Type.COMPLETE) {
                        index--;
                    }
                }
                return Math.min(index, fileDropper.getModel().getSize());
            }
        }

        private AvatarIcon createIcon() {
            Dimension iconSize = fileItem.getIconSize();
            return new AvatarIcon(fileDropper.getFileViewer().getFileViewer(file), UIScale.unscale(iconSize.width), UIScale.unscale(iconSize.height), 10);
        }

        protected void drop(Point dropLocation) {
            Dimension iconSize = fileItem.getIconSize();
            int x = dropLocation.x - iconSize.width / 2;
            int y = dropLocation.y - iconSize.height / 2;
            this.dropLocation = new Point(x, y);
            icon = createIcon();
            type = Type.DROP;
            dropFiles.add(this);
        }

        protected void createAndDrop() {
            icon = createIcon();
            dropped();
        }

        protected void progress(Consumer<FileProgress> fileProgress) {
            fileItem.progress(icon, fileProgress);
            type = Type.PROGRESS;
        }

        protected void dropped() {
            fileItem.dropped(type == Type.PROGRESS ? null : icon);
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
        PREPARE, DROP, PROGRESS, COMPLETE
    }
}
