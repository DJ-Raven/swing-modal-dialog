package raven.modal.demo.component.dashboard;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardBox extends JPanel {

    private List<CardItem> cardItems = new ArrayList<>();

    public CardBox() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("", "[fill]", "[fill]"));
        putClientProperty(FlatClientProperties.STYLE_CLASS, "dashboardBackground");
    }

    private void createSeparator() {
        add(new JSeparator(JSeparator.VERTICAL), "width 3!");
    }

    public void addCardItem(Icon icon, String title) {
        CardItem cardItem = new CardItem(icon, title);
        cardItems.add(cardItem);
        if (cardItems.size() > 1) {
            createSeparator();
        }
        add(cardItem, "width 100%");
    }

    public void setValueAt(int index, String value, String description, String tags, boolean up) {
        cardItems.get(index).setValue(value, description, tags, up);
    }

    public void setCardIconColor(int index, Color color) {
        cardItems.get(index).setCardIconColor(color);
    }
}
