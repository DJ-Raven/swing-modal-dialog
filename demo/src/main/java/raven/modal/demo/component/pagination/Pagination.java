package raven.modal.demo.component.pagination;

import com.formdev.flatlaf.util.UIScale;
import raven.modal.demo.component.pagination.event.PaginationModelEvent;
import raven.modal.demo.component.pagination.event.PaginationModelListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Pagination extends JPanel implements PaginationModelListener {

    public static String VISUAL_PADDING_PROPERTY = "visualPadding";

    protected transient ChangeEvent changeEvent = null;
    private final CellRendererPane rendererPane;
    private PaginationModel paginationModel;
    private PaginationItemRenderer itemRenderer;
    private int maxItem;
    private boolean showNextAndPreviousButton = true;
    private boolean hideWhenNoPage = true;
    private boolean noVisualPadding;
    private Dimension itemSize = new Dimension(28, 28);
    private int itemGap = 7;

    private int focusIndex = -1;
    private int pressedIndex = -1;

    public Pagination() {
        this(7);
    }

    public Pagination(int maxItem) {
        this(maxItem, 0, 0);
    }

    public Pagination(int selectedPage, int pageSize) {
        this(7, selectedPage, pageSize);
    }

    public Pagination(int maxItem, int selectedPage, int pageSize) {
        this(new DefaultPaginationModel(maxItem, selectedPage, pageSize));
        this.maxItem = maxItem;
    }

    public Pagination(PaginationModel model) {
        setLayout(new BorderLayout());
        setModel(model);
        rendererPane = new CellRendererPane();
        itemRenderer = new DefaultPaginationItemRenderer();

        installListener();
    }

    private void installListener() {
        MouseAdapter mouseListener = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    setPressedIndex(getIndexAt(e.getX(), e.getY()));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int index = getIndexAt(e.getX(), e.getY());
                    boolean action = index != -1 && index == focusIndex;
                    setPressedIndex(-1);
                    setFocusIndex(index);
                    if (action) {
                        Page page = getPageAt(index);
                        if (page.getType() == Page.Type.PAGE || page.getType() == Page.Type.ELLIPSIS) {
                            setSelectedPage(page.getValue());
                        } else if (page.getType() == Page.Type.PREVIOUS) {
                            setSelectedPage(getSelectedPage() - 1);
                        } else if (page.getType() == Page.Type.NEXT) {
                            setSelectedPage(getSelectedPage() + 1);
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                setFocusIndex(getIndexAt(e.getX(), e.getY()));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setFocusIndex(-1);
            }

            private void setFocusIndex(int index) {
                if (focusIndex != index) {
                    focusIndex = index;
                    repaint();
                }
            }

            private void setPressedIndex(int index) {
                if (pressedIndex != index) {
                    pressedIndex = index;
                    repaint();
                }
            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
    }

    public PaginationModel getModel() {
        return paginationModel;
    }

    public void setModel(PaginationModel model) {
        PaginationModel oldValue = getModel();
        if (oldValue != null) {
            oldValue.removePaginationModelListener(this);
        }
        this.paginationModel = model;
        if (model != null) {
            model.addPaginationModelListener(this);
        }
        updatePaginationComponent();
        firePropertyChange("model", oldValue, model);
    }

    public PaginationItemRenderer getItemRenderer() {
        return itemRenderer;
    }

    public void setItemRenderer(PaginationItemRenderer itemRenderer) {
        this.itemRenderer = itemRenderer;
        repaint();
    }

    public boolean isShowNextAndPreviousButton() {
        return showNextAndPreviousButton;
    }

    public void setShowNextAndPreviousButton(boolean showNextAndPreviousButton) {
        if (this.showNextAndPreviousButton != showNextAndPreviousButton) {
            this.showNextAndPreviousButton = showNextAndPreviousButton;
            repaint();
            revalidate();
        }
    }

    public boolean isHideWhenNoPage() {
        return hideWhenNoPage;
    }

    public void setHideWhenNoPage(boolean hideWhenNoPage) {
        if (this.hideWhenNoPage != hideWhenNoPage) {
            this.hideWhenNoPage = hideWhenNoPage;
            repaint();
            revalidate();
        }
    }

    public boolean isNoVisualPadding() {
        return noVisualPadding;
    }

    public void setNoVisualPadding(boolean noVisualPadding) {
        if (this.noVisualPadding != noVisualPadding) {
            this.noVisualPadding = noVisualPadding;
            repaint();
            revalidate();
        }
    }

    public Dimension getItemSize() {
        return itemSize;
    }

    public void setItemSize(Dimension itemSize) {
        this.itemSize = itemSize;
        repaint();
        revalidate();
    }

    public int getItemGap() {
        return itemGap;
    }

    public void setItemGap(int itemGap) {
        if (this.itemGap != itemGap) {
            this.itemGap = itemGap;
            repaint();
            revalidate();
        }
    }

    public int getSelectedPage() {
        return getModel().getSelectedPage();
    }

    public void setSelectedPage(int selectedPage) {
        getModel().setSelectedPage(selectedPage);
    }

    public int getPageSize() {
        return getModel().getPageSize();
    }

    public void setPageSize(int pageSize) {
        getModel().setPageSize(pageSize);
    }

    public int getMaxItem() {
        return maxItem;
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    @Override
    public void paginationModelChanged(PaginationModelEvent event) {
        updatePaginationComponent();
        if (event.isPageChanged()) {
            fireStateChanged();
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();

        // update the page renderer when L&F changed
        if (itemRenderer != null && itemRenderer instanceof Component) {
            SwingUtilities.updateComponentTreeUI((Component) itemRenderer);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintImpl(g);
    }

    private void paintImpl(Graphics g) {
        Shape clip = g.getClip();
        Page[] pages = getModel().getPagination();
        int size = pages.length;

        boolean isCreateNextAndPrevious = checkCreateNextAndPreviousButton(size);
        int index = -1;
        if (isCreateNextAndPrevious) {
            // create previous button
            Page previousPage = new Page(-1, Page.Type.PREVIOUS);
            int pageIndex = ++index;
            Rectangle rec = rectangleAt(pageIndex);
            if (clip == null || clip.intersects(rec)) {
                paintItem(g, previousPage, pageIndex, rec);
            }
        }

        // create item page
        for (int i = 0; i < size; i++) {
            Page page = pages[i];
            int pageIndex = ++index;
            Rectangle rec = rectangleAt(pageIndex);
            if (clip == null || clip.intersects(rec)) {
                paintItem(g, page, pageIndex, rec);
            }
        }

        if (isCreateNextAndPrevious) {
            // create next button
            Page nextPage = new Page(1, Page.Type.NEXT);
            int pageIndex = ++index;
            Rectangle rec = rectangleAt(pageIndex);
            if (clip == null || clip.intersects(rec)) {
                paintItem(g, nextPage, pageIndex, rec);
            }
        }

        rendererPane.removeAll();
    }

    private void paintItem(Graphics g, Page page, int index, Rectangle rec) {
        boolean isSelected = page.getType() == Page.Type.PAGE && getSelectedPage() == page.getValue();
        Component c = itemRenderer.getPaginationItemRendererComponent(this, page, isSelected, index == pressedIndex, index == focusIndex, index);
        if (!noVisualPadding) {
            applyVisualPadding(c, rec);
        }
        rendererPane.paintComponent(g, c, this, rec);
    }

    private void applyVisualPadding(Component c, Rectangle rec) {
        if (c instanceof JComponent) {
            Object padding = ((JComponent) c).getClientProperty(VISUAL_PADDING_PROPERTY);
            if (padding instanceof Insets) {
                Insets insets = (Insets) padding;
                rec.x -= insets.left;
                rec.y -= insets.top;
                rec.width += (insets.left + insets.right);
                rec.height += (insets.top + insets.bottom);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        return paginationPreferredSize();
    }

    private Dimension paginationPreferredSize() {
        Insets insets = getInsets();
        int width = insets.left + insets.right;
        int height = insets.top + insets.bottom;
        int size = getModel().getPagination().length;
        if (size > 0) {
            int totalGap = (size - 1) * scale(itemGap);
            width += (scale(itemSize.width) * size) + totalGap;
            height += (scale(itemSize.height));
        }

        if (checkCreateNextAndPreviousButton(size)) {
            int addGap = 2;
            if (size == 0) {
                height += scale(itemSize.height);
                addGap = 1;
            }
            width += scale((itemSize.width * 2) + (itemGap * addGap));
        }
        return new Dimension(width, height);
    }

    private Rectangle rectangleAt(int index) {
        Insets insets = getInsets();
        int width = scale(itemSize.width);
        int height = scale(itemSize.height);
        int gap = scale(itemGap);
        int x = insets.left + (width * index) + (gap * index);
        int y = insets.top;
        return new Rectangle(x, y, width, height);
    }

    private int getIndexAt(int x, int y) {
        Insets insets = getInsets();
        int index = (x - insets.left) / (scale(itemSize.width + itemGap));
        if (rectangleAt(index).contains(x, y)) {
            return index;
        }
        return -1;
    }

    private Page getPageAt(int index) {
        Page[] pages = getModel().getPagination();
        int size = pages.length;
        if (checkCreateNextAndPreviousButton(size)) {
            size += 2;
            if (index == 0) {
                return new Page(-1, Page.Type.PREVIOUS);
            } else if (index == size - 1) {
                return new Page(1, Page.Type.NEXT);
            }
            return pages[index - 1];
        } else {
            return pages[index];
        }
    }

    private boolean checkCreateNextAndPreviousButton(int size) {
        return isShowNextAndPreviousButton() && (size > 0 || !isHideWhenNoPage());
    }

    private void updatePaginationComponent() {
        repaint();
    }

    protected int scale(int value) {
        return UIScale.scale(value);
    }
}
