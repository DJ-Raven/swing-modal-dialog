package raven.modal.demo.sample;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import raven.extras.AvatarIcon;
import raven.modal.demo.model.ModelEmployee;
import raven.modal.demo.model.ModelProfile;

import javax.swing.*;
import java.util.ArrayList;
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

    public static XYDataset getTimeSeriesDataset() {
        TimeSeries s1 = new TimeSeries("Income");
        s1.add(new Month(2, 2001), 181.8);
        s1.add(new Month(3, 2001), 167.3);
        s1.add(new Month(4, 2001), 153.8);
        s1.add(new Month(5, 2001), 167.6);
        s1.add(new Month(6, 2001), 158.8);
        s1.add(new Month(7, 2001), 148.3);
        s1.add(new Month(8, 2001), 153.9);
        s1.add(new Month(9, 2001), 142.7);
        s1.add(new Month(10, 2001), 123.2);
        s1.add(new Month(11, 2001), 131.8);
        s1.add(new Month(12, 2001), 139.6);
        s1.add(new Month(1, 2002), 142.9);
        s1.add(new Month(2, 2002), 138.7);
        s1.add(new Month(3, 2002), 137.3);
        s1.add(new Month(4, 2002), 143.9);
        s1.add(new Month(5, 2002), 139.8);
        s1.add(new Month(6, 2002), 80.0);
        s1.add(new Month(7, 2002), 50.8);

        TimeSeries s2 = new TimeSeries("Expense");
        s2.add(new Month(2, 2001), 129.6);
        s2.add(new Month(3, 2001), 123.2);
        s2.add(new Month(4, 2001), 117.2);
        s2.add(new Month(5, 2001), 124.1);
        s2.add(new Month(6, 2001), 122.6);
        s2.add(new Month(7, 2001), 119.2);
        s2.add(new Month(8, 2001), 116.5);
        s2.add(new Month(9, 2001), 112.7);
        s2.add(new Month(10, 2001), 101.5);
        s2.add(new Month(11, 2001), 106.1);
        s2.add(new Month(12, 2001), 125.2);
        s2.add(new Month(1, 2002), 111.7);
        s2.add(new Month(2, 2002), 111.0);
        s2.add(new Month(3, 2002), 109.6);
        s2.add(new Month(4, 2002), 113.2);
        s2.add(new Month(5, 2002), 111.6);
        s2.add(new Month(6, 2002), 108.8);
        s2.add(new Month(7, 2002), 101.6);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
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
