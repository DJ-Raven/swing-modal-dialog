package raven.modal.demo.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatClearIcon;
import com.formdev.flatlaf.icons.FlatMenuArrowIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.ModalContainer;
import raven.modal.demo.layout.ResponsiveLayout;
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
import java.util.Arrays;
import java.util.Map;

public class FormSearchPanel extends JPanel {

    private LookAndFeel oldTheme = UIManager.getLookAndFeel();
    private final int SEARCH_MAX_LENGTH = 50;
    private final Map<SystemForm, Class<? extends Form>> formsMap;

    public FormSearchPanel(Map<SystemForm, Class<? extends Form>> formsMap) {
        this.formsMap = formsMap;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,insets 0,wrap", "[fill,500]"));
        textSearch = new JTextField();
        panelResult = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 3, 1));
        panelResult.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,10,0,10;");
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
        showRecentResult();
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
                String st = textSearch.getText().trim();
                if (!st.equals(text)) {
                    text = st;
                    panelResult.removeAll();
                    if (st.isEmpty()) {
                        showRecentResult();
                    } else {
                        for (Map.Entry<SystemForm, Class<? extends Form>> entry : formsMap.entrySet()) {
                            SystemForm s = entry.getKey();
                            if (s.name().toLowerCase().contains(st)
                                    || s.description().toLowerCase().contains(st)
                                    || checkTags(s.tags(), st)) {
                                panelResult.add(new Item(s, entry.getValue(), false));
                            }
                        }
                        if (panelResult.getComponentCount() > 0) {
                            setSelected(0);
                        } else {
                            panelResult.add(createNoResult(st));
                        }
                        panelResult.repaint();
                        SwingUtilities.getAncestorOfClass(ModalContainer.class, FormSearchPanel.this).revalidate();
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

    private void showForm() {
        int index = getSelectedIndex();
        if (index >= 0 && index <= panelResult.getComponentCount() - 1) {
            Component com = panelResult.getComponent(index);
            if (com instanceof Item) {
                ((Item) com).showForm();
            }
        }
    }

    private void setSelected(int index) {
        Component[] components = panelResult.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JButton) {
                ((JButton) components[i]).setSelected(index == i);
                if (i == index) {
                    panelResult.scrollRectToVisible(components[i].getBounds());
                }
            }
        }
    }

    private int getSelectedIndex() {
        for (Component com : panelResult.getComponents()) {
            if (com instanceof JButton) {
                if (((JButton) com).isSelected()) {
                    return panelResult.getComponentZOrder(com);
                }
            }
        }
        return -1;
    }

    private void move(boolean up) {
        int index = getSelectedIndex();
        boolean isShowRecent = panelResult.getComponentCount() > 0 && panelResult.getComponent(0) instanceof JLabel;
        if (index == -1) {
            if (up) {
                setSelected(panelResult.getComponentCount() - 1);
            } else {
                setSelected(isShowRecent ? 1 : 0);
            }
        } else {
            int count = panelResult.getComponentCount();
            if (up) {
                if (index == 0 || (isShowRecent && index == 1)) {
                    setSelected(count - 1);
                } else {
                    setSelected(index - 1);
                }
            } else {
                if (index == count - 1) {
                    setSelected(isShowRecent ? 1 : 0);
                } else {
                    setSelected(index + 1);
                }
            }
        }
    }

    private void showRecentResult() {
        String[] recentSearch = DemoPreferences.getRecentSearch();
        panelResult.removeAll();
        if (recentSearch == null || recentSearch.length == 0) {
            panelResult.add(new NoRecentResult());
        } else {
            for (String r : recentSearch) {
                Item item = createRecentItem(r);
                if (item != null) {
                    panelResult.add(item);
                }
            }
            if (panelResult.getComponentCount() > 0) {
                JLabel label = new JLabel("Recent");
                label.putClientProperty(FlatClientProperties.STYLE, "" +
                        "font:bold +1;" +
                        "border:0,18,0,18;");
                panelResult.add(label, 0);
                setSelected(1);
            }
        }
        Container container = SwingUtilities.getAncestorOfClass(ModalContainer.class, FormSearchPanel.this);
        if (container != null) {
            container.revalidate();
        }
    }

    private Item createRecentItem(String name) {
        for (Map.Entry<SystemForm, Class<? extends Form>> entry : formsMap.entrySet()) {
            if (entry.getKey().name().equals(name)) {
                return new Item(entry.getKey(), entry.getValue(), true);
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
        textSearch.setText("");
    }

    private JTextField textSearch;
    private JPanel panelResult;

    public void searchGrabFocus() {
        textSearch.grabFocus();
    }

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

    private static class Item extends JButton {

        private final SystemForm data;
        private final Class<? extends Form> form;
        private final boolean isRecent;
        private JLabel labelRemove;
        private boolean removeHoverButton;

        public Item(SystemForm data, Class<? extends Form> form, boolean isRecent) {
            this.data = data;
            this.form = form;
            this.isRecent = isRecent;
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
                if (!removeHoverButton) {
                    clearSelected();
                    setSelected(true);
                    showForm();
                } else {
                    removeRecent();
                }
            });
            if (isRecent) {
                addMouseMotionListener(new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        boolean h = SwingUtilities.convertRectangle(labelRemove.getParent(), labelRemove.getBounds(), Item.this).contains(e.getPoint());
                        changeHover(h);
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        boolean h = SwingUtilities.convertRectangle(labelRemove.getParent(), labelRemove.getBounds(), Item.this).contains(e.getPoint());
                        changeHover(h);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        changeHover(false);
                    }

                    private void changeHover(boolean hover) {
                        if (removeHoverButton != hover) {
                            removeHoverButton = hover;
                            if (removeHoverButton) {
                                labelRemove.putClientProperty(FlatClientProperties.STYLE, "" +
                                        "background:$Button.toolbar.hoverBackground;" +
                                        "border:3,3,3,3,#FFFFFF,0,999");
                            } else {
                                labelRemove.putClientProperty(FlatClientProperties.STYLE, "" +
                                        "border:3,3,3,3;");
                            }
                        }
                    }
                });
            }
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
            FormManager.showForm(AllForms.getForm(form));
            DemoPreferences.addRecentSearch(data.name());
        }

        protected Component createRecentOption() {
            JPanel panel = new JPanel(new MigLayout());
            panel.setOpaque(false);
            labelRemove = new JLabel(new FlatClearIcon());
            labelRemove.putClientProperty(FlatClientProperties.STYLE, "" +
                    "border:3,3,3,3;");
            panel.add(labelRemove);
            return panel;
        }

        protected void removeRecent() {
            DemoPreferences.removeRecentSearch(data.name());
            Container parent = getParent();
            parent.remove(this);
            if (parent.getComponentCount() <= 1) {
                parent.removeAll();
                parent.add(new NoRecentResult());
            }
            SwingUtilities.getAncestorOfClass(ModalContainer.class, parent).revalidate();
        }
    }
}
