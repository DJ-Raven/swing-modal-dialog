package raven.modal.demo.system;

import raven.modal.ModalDialog;
import raven.modal.demo.component.EmptyModalBorder;
import raven.modal.demo.component.FormSearchPanel;
import raven.modal.option.Location;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class FormSearch {

    private static FormSearch instance;
    private final String ID = "search";
    private FormSearchPanel searchPanel;

    public static FormSearch getInstance() {
        if (instance == null) {
            instance = new FormSearch();
        }
        return instance;
    }

    private FormSearch() {
    }

    public void installKeyMap(JComponent component) {
        ActionListener key = e -> showSearch();
        component.registerKeyboardAction(key, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void showSearch() {
        if (ModalDialog.isIdExist(ID)) {
            return;
        }
        Option option = ModalDialog.createOption();
        option.getLayoutOption().setMargin(20, 10, 10, 10).setLocation(Location.CENTER, Location.TOP);
        ModalDialog.showModal(FormManager.getFrame(), new EmptyModalBorder(getSearchPanel()), option, ID);
    }

    private JPanel getSearchPanel() {
        if (searchPanel == null) {
            searchPanel = new FormSearchPanel();
        }
        return searchPanel;
    }
}
