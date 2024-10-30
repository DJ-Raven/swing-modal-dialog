package raven.modal.demo.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatMenuArrowIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.ModalDialog;
import raven.modal.component.ModalContainer;
import raven.modal.demo.icons.SVGIconUIColor;
import raven.modal.demo.system.*;
import raven.modal.demo.utils.DemoPreferences;
import raven.modal.demo.utils.SystemForm;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FormSearchPanel extends JPanel {

    private LookAndFeel oldTheme = UIManager.getLookAndFeel();
    private final int SEARCH_MAX_LENGTH = 50;
    private final Map<SystemForm, Class<? extends Form>> formsMap;
    private final List<Item> listItems = new ArrayList<>();

    public FormSearchPanel(Map<SystemForm, Class<? extends Form>> formsMap) {
        this.formsMap = formsMap;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,insets 0,wrap", "[fill,500]"));
        textSearch = new JTextField();
        panelResult = new JPanel(new MigLayout("insets 3 10 3 10,fillx,wrap", "[fill]"));
        textSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        textSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        textSearch.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,3,3,3;" +
                "background:null;" +
                "showClearButton:true;");
        add(textSearch, "gap 17 17 0 0");
        add(new JSeparator(), "height 2!");
        JScrollPane scrollPane = new JScrollPane(panelResult);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                "trackArc:$ScrollBar.thumbArc;" +
                "thumbInsets:0,3,0,3;" +
                "trackInsets:0,3,0,3;" +
                "width:12;");
        add(scrollPane);
        installSearchField();
    }

    public final void formCheck() {
        if (oldTheme != UIManager.getLookAndFeel()) {
            oldTheme = UIManager.getLookAndFeel();
            SwingUtilities.updateComponentTreeUI(this);
        }
    }

    private void installSearchField() {
        textSearch.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (getLength() + str.length() <= SEARCH_MAX_LENGTH) {
                    super.insertString(offs, str, a);
                }
            }
        });
        textSearch.getDocument().addDocumentListener(new DocumentListener() {
            private String text;

            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String st = textSearch.getText().trim().toLowerCase(); // Convert search term to lowercase
                if (!st.equals(text)) {
                    text = st;
                    panelResult.removeAll();
                    listItems.clear();
                    if (st.isEmpty()) {
                        showRecentResult();
                    } else {
                        for (Map.Entry<SystemForm, Class<? extends Form>> entry : formsMap.entrySet()) {
                            SystemForm s = entry.getKey();
                            // Compare both name and description with lower cased search term
                            if (s.name().toLowerCase().contains(st)
                                    || s.description().toLowerCase().contains(st)
                                    || checkTags(s.tags(), st)) {
                                Item item = new Item(s, entry.getValue(), false, false);
                                panelResult.add(item);
                                listItems.add(item);
                            }
                        }
                        if (!listItems.isEmpty()) {
                            setSelected(0);
                        } else {
                            panelResult.add(createNoResult(st));
                        }
                        panelResult.repaint();
                        updateLayout();
                    }
                }
            }

            private boolean checkTags(String tags[], String st) {
                if (tags.length == 0) return false;
                boolean contains = Arrays.stream(tags).anyMatch(s -> s.contains(st));
                return contains;
            }
        });
        textSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        move(true);
                        break;
                    case KeyEvent.VK_DOWN:
                        move(false);
                        break;
                    case KeyEvent.VK_ENTER:
                        showForm();
                        break;
                }
            }
        });
    }

    private void updateLayout() {
        Container container = SwingUtilities.getAncestorOfClass(ModalContainer.class, FormSearchPanel.this);
        if (container != null) {
            container.revalidate();
        }
    }

    private void showForm() {
        int index = getSelectedIndex();
        if (index != -1) {
            listItems.get(index).showForm();
        }
    }

    private void setSelected(int index) {
        for (int i = 0; i < listItems.size(); i++) {
            listItems.get(i).setSelected(index == i);
        }
    }

    private int getSelectedIndex() {
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void move(boolean up) {
        if (listItems.isEmpty()) return;
        int index = getSelectedIndex();
        int size = listItems.size();
        if (index == -1) {
            if (up) {
                index = listItems.size() - 1;
            } else {
                index = 0;
            }
        } else {
            if (up) {
                index = (index == 0) ? size - 1 : index - 1;
            } else {
                index = (index == size - 1) ? 0 : index + 1;
            }
        }
        setSelected(index);
    }

    private void showRecentResult() {
        String[] recentSearch = DemoPreferences.getRecentSearch(false);
        String[] favoriteSearch = DemoPreferences.getRecentSearch(true);
        panelResult.removeAll();
        listItems.clear();
        if (recentSearch != null && recentSearch.length > 0) {
            for (String r : recentSearch) {
                Item item = createRecentItem(r, false);
                if (item != null) {
                    panelResult.add(item);
                    listItems.add(item);
                }
            }
            if (!listItems.isEmpty()) {
                panelResult.add(createLabel("Recent"), 0);
            }
        }

        if (favoriteSearch != null && favoriteSearch.length > 0) {
            panelResult.add(createLabel("Favorite"));
            for (String r : favoriteSearch) {
                Item item = createRecentItem(r, true);
                if (item != null) {
                    panelResult.add(item);
                    listItems.add(item);
                }
            }
        }
        if (listItems.isEmpty()) {
            panelResult.add(new NoRecentResult());
        } else {
            setSelected(0);
        }
        updateLayout();
    }

    private JLabel createLabel(String title) {
        JLabel label = new JLabel(title);
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +1;" +
                "border:5,15,5,15;");
        return label;
    }

    private Item createRecentItem(String name, boolean favorite) {
        for (Map.Entry<SystemForm, Class<? extends Form>> entry : formsMap.entrySet()) {
            if (entry.getKey().name().equals(name)) {
                return new Item(entry.getKey(), entry.getValue(), true, favorite);
            }
        }
        return null;
    }

    private Component createNoResult(String text) {
        JPanel panel = new JPanel(new MigLayout("insets 15 5 15 5,al center,gapx 1"));
        JLabel label = new JLabel("No result for \"");
        JLabel labelEnd = new JLabel("\"");
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");
        labelEnd.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");
        JLabel labelText = new JLabel(text);

        panel.add(label);
        panel.add(labelText);
        panel.add(labelEnd);
        return panel;
    }

    public void clearSearch() {
        if (!textSearch.getText().isEmpty()) {
            textSearch.setText("");
        } else {
            showRecentResult();
        }
    }

    public void searchGrabFocus() {
        textSearch.grabFocus();
    }

    private JTextField textSearch;
    private JPanel panelResult;

    private static class NoRecentResult extends JPanel {

        public NoRecentResult() {
            init();
        }

        private void init() {
            setLayout(new MigLayout("insets 15 5 15 5,al center"));
            JLabel label = new JLabel("No recent searches");
            label.putClientProperty(FlatClientProperties.STYLE, "" +
                    "foreground:$Label.disabledForeground;" +
                    "font:bold;");
            add(label);
        }
    }

    private class Item extends JButton {

        private final SystemForm data;
        private final Class<? extends Form> form;
        private final boolean isRecent;
        private final boolean isFavorite;
        private Component itemSource;

        public Item(SystemForm data, Class<? extends Form> form, boolean isRecent, boolean isFavorite) {
            this.data = data;
            this.form = form;
            this.isRecent = isRecent;
            this.isFavorite = isFavorite;
            init();
        }

        private void init() {
            setFocusable(false);
            setHorizontalAlignment(JButton.LEADING);
            setLayout(new MigLayout("insets 3 3 3 0,filly,gapy 2", "[]push[]"));
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "background:null;" +
                    "arc:10;" +
                    "borderWidth:0;" +
                    "focusWidth:0;" +
                    "innerFocusWidth:0;" +
                    "[light]selectedBackground:lighten($Button.selectedBackground,9%)");
            JLabel labelDescription = new JLabel(data.description());
            labelDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                    "foreground:$Label.disabledForeground;");
            add(new JLabel(data.name()), "cell 0 0");
            add(labelDescription, "cell 0 1");
            if (!isRecent) {
                add(new JLabel(new FlatMenuArrowIcon()), "cell 1 0,span 1 2");
            } else {
                add(createRecentOption(), "cell 1 0,span 1 2");
            }
            addActionListener(e -> {
                if (itemSource == null) {
                    clearSelected();
                    setSelected(true);
                    showForm();
                } else if (itemSource.getName().equals("remove")) {
                    removeRecent();
                } else if (itemSource.getName().equals("favorite")) {
                    addFavorite();
                }
            });
        }

        private void clearSelected() {
            for (Component com : getParent().getComponents()) {
                if (com instanceof JButton) {
                    ((JButton) com).setSelected(false);
                }
            }
        }

        protected void showForm() {
            ModalDialog.closeModal(FormSearch.ID);
            Drawer.setSelectedItemClass(form);
            if (!isFavorite) {
                DemoPreferences.addRecentSearch(data.name(), false);
            }
        }

        protected Component createRecentOption() {
            JPanel panel = new JPanel(new MigLayout("insets n 0 n 0,fill,gapx 2", "", "[fill]"));
            panel.setOpaque(false);
            JButton cmdRemove = createButton("remove", "clear.svg", 0.35f, "Label.foreground", 0.9f);
            if (!isFavorite) {
                JButton cmdFavorite = createButton("favorite", "favorite.svg", 0.4f, "Component.accentColor", 0.9f);
                panel.add(cmdFavorite);
            } else {
                JLabel label = new JLabel(new SVGIconUIColor("raven/modal/demo/icons/favorite_filled.svg", 0.4f, "Component.accentColor", 0.8f));
                label.putClientProperty(FlatClientProperties.STYLE, "" +
                        "border:3,3,3,3;");
                panel.add(label);
            }
            panel.add(new JSeparator(JSeparator.VERTICAL), "gapy 5 5");
            panel.add(cmdRemove);
            return panel;
        }

        private JButton createButton(String name, String icon, float scale, String hoverKey, float alpha) {
            SVGIconUIColor svgIcon = new SVGIconUIColor("raven/modal/demo/icons/" + icon, scale, "Label.disabledForeground", alpha);
            JButton button = new JButton(svgIcon);
            button.setName(name);
            button.setFocusable(false);
            button.setContentAreaFilled(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setModel(getModel());
            button.putClientProperty(FlatClientProperties.STYLE, "" +
                    "margin:3,3,3,3;");

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    svgIcon.setColorKey(hoverKey);
                    itemSource = (Component) e.getSource();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    svgIcon.setColorKey("Label.disabledForeground");
                    itemSource = null;
                }
            });
            return button;
        }

        protected void removeRecent() {
            DemoPreferences.removeRecentSearch(data.name(), isFavorite);
            panelResult.remove(this);
            listItems.remove(this);
            if (listItems.isEmpty()) {
                panelResult.removeAll();
                panelResult.add(new NoRecentResult());
            } else {
                if (getCount(isFavorite) == 0) {
                    if (isFavorite) {
                        panelResult.remove(panelResult.getComponentCount() - 1);
                    } else {
                        panelResult.remove(0);
                    }
                }
            }
            updateLayout();
        }

        protected void addFavorite() {
            DemoPreferences.addRecentSearch(data.name(), true);
            int index[] = getFirstFavoriteIndex();
            panelResult.remove(this);
            listItems.remove(this);
            Item item = new Item(data, form, isRecent, true);
            if (index == null) {
                panelResult.add(createLabel("Favorite"));
                panelResult.add(item);
                listItems.add(item);
            } else {
                panelResult.remove(this);
                listItems.remove(this);
                panelResult.add(item, index[1] - 1);
                listItems.add(index[0] - 1, item);
            }
            if (getCount(false) == 0) {
                panelResult.remove(0);
            }
            updateLayout();
        }

        private int getCount(boolean favorite) {
            int count = 0;
            for (Item item : listItems) {
                if (item.isFavorite == favorite) {
                    count++;
                }
            }
            return count;
        }

        private int[] getFirstFavoriteIndex() {
            for (int i = 0; i < listItems.size(); i++) {
                if (listItems.get(i).isFavorite) {
                    return new int[]{i, panelResult.getComponentZOrder(listItems.get(i))};
                }
            }
            return null;
        }
    }
}
