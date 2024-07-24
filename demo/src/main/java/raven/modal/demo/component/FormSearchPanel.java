package raven.modal.demo.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatMenuArrowIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.ModalContainer;
import raven.modal.demo.layout.ResponsiveLayout;
import raven.modal.demo.system.*;
import raven.modal.demo.utils.SystemForm;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Map;

public class FormSearchPanel extends JPanel {

    private LookAndFeel oldTheme = UIManager.getLookAndFeel();
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

        for (Map.Entry<SystemForm, Class<? extends Form>> entry : formsMap.entrySet()) {
            panelResult.add(new Item(entry.getKey(), entry.getValue()));
        }
        if (panelResult.getComponentCount() > 0) {
            setSelected(0);
        }
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
                    for (Map.Entry<SystemForm, Class<? extends Form>> entry : formsMap.entrySet()) {
                        SystemForm s = entry.getKey();
                        if (s.name().toLowerCase().contains(st)
                                || s.description().toLowerCase().contains(st)
                                || checkTags(s.tags(), st)) {
                            panelResult.add(new Item(s, entry.getValue()));
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
        if (index == -1) {
            if (up) {
                setSelected(panelResult.getComponentCount() - 1);
            } else {
                setSelected(0);
            }
        } else {
            int count = panelResult.getComponentCount();
            if (up) {
                if (index == 0) {
                    setSelected(count - 1);
                } else {
                    setSelected(index - 1);
                }
            } else {
                if (index == count - 1) {
                    setSelected(0);
                } else {
                    setSelected(index + 1);
                }
            }
        }
    }

    private Component createNoResult(String text) {
        JPanel panel = new JPanel(new MigLayout("al center,gapx 1"));
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

    private JTextField textSearch;
    private JPanel panelResult;

    public void searchGrabFocus() {
        textSearch.grabFocus();
    }

    private static class Item extends JButton {

        private final SystemForm data;
        private final Class<? extends Form> form;

        public Item(SystemForm data, Class<? extends Form> form) {
            this.data = data;
            this.form = form;
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
            add(new JLabel(new FlatMenuArrowIcon()), "cell 1 0,span 1 2");
            addActionListener(e -> {
                clearSelected();
                setSelected(true);
                showForm();
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
            FormManager.showForm(AllForms.getForm(form));
        }
    }
}
