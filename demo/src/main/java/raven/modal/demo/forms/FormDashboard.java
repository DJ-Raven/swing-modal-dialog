package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.component.chart.SpiderChart;
import raven.modal.demo.component.chart.TimeSeriesChart;
import raven.modal.demo.component.chart.utils.ToolBarTimeSeriesChartRenderer;
import raven.modal.demo.component.dashboard.CardBox;
import raven.modal.demo.system.Form;
import raven.modal.demo.utils.SystemForm;

import javax.swing.*;
import java.awt.*;

@SystemForm(name = "Dashboard", description = "dashboard form display some details")
public class FormDashboard extends Form {

    public FormDashboard() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill", "[fill]", "[grow 0][fill]"));
        createTitle();
        createPanelLayout();
        createCard();
        createChart();
        createOtherChart();
    }

    @Override
    public void formInit() {
        loadData();
    }

    @Override
    public void formRefresh() {
        loadData();
    }

    private void loadData() {
        cardBox.setValueAt(0, "1,205", "+305 new registered", "+25%", true);
        cardBox.setValueAt(1, "$52,420.55", "less then previous month", "-5%", false);
        cardBox.setValueAt(2, "$3,180.00", "more then previous month", "+12%", true);
        cardBox.setValueAt(3, "$49,240.55", "more then previous month", "+7%", true);
    }

    private void createTitle() {
        JPanel panel = new JPanel(new MigLayout("fillx"));
        JLabel title = new JLabel("Dashboard");

        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +3");

        panel.add(title);
        add(panel);
    }

    private void createPanelLayout() {
        panelLayout = new JPanel(new DashboardLayout());
        JScrollPane scrollPane = new JScrollPane(panelLayout);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "width:5;" +
                "trackArc:$ScrollBar.thumbArc;" +
                "trackInsets:0,0,0,0;" +
                "thumbInsets:0,0,0,0;");
        add(scrollPane);
    }

    private void createCard() {
        JPanel panel = new JPanel(new MigLayout("fillx", "[fill]"));
        cardBox = new CardBox();
        cardBox.addCardItem(createIcon("raven/modal/demo/icons/dashboard/customer.svg", Color.decode("#0ea5e9")), "Total Customer");
        cardBox.addCardItem(createIcon("raven/modal/demo/icons/dashboard/income.svg", Color.decode("#10b981")), "Total Income");
        cardBox.addCardItem(createIcon("raven/modal/demo/icons/dashboard/expense.svg", Color.decode("#f43f5e")), "Total Expense");
        cardBox.addCardItem(createIcon("raven/modal/demo/icons/dashboard/profit.svg", Color.decode("#10b981")), "Last Profit");
        panel.add(cardBox);
        panelLayout.add(panel);
    }

    private void createChart() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx", "[fill]", "[350]"));
        TimeSeriesChart timeSeriesChart = new TimeSeriesChart();
        timeSeriesChart.add(new ToolBarTimeSeriesChartRenderer(timeSeriesChart), "al trailing,grow 0", 0);
        panel.add(timeSeriesChart);
        panelLayout.add(panel);
    }

    private void createOtherChart() {
        JPanel panel = new JPanel(new MigLayout("", "[350]", "[300]"));
        SpiderChart spiderChart = new SpiderChart();
        panel.add(spiderChart);
        panelLayout.add(panel);
    }

    private Icon createIcon(String icon, Color color) {
        return new FlatSVGIcon(icon, 0.4f).setColorFilter(new FlatSVGIcon.ColorFilter(color1 -> color));
    }

    private JPanel panelLayout;
    private CardBox cardBox;

    private class DashboardLayout implements LayoutManager {

        private int gap = 0;

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
                int width = (insets.left + insets.right);
                int height = insets.top + insets.bottom;
                int g = UIScale.scale(gap);
                int count = parent.getComponentCount();
                for (int i = 0; i < count; i++) {
                    Component com = parent.getComponent(i);
                    Dimension size = com.getPreferredSize();
                    height += size.height;
                }
                if (count > 1) {
                    height += (count - 1) * g;
                }
                return new Dimension(width, height);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(10, 10);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int g = UIScale.scale(gap);
                int count = parent.getComponentCount();
                for (int i = 0; i < count; i++) {
                    Component com = parent.getComponent(i);
                    Dimension size = com.getPreferredSize();
                    com.setBounds(x, y, width, size.height);
                    y += size.height + g;
                }
            }
        }
    }
}
