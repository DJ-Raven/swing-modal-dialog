package raven.modal.demo.utils.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TableHeaderAlignment implements TableCellRenderer {

    private final TableCellRenderer headerDelegate;
    private final TableCellRenderer cellDelegate;

    public TableHeaderAlignment(JTable table) {
        this.headerDelegate = table.getTableHeader().getDefaultRenderer();
        this.cellDelegate = table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int row, int column) {
                JLabel label = (JLabel) cellDelegate.getTableCellRendererComponent(jtable, o, bln, bln1, row, column);
                label.setHorizontalAlignment(getAlignment(column));
                return label;
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) headerDelegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(getAlignment(column));
        return label;
    }

    protected int getAlignment(int column) {
        return SwingConstants.CENTER;
    }
}
