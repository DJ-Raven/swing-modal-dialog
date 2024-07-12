package raven.modal.demo.layout;

import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;

/**
 * @author Raven
 */
public class ResponsiveLayout implements LayoutManager {

    public JustifyContent getJustifyContent() {
        return justifyContent;
    }

    public void setJustifyContent(JustifyContent justifyContent) {
        this.justifyContent = justifyContent;
    }

    public Dimension getSize() {
        return new Dimension(size);
    }

    public void setSize(Dimension size) {
        this.size = new Dimension(size);
    }

    public int getHorizontalGap() {
        return horizontalGap;
    }

    public void setHorizontalGap(int horizontalGap) {
        this.horizontalGap = horizontalGap;
    }

    public int getVerticalGap() {
        return verticalGap;
    }

    public void setVerticalGap(int verticalGap) {
        this.verticalGap = verticalGap;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    private JustifyContent justifyContent;
    private Dimension size;
    private int horizontalGap;
    private int verticalGap;
    private int column;

    public ResponsiveLayout(JustifyContent justifyContent) {
        this(justifyContent, new Dimension(-1, -1));
    }

    public ResponsiveLayout(JustifyContent justifyContent, Dimension size) {
        this(justifyContent, size, 5, 5, -1);
    }

    public ResponsiveLayout(JustifyContent justifyContent, Dimension size, int horizontalGap, int verticalGap) {
        this(justifyContent, size, horizontalGap, verticalGap, -1);
    }

    public ResponsiveLayout(JustifyContent justifyContent, Dimension size, int horizontalGap, int verticalGap, int column) {
        this.justifyContent = justifyContent;
        this.size = size;
        this.horizontalGap = horizontalGap;
        this.verticalGap = verticalGap;
        this.column = column;
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
            Insets insets = parent.getInsets();
            int height = insets.top + insets.bottom;
            int width = (insets.left + insets.right);
            Dimension itemSize = getItemSize(parent);
            int count = getVisibleComponentCount(parent);
            int column = getColumn(parent, itemSize.width, count);
            if (count == 0) {
                return new Dimension(width, height);
            }
            int row = (int) Math.ceil((double) count / column);
            int hGap = UIScale.scale(horizontalGap);
            int vGap = UIScale.scale(verticalGap);
            width += column * itemSize.width + ((column - 1) * hGap);
            height += row * itemSize.height + ((row - 1) * vGap);
            if (SwingUtilities.getAncestorOfClass(JScrollPane.class, parent) != null) {
                width -= 1;
            }
            return new Dimension(width, height);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            // update if we require to use
            Insets insets = parent.getInsets();
            return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = parent.getWidth() - (insets.left + insets.right);
            if (width == 0) {
                return;
            }
            Dimension itemSize = getItemSize(parent);
            int count = parent.getComponentCount();
            int column = getColumn(parent, itemSize.width, count);
            LayoutOption layoutOption = getLayoutOption(itemSize, width, column);
            int startX = insets.left + layoutOption.startX;
            int startY = insets.top + layoutOption.startY;
            int x = startX;
            int y = startY;
            int c = 1;
            for (int i = 0; i < count; i++) {
                Component component = parent.getComponent(i);
                if (component.isVisible()) {
                    component.setBounds(x, y, layoutOption.itemSize.width, layoutOption.itemSize.height);
                    if (c == column) {
                        c = 1;
                        x = startX;
                        y += layoutOption.itemSize.height + layoutOption.vGap;
                    } else {
                        x += layoutOption.itemSize.width + layoutOption.hGap;
                        c++;
                    }
                }
            }
        }
    }

    private LayoutOption getLayoutOption(Dimension itemSize, int width, int column) {
        int h = UIScale.scale(horizontalGap);
        int v = UIScale.scale(verticalGap);
        int startX = 0;
        int startY = 0;
        int hGap = h;
        int vGap = v;
        if (justifyContent == JustifyContent.FIT_CONTENT) {
            itemSize.width = (width - ((column - 1) * hGap)) / column;
        } else if (justifyContent == JustifyContent.SPACE_BETWEEN) {
            if (column > 1) {
                hGap = Math.max((width - column * itemSize.width) / (column - 1), hGap);
            }
        } else if (justifyContent == JustifyContent.SPACE_AROUND) {
            hGap = Math.max((width - column * itemSize.width) / column, hGap);
            startX = (width - (itemSize.width * column + (column - 1) * hGap)) / 2;
        } else if (justifyContent == JustifyContent.SPACE_EVENLY) {
            hGap = Math.max((width - column * itemSize.width) / (column + 1), hGap);
            startX = (width - (itemSize.width * column + (column - 1) * hGap)) / 2;
        } else if (justifyContent == JustifyContent.CENTER) {
            startX = (width - (itemSize.width * column + (column - 1) * hGap)) / 2;
        } else if (justifyContent == JustifyContent.END) {
            startX = width - (itemSize.width * column + (column - 1) * hGap);
        }
        return new LayoutOption(startX, startY, hGap, vGap, itemSize);
    }

    private int getVisibleComponentCount(Container parent) {
        int count = 0;
        Component[] components = parent.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i].isVisible()) {
                count++;
            }
        }
        return count;
    }

    private Dimension getVisibleComponentMaxSize(Container parent) {
        int width = 0;
        int height = 0;
        Component[] components = parent.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i].isVisible()) {
                Dimension size = components[i].getPreferredSize();
                width = Math.max(height, size.width);
                height = Math.max(height, size.height);
            }
        }
        return new Dimension(width, height);
    }

    private Dimension getItemSize(Container parent) {
        if (size.width >= 0 && size.height >= 0) {
            return new Dimension(UIScale.scale(size));
        }
        Dimension itemSize = getVisibleComponentMaxSize(parent);
        if (size.width >= 0) {
            itemSize.width = UIScale.scale(size.width);
        }
        if (size.height >= 0) {
            itemSize.height = UIScale.scale(size.height);
        }
        return itemSize;
    }

    private int getColumn(Container parent, int itemWidth, int itemCount) {
        Insets insets = parent.getInsets();
        int width = parent.getWidth() - (insets.left + insets.right);
        int height = parent.getHeight() - (insets.top + insets.bottom);
        int hGap = UIScale.scale(horizontalGap);
        return Math.max(Math.min(calculateColumns(width, itemWidth, hGap), itemCount), 1);
    }

    public int calculateColumns(int width, int itemWidth, int gap) {
        if (column > 0) {
            return column;
        }
        return (width + gap) / (itemWidth + gap);
    }

    private static class LayoutOption {
        protected int startX;
        protected int startY;
        protected int hGap;
        protected int vGap;
        protected Dimension itemSize;

        public LayoutOption(int startX, int startY, int hGap, int vGap, Dimension itemSize) {
            this.startX = startX;
            this.startY = startY;
            this.hGap = hGap;
            this.vGap = vGap;
            this.itemSize = itemSize;
        }
    }

    /**
     * START: Items are positioned at the beginning of the container
     * END: Items are positioned at the end of the container
     * CENTER: Items are positioned in the center of the container
     * SPACE_BETWEEN: Items will have space between them
     * SPACE_AROUND: Items Start and end gaps are half the size of the space between then
     * SPACE_EVENLY: Items will have equal space around them
     * FIT_CONTENT: Items will full size of the container
     */
    public enum JustifyContent {
        START, END, CENTER, SPACE_BETWEEN, SPACE_AROUND, SPACE_EVENLY, FIT_CONTENT
    }
}
