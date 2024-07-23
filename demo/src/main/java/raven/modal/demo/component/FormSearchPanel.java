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
import java.util.Arrays;
import java.util.Map;

public class FormSearchPanel extends JPanel {

    private final Map<SystemForm, Class<? extends Form>> formsMap;

    public FormSearchPanel(Map<SystemForm, Class<? extends Form>> formsMap) {
        this.formsMap = formsMap;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,insets 0,wrap", "[fill,500]"));
        textSearch = new JTextField();
        panelResult = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 0, 1));
        panelResult.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,10,0,10;");
        textSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        textSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        textSearch.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:3,20,3,20;" +
                "background:null;");
        add(textSearch);
        add(new JSeparator(), "height 2");
        JScrollPane scrollPane = new JScrollPane(panelResult);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        for (Map.Entry<SystemForm, Class<? extends Form>> entry : formsMap.entrySet()) {
            panelResult.add(new Item(entry.getKey(), entry.getValue()));
        }
        add(scrollPane);
        installSearchField();
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
            setHorizontalAlignment(JButton.LEADING);
            setLayout(new MigLayout("insets 3 3 3 0,filly,gapy 2", "[]push[]"));
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "background:null;" +
                    "arc:10;" +
                    "borderWidth:0;" +
                    "focusWidth:0;" +
                    "innerFocusWidth:0;");
            JLabel labelDescription = new JLabel(data.description());
            labelDescription.putClientProperty(FlatClientProperties.STYLE, "" +
                    "foreground:$Label.disabledForeground;");
            add(new JLabel(data.name()), "cell 0 0");
            add(labelDescription, "cell 0 1");
            add(new JLabel(new FlatMenuArrowIcon()), "cell 1 0,span 1 2");
            addActionListener(e -> {
                ModalDialog.closeModal(FormSearch.ID);
                FormManager.showForm(AllForms.getForm(form));
            });
        }
    }
}
