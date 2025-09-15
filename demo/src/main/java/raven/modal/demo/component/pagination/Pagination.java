package raven.modal.demo.component.pagination;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.component.pagination.event.PaginationModelEvent;
import raven.modal.demo.component.pagination.event.PaginationModelListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class Pagination extends JComponent implements PaginationModelListener {

    private PaginationModel paginationModel;
    protected transient ChangeEvent changeEvent = null;
    private int maxItem;

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

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    public void fireActionPerformed(ActionEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(event);
            }
        }
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

    private void updatePaginationComponent() {
        removeAll();
        add(createPagination());
        repaint();
        revalidate();
    }

    protected Component createPagination() {
        JPanel panel = new JPanel(new MigLayout("", "[fill,sg]", "[fill]"));
        panel.setOpaque(false);
        // create previous
        Component previous = createPreviousComponent();
        if (previous != null) {
            if (!getModel().hasPrevious()) {
                previous.setEnabled(false);
            }
            panel.add(previous);
        }

        // create page item
        Page[] pages = getModel().getPagination();
        for (Page page : pages) {
            panel.add(createPageItemComponent(page));
        }

        // create next
        Component next = createNextComponent();
        if (next != null) {
            if (!getModel().hasNext()) {
                next.setEnabled(false);
            }
            panel.add(next);
        }
        return panel;
    }

    protected Component createPageItemComponent(Page page) {
        PageItem pageItem = new PageItem(page);
        pageItem.setFocusable(false);
        installPageItemListeners(pageItem);
        installPageItemStyle(pageItem, page);
        return pageItem;
    }

    protected Component createPreviousComponent() {
        JButton button = new JButton();
        button.setFocusable(false);
        button.setIcon(new FlatSVGIcon("raven/modal/demo/icons/pagination/back.svg", 0.25f)
                .setColorFilter(new FlatSVGIcon.ColorFilter(color -> button.getForeground())));
        installPreviousButtonListeners(button);
        installPreviousButtonStyle(button);
        return button;
    }

    protected Component createNextComponent() {
        JButton button = new JButton();
        button.setFocusable(false);
        button.setIcon(new FlatSVGIcon("raven/modal/demo/icons/pagination/next.svg", 0.25f)
                .setColorFilter(new FlatSVGIcon.ColorFilter(color -> button.getForeground())));
        installNextButtonListeners(button);
        installNextButtonStyle(button);
        return button;
    }

    protected void installPageItemListeners(PageItem item) {
        item.addActionListener(e -> {
            setSelectedPage(item.getPage().getValue());
            fireActionPerformed(e);
        });
    }

    protected void installPreviousButtonListeners(JButton button) {
        button.addActionListener(e -> {
            setSelectedPage(getSelectedPage() - 1);
            fireActionPerformed(e);
        });
    }

    protected void installNextButtonListeners(JButton button) {
        button.addActionListener(e -> {
            setSelectedPage(getSelectedPage() + 1);
            fireActionPerformed(e);
        });
    }

    protected void installPageItemStyle(JButton button, Page page) {
        boolean selected = page.getValue() == getSelectedPage();
        button.putClientProperty(FlatClientProperties.STYLE_CLASS, selected ? "pageItemSelected" : "pageItem");
    }

    protected void installPreviousButtonStyle(JButton button) {
        button.putClientProperty(FlatClientProperties.STYLE_CLASS, "pagePrevious");
    }

    protected void installNextButtonStyle(JButton button) {
        button.putClientProperty(FlatClientProperties.STYLE_CLASS, "pageNext");
    }

    @Override
    public void paginationModelChanged(PaginationModelEvent event) {
        updatePaginationComponent();
        if (event.isPageChanged()) {
            fireStateChanged();
        }
    }

    public static class PageItem extends JButton {

        private final NumberFormat format = NumberFormat.getNumberInstance();
        private Page page;

        public PageItem(Page page) {
            setPage(page);
        }

        public Page getPage() {
            return page;
        }

        public void setPage(Page page) {
            this.page = page;
            if (page.getType() == Page.Type.PAGE) {
                setText(format.format(page.getValue()));
            } else {
                setText("...");
            }
        }
    }
}
