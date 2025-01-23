package raven.modal.demo.sample;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.TableXYDataset;
import raven.extras.AvatarIcon;
import raven.modal.demo.model.ModelEmployee;
import raven.modal.demo.model.ModelProfile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SampleData {

    public static List<ModelEmployee> getSampleEmployeeData(boolean defaultIcon) {
        List<ModelEmployee> list = new ArrayList<>();
        list.add(new ModelEmployee("20-August-2024", 1750, "Business Analyst", "Analytical thinker with experience in business process improvement.", new ModelProfile(getProfileIcon("profile_1.jpg", defaultIcon), "Hannah Scott", "Washington, D.C.")));
        list.add(new ModelEmployee("15-May-2024", 1200, "Marketing Manager", "Experienced marketing professional with a focus on digital advertising.", new ModelProfile(getProfileIcon("profile_2.jpg", defaultIcon), "Samantha Smith", "New York City")));
        list.add(new ModelEmployee("20-May-2024", 1500, "Software Engineer", "Skilled developer proficient in Java, Python, and JavaScript.", new ModelProfile(getProfileIcon("profile_3.jpg", defaultIcon), "John Johnson", "Los Angeles")));
        list.add(new ModelEmployee("25-May-2024", 1300, "Graphic Designer", "Creative designer with expertise in Adobe Creative Suite.", new ModelProfile(getProfileIcon("profile_4.jpg", defaultIcon), "Emily Brown", "Chicago")));
        list.add(new ModelEmployee("30-May-2024", 1800, "Financial Analyst", "Analytical thinker with a background in financial modeling and forecasting.", new ModelProfile(getProfileIcon("profile_5.jpg", defaultIcon), "Michael Davis", "San Francisco")));
        list.add(new ModelEmployee("15-August-2024", 1450, "Financial Planner", "Certified financial planner with a client-centered approach.", new ModelProfile(getProfileIcon("profile_6.jpg", defaultIcon), "Justin White", "San Diego")));
        list.add(new ModelEmployee("10-June-2024", 1700, "Sales Representative", "Proven track record in sales and client relationship management.", new ModelProfile(getProfileIcon("profile_7.jpg", defaultIcon), "David Martinez", "Miami")));
        list.add(new ModelEmployee("30-June-2024", 1900, "Project Manager", "Organized leader skilled in managing cross-functional teams.", new ModelProfile(getProfileIcon("profile_8.jpg", defaultIcon), "Ryan Anderson", "Portland")));
        list.add(new ModelEmployee("20-June-2024", 1550, "UX/UI Designer", "Design thinker focused on creating intuitive user experiences.", new ModelProfile(getProfileIcon("profile_9.jpg", defaultIcon), "Daniel Wilson", "Austin")));
        return list;
    }

    public static List<ModelEmployee> getSampleBasicEmployeeData() {
        List<ModelEmployee> list = new ArrayList<>();
        list.add(new ModelEmployee("20-August-2024", 1750, "Business Analyst", "Analytical thinker with experience in business process improvement.", new ModelProfile(null, "Hannah Scott", "Washington, D.C.")));
        list.add(new ModelEmployee("15-May-2024", 1200, "Marketing Manager", "Experienced marketing professional with a focus on digital advertising.", new ModelProfile(null, "Samantha Smith", "New York City")));
        list.add(new ModelEmployee("20-May-2024", 1500, "Software Engineer", "Skilled developer proficient in Java, Python, and JavaScript.", new ModelProfile(null, "John Johnson", "Los Angeles")));
        list.add(new ModelEmployee("25-May-2024", 1300, "Graphic Designer", "Creative designer with expertise in Adobe Creative Suite.", new ModelProfile(null, "Emily Brown", "Chicago")));
        list.add(new ModelEmployee("30-May-2024", 1800, "Financial Analyst", "Analytical thinker with a background in financial modeling and forecasting.", new ModelProfile(null, "Michael Davis", "San Francisco")));
        list.add(new ModelEmployee("5-June-2024", 1600, "HR Manager", "Human resources professional specializing in recruitment and employee relations.", new ModelProfile(null, "Jessica Miller", "Seattle")));
        list.add(new ModelEmployee("10-June-2024", 1700, "Sales Representative", "Proven track record in sales and client relationship management.", new ModelProfile(null, "David Martinez", "Miami")));
        list.add(new ModelEmployee("15-June-2024", 1400, "Content Writer", "Versatile writer capable of producing engaging content across various platforms.", new ModelProfile(null, "Sarah Thompson", "Boston")));
        list.add(new ModelEmployee("20-June-2024", 1550, "UX/UI Designer", "Design thinker focused on creating intuitive user experiences.", new ModelProfile(null, "Daniel Wilson", "Austin")));
        list.add(new ModelEmployee("25-June-2024", 1350, "Accountant", "Detail-oriented accountant with expertise in financial reporting.", new ModelProfile(null, "Rachel Taylor", "Denver")));
        list.add(new ModelEmployee("30-June-2024", 1900, "Project Manager", "Organized leader skilled in managing cross-functional teams.", new ModelProfile(null, "Ryan Anderson", "Portland")));
        list.add(new ModelEmployee("5-July-2024", 1750, "Marketing Coordinator", "Marketing professional with experience in campaign management and analysis.", new ModelProfile(null, "Lauren Hernandez", "Phoenix")));
        list.add(new ModelEmployee("10-July-2024", 1650, "Software Developer", "Full-stack developer proficient in front-end and back-end technologies.", new ModelProfile(null, "Kevin Garcia", "Atlanta")));
        list.add(new ModelEmployee("15-July-2024", 1300, "Customer Service Representative", "Dedicated customer service professional committed to resolving issues.", new ModelProfile(null, "Amanda Martinez", "Houston")));
        list.add(new ModelEmployee("20-July-2024", 1600, "Data Analyst", "Analytical thinker with expertise in data visualization and statistical analysis.", new ModelProfile(null, "Erica Robinson", "Philadelphia")));
        list.add(new ModelEmployee("25-July-2024", 1850, "Operations Manager", "Efficient manager with experience in optimizing operational processes.", new ModelProfile(null, "Matthew Walker", "Dallas")));
        list.add(new ModelEmployee("30-July-2024", 1400, "Social Media Manager", "Strategic thinker with a passion for creating engaging social media content.", new ModelProfile(null, "Olivia Lewis", "Detroit")));
        list.add(new ModelEmployee("5-August-2024", 1700, "Web Developer", "Skilled web developer with expertise in HTML, CSS, and JavaScript frameworks.", new ModelProfile(null, "Nathan King", "Minneapolis")));
        list.add(new ModelEmployee("10-August-2024", 1550, "Digital Marketing Specialist", "Experienced marketer focused on digital advertising and SEO strategies.", new ModelProfile(null, "Maria Perez", "Orlando")));
        list.add(new ModelEmployee("15-August-2024", 1450, "Financial Planner", "Certified financial planner with a client-centered approach.", new ModelProfile(null, "Justin White", "San Diego")));
        return list;
    }

    public static TableXYDataset getTimeSeriesDataset() {
        TimeTableXYDataset dataset = new TimeTableXYDataset();
        String seriesIncome = "Income";

        dataset.add(new Month(2, 2001), 181.8, seriesIncome);
        dataset.add(new Month(3, 2001), 167.3, seriesIncome);
        dataset.add(new Month(4, 2001), 153.8, seriesIncome);
        dataset.add(new Month(5, 2001), 167.6, seriesIncome);
        dataset.add(new Month(6, 2001), 158.8, seriesIncome);
        dataset.add(new Month(7, 2001), 148.3, seriesIncome);
        dataset.add(new Month(8, 2001), 153.9, seriesIncome);
        dataset.add(new Month(9, 2001), 142.7, seriesIncome);
        dataset.add(new Month(10, 2001), 123.2, seriesIncome);
        dataset.add(new Month(11, 2001), 131.8, seriesIncome);
        dataset.add(new Month(12, 2001), 139.6, seriesIncome);
        dataset.add(new Month(1, 2002), 142.9, seriesIncome);
        dataset.add(new Month(2, 2002), 138.7, seriesIncome);
        dataset.add(new Month(3, 2002), 137.3, seriesIncome);
        dataset.add(new Month(4, 2002), 143.9, seriesIncome);
        dataset.add(new Month(5, 2002), 139.8, seriesIncome);
        dataset.add(new Month(6, 2002), 80.0, seriesIncome);
        dataset.add(new Month(7, 2002), 50.8, seriesIncome);

        String seriesExpense = "Expense";
        dataset.add(new Month(2, 2001), 129.6, seriesExpense);
        dataset.add(new Month(3, 2001), 123.2, seriesExpense);
        dataset.add(new Month(4, 2001), 117.2, seriesExpense);
        dataset.add(new Month(5, 2001), 124.1, seriesExpense);
        dataset.add(new Month(6, 2001), 122.6, seriesExpense);
        dataset.add(new Month(7, 2001), 119.2, seriesExpense);
        dataset.add(new Month(8, 2001), 116.5, seriesExpense);
        dataset.add(new Month(9, 2001), 112.7, seriesExpense);
        dataset.add(new Month(10, 2001), 101.5, seriesExpense);
        dataset.add(new Month(11, 2001), 106.1, seriesExpense);
        dataset.add(new Month(12, 2001), 125.2, seriesExpense);
        dataset.add(new Month(1, 2002), 111.7, seriesExpense);
        dataset.add(new Month(2, 2002), 111.0, seriesExpense);
        dataset.add(new Month(3, 2002), 109.6, seriesExpense);
        dataset.add(new Month(4, 2002), 113.2, seriesExpense);
        dataset.add(new Month(5, 2002), 111.6, seriesExpense);
        dataset.add(new Month(6, 2002), 108.8, seriesExpense);
        dataset.add(new Month(7, 2002), 101.6, seriesExpense);

        return dataset;
    }

    public static CategoryDataset getCategoryDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // series key
        String series1 = "Sales";
        String series2 = "Adjust";
        String series3 = "Return";
        String series4 = "Custom";

        // product names
        String product1 = "Laptop";
        String product2 = "Phone";
        String product3 = "Accessory";

        // product 1
        dataset.addValue(200, product1, series1);
        dataset.addValue(50, product1, series2);
        dataset.addValue(80, product1, series3);
        dataset.addValue(150, product1, series4);

        // product 2
        dataset.addValue(50, product2, series1);
        dataset.addValue(180, product2, series2);
        dataset.addValue(250, product2, series3);
        dataset.addValue(230, product2, series4);

        // product 3
        dataset.addValue(180, product3, series1);
        dataset.addValue(100, product3, series2);
        dataset.addValue(250, product3, series3);
        dataset.addValue(80, product3, series4);

        return dataset;
    }

    public static PieDataset getPieDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        dataset.setValue("Laptop", 30);
        dataset.setValue("Phone", 25);
        dataset.setValue("Tablet", 18);
        dataset.setValue("Watch", 12);

        return dataset;
    }

    public static OHLCDataset getOhlcDataset() {
        Date[] date = new Date[47];
        double[] high = new double[47];
        double[] low = new double[47];
        double[] open = new double[47];
        double[] close = new double[47];
        double[] volume = new double[47];
        int jan = 1;
        int feb = 2;
        date[0] = createOhlcData(2001, jan, 4, 12, 0);
        high[0] = 47.0;
        low[0] = 33.0;
        open[0] = 35.0;
        close[0] = 33.0;
        volume[0] = 100.0;
        date[1] = createOhlcData(2001, jan, 5, 12, 0);
        high[1] = 47.0;
        low[1] = 32.0;
        open[1] = 41.0;
        close[1] = 37.0;
        volume[1] = 150.0;
        date[2] = createOhlcData(2001, jan, 6, 12, 0);
        high[2] = 49.0;
        low[2] = 43.0;
        open[2] = 46.0;
        close[2] = 48.0;
        volume[2] = 70.0;
        date[3] = createOhlcData(2001, jan, 7, 12, 0);
        high[3] = 51.0;
        low[3] = 39.0;
        open[3] = 40.0;
        close[3] = 47.0;
        volume[3] = 200.0;
        date[4] = createOhlcData(2001, jan, 8, 12, 0);
        high[4] = 60.0;
        low[4] = 40.0;
        open[4] = 46.0;
        close[4] = 53.0;
        volume[4] = 120.0;
        date[5] = createOhlcData(2001, jan, 9, 12, 0);
        high[5] = 62.0;
        low[5] = 55.0;
        open[5] = 57.0;
        close[5] = 61.0;
        volume[5] = 110.0;
        date[6] = createOhlcData(2001, jan, 10, 12, 0);
        high[6] = 65.0;
        low[6] = 56.0;
        open[6] = 62.0;
        close[6] = 59.0;
        volume[6] = 70.0;
        date[7] = createOhlcData(2001, jan, 11, 12, 0);
        high[7] = 55.0;
        low[7] = 43.0;
        open[7] = 45.0;
        close[7] = 47.0;
        volume[7] = 20.0;
        date[8] = createOhlcData(2001, jan, 12, 12, 0);
        high[8] = 54.0;
        low[8] = 33.0;
        open[8] = 40.0;
        close[8] = 51.0;
        volume[8] = 30.0;
        date[9] = createOhlcData(2001, jan, 13, 12, 0);
        high[9] = 47.0;
        low[9] = 33.0;
        open[9] = 35.0;
        close[9] = 33.0;
        volume[9] = 100.0;
        date[10] = createOhlcData(2001, jan, 14, 12, 0);
        high[10] = 54.0;
        low[10] = 38.0;
        open[10] = 43.0;
        close[10] = 52.0;
        volume[10] = 50.0;
        date[11] = createOhlcData(2001, jan, 15, 12, 0);
        high[11] = 48.0;
        low[11] = 41.0;
        open[11] = 44.0;
        close[11] = 41.0;
        volume[11] = 80.0;
        date[12] = createOhlcData(2001, jan, 17, 12, 0);
        high[12] = 60.0;
        low[12] = 30.0;
        open[12] = 34.0;
        close[12] = 44.0;
        volume[12] = 90.0;
        date[13] = createOhlcData(2001, jan, 18, 12, 0);
        high[13] = 58.0;
        low[13] = 44.0;
        open[13] = 54.0;
        close[13] = 56.0;
        volume[13] = 20.0;
        date[14] = createOhlcData(2001, jan, 19, 12, 0);
        high[14] = 54.0;
        low[14] = 32.0;
        open[14] = 42.0;
        close[14] = 53.0;
        volume[14] = 70.0;
        date[15] = createOhlcData(2001, jan, 20, 12, 0);
        high[15] = 53.0;
        low[15] = 39.0;
        open[15] = 50.0;
        close[15] = 49.0;
        volume[15] = 60.0;
        date[16] = createOhlcData(2001, jan, 21, 12, 0);
        high[16] = 47.0;
        low[16] = 33.0;
        open[16] = 41.0;
        close[16] = 40.0;
        volume[16] = 30.0;
        date[17] = createOhlcData(2001, jan, 22, 12, 0);
        high[17] = 55.0;
        low[17] = 37.0;
        open[17] = 43.0;
        close[17] = 45.0;
        volume[17] = 90.0;
        date[18] = createOhlcData(2001, jan, 23, 12, 0);
        high[18] = 54.0;
        low[18] = 42.0;
        open[18] = 50.0;
        close[18] = 42.0;
        volume[18] = 150.0;
        date[19] = createOhlcData(2001, jan, 24, 12, 0);
        high[19] = 48.0;
        low[19] = 37.0;
        open[19] = 37.0;
        close[19] = 47.0;
        volume[19] = 120.0;
        date[20] = createOhlcData(2001, jan, 25, 12, 0);
        high[20] = 58.0;
        low[20] = 33.0;
        open[20] = 39.0;
        close[20] = 41.0;
        volume[20] = 80.0;
        date[21] = createOhlcData(2001, jan, 26, 12, 0);
        high[21] = 47.0;
        low[21] = 31.0;
        open[21] = 36.0;
        close[21] = 41.0;
        volume[21] = 40.0;
        date[22] = createOhlcData(2001, jan, 27, 12, 0);
        high[22] = 58.0;
        low[22] = 44.0;
        open[22] = 49.0;
        close[22] = 44.0;
        volume[22] = 20.0;
        date[23] = createOhlcData(2001, jan, 28, 12, 0);
        high[23] = 46.0;
        low[23] = 41.0;
        open[23] = 43.0;
        close[23] = 44.0;
        volume[23] = 60.0;
        date[24] = createOhlcData(2001, jan, 29, 12, 0);
        high[24] = 56.0;
        low[24] = 39.0;
        open[24] = 39.0;
        close[24] = 51.0;
        volume[24] = 40.0;
        date[25] = createOhlcData(2001, jan, 30, 12, 0);
        high[25] = 56.0;
        low[25] = 39.0;
        open[25] = 47.0;
        close[25] = 49.0;
        volume[25] = 70.0;
        date[26] = createOhlcData(2001, jan, 31, 12, 0);
        high[26] = 53.0;
        low[26] = 39.0;
        open[26] = 52.0;
        close[26] = 47.0;
        volume[26] = 60.0;
        date[27] = createOhlcData(2001, feb, 1, 12, 0);
        high[27] = 51.0;
        low[27] = 30.0;
        open[27] = 45.0;
        close[27] = 47.0;
        volume[27] = 90.0;
        date[28] = createOhlcData(2001, feb, 2, 12, 0);
        high[28] = 47.0;
        low[28] = 30.0;
        open[28] = 34.0;
        close[28] = 46.0;
        volume[28] = 100.0;
        date[29] = createOhlcData(2001, feb, 3, 12, 0);
        high[29] = 57.0;
        low[29] = 37.0;
        open[29] = 44.0;
        close[29] = 56.0;
        volume[29] = 20.0;
        date[30] = createOhlcData(2001, feb, 4, 12, 0);
        high[30] = 49.0;
        low[30] = 40.0;
        open[30] = 47.0;
        close[30] = 44.0;
        volume[30] = 50.0;
        date[31] = createOhlcData(2001, feb, 5, 12, 0);
        high[31] = 46.0;
        low[31] = 38.0;
        open[31] = 43.0;
        close[31] = 40.0;
        volume[31] = 70.0;
        date[32] = createOhlcData(2001, feb, 6, 12, 0);
        high[32] = 55.0;
        low[32] = 38.0;
        open[32] = 39.0;
        close[32] = 53.0;
        volume[32] = 120.0;
        date[33] = createOhlcData(2001, feb, 7, 12, 0);
        high[33] = 50.0;
        low[33] = 33.0;
        open[33] = 37.0;
        close[33] = 37.0;
        volume[33] = 140.0;
        date[34] = createOhlcData(2001, feb, 8, 12, 0);
        high[34] = 59.0;
        low[34] = 34.0;
        open[34] = 57.0;
        close[34] = 43.0;
        volume[34] = 70.0;
        date[35] = createOhlcData(2001, feb, 9, 12, 0);
        high[35] = 48.0;
        low[35] = 39.0;
        open[35] = 46.0;
        close[35] = 47.0;
        volume[35] = 70.0;
        date[36] = createOhlcData(2001, feb, 10, 12, 0);
        high[36] = 55.0;
        low[36] = 30.0;
        open[36] = 37.0;
        close[36] = 30.0;
        volume[36] = 30.0;
        date[37] = createOhlcData(2001, feb, 11, 12, 0);
        high[37] = 60.0;
        low[37] = 32.0;
        open[37] = 56.0;
        close[37] = 36.0;
        volume[37] = 70.0;
        date[38] = createOhlcData(2001, feb, 12, 12, 0);
        high[38] = 56.0;
        low[38] = 42.0;
        open[38] = 53.0;
        close[38] = 54.0;
        volume[38] = 40.0;
        date[39] = createOhlcData(2001, feb, 13, 12, 0);
        high[39] = 49.0;
        low[39] = 42.0;
        open[39] = 45.0;
        close[39] = 42.0;
        volume[39] = 90.0;
        date[40] = createOhlcData(2001, feb, 14, 12, 0);
        high[40] = 55.0;
        low[40] = 42.0;
        open[40] = 47.0;
        close[40] = 54.0;
        volume[40] = 70.0;
        date[41] = createOhlcData(2001, feb, 15, 12, 0);
        high[41] = 49.0;
        low[41] = 35.0;
        open[41] = 38.0;
        close[41] = 35.0;
        volume[41] = 20.0;
        date[42] = createOhlcData(2001, feb, 16, 12, 0);
        high[42] = 47.0;
        low[42] = 38.0;
        open[42] = 43.0;
        close[42] = 42.0;
        volume[42] = 10.0;
        date[43] = createOhlcData(2001, feb, 17, 12, 0);
        high[43] = 53.0;
        low[43] = 42.0;
        open[43] = 47.0;
        close[43] = 48.0;
        volume[43] = 20.0;
        date[44] = createOhlcData(2001, feb, 18, 12, 0);
        high[44] = 47.0;
        low[44] = 44.0;
        open[44] = 46.0;
        close[44] = 44.0;
        volume[44] = 30.0;
        date[45] = createOhlcData(2001, feb, 19, 12, 0);
        high[45] = 46.0;
        low[45] = 40.0;
        open[45] = 43.0;
        close[45] = 44.0;
        volume[45] = 50.0;
        date[46] = createOhlcData(2001, feb, 20, 12, 0);
        high[46] = 48.0;
        low[46] = 41.0;
        open[46] = 46.0;
        close[46] = 41.0;
        volume[46] = 100.0;
        return new DefaultHighLowDataset("Series 1", date, high, low, open, close, volume);
    }

    private static Date createOhlcData(int y, int m, int d, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, d, hour, min);
        return calendar.getTime();
    }

    private static Icon getProfileIcon(String name, boolean defaultIcon) {
        if (defaultIcon) {
            return new ImageIcon(SampleData.class.getResource("/raven/modal/demo/images/" + name));
        } else {
            AvatarIcon avatarIcon = new AvatarIcon(SampleData.class.getResource("/raven/modal/demo/images/" + name), 55, 55, 3f);
            avatarIcon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
            return avatarIcon;
        }
    }
}
