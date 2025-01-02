package raven.modal.demo.forms;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
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
        setLayout(new MigLayout("wrap,fillx", "[fill]"));
        createTitle();
        createPanelLayout();
        createCard();
        createChart();
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
        panelLayout = new JPanel(new MigLayout("insets 0,wrap,fillx", "[fill]"));
        add(panelLayout);
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
        timeSeriesChart.add(createChartRendererOption(timeSeriesChart), 0);
        panel.add(timeSeriesChart);
        panelLayout.add(panel);
    }

    private JComponent createChartRendererOption(TimeSeriesChart chart) {
        JPanel panel = new JPanel(new MigLayout("insets 3,al trailing"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");
        panel.add(new ToolBarTimeSeriesChartRenderer(chart));
        return panel;
    }

    private Icon createIcon(String icon, Color color) {
        return new FlatSVGIcon(icon, 0.4f).setColorFilter(new FlatSVGIcon.ColorFilter(color1 -> color));
    }

    private JPanel panelLayout;
    private CardBox cardBox;
}
