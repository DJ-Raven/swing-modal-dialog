package raven.modal.demo.utils.table;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckBoxTableHeaderRenderer extends JCheckBox implements TableCellRenderer {

    private final JTable table;
    private final int column;
    private final TableCellRenderer oldCellRenderer;

    public CheckBoxTableHeaderRenderer(JTable table, int column) {
        this.table = table;
        this.column = column;
        this.oldCellRenderer = table.getTableHeader().getDefaultRenderer();
        init();
    }

    private void init() {
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");
        setHorizontalAlignment(SwingConstants.CENTER);

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    int col = table.columnAtPoint(me.getPoint());
                    if (col == column) {
                        putClientProperty(FlatClientProperties.SELECTED_STATE, null);
                        setSelected(!isSelected());
                        selectedTableRow(isSelected());
                    }
                }
            }
        });

        table.getModel().addTableModelListener((tme) -> {
            if (tme.getColumn() == column || tme.getType() == TableModelEvent.DELETE) {
                checkRow();
            }
        });
    }

    private void checkRow() {
        boolean initValue = table.getRowCount() == 0 ? false : (boolean) table.getValueAt(0, column);
        for (int i = 1; i < table.getRowCount(); i++) {
            boolean v = (boolean) table.getValueAt(i, column);
            if (initValue != v) {
                putClientProperty(FlatClientProperties.SELECTED_STATE, FlatClientProperties.SELECTED_STATE_INDETERMINATE);
                table.getTableHeader().repaint();
                return;
            }
        }
        putClientProperty(FlatClientProperties.SELECTED_STATE, null);
        setSelected(initValue);
        table.getTableHeader().repaint();
    }

    private void selectedTableRow(boolean selected) {
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setValueAt(selected, i, column);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JComponent com = (JComponent) oldCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setBorder(com.getBorder());
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getBorder() != null) {
            getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }
}
