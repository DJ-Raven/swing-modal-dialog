package raven.modal.demo.system;

import raven.modal.ModalDialog;
import raven.modal.demo.component.EmptyModalBorder;
import raven.modal.demo.component.FormSearchPanel;
import raven.modal.demo.menu.MyDrawerBuilder;
import raven.modal.demo.utils.SystemForm;
import raven.modal.drawer.item.Item;
import raven.modal.drawer.item.MenuItem;
import raven.modal.option.Location;
import raven.modal.option.Option;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormSearch {

    private static FormSearch instance;
    public static final String ID = "search";
    private Map<SystemForm, Class<? extends Form>> formsMap;
    private FormSearchPanel searchPanel;

    public static FormSearch getInstance() {
        if (instance == null) {
            instance = new FormSearch();
        }
        return instance;
    }

    private FormSearch() {
        formsMap = new HashMap<>();
        for (Class<? extends Form> cls : getClassForms()) {
            if (cls.isAnnotationPresent(SystemForm.class)) {
                SystemForm f = cls.getAnnotation(SystemForm.class);
                formsMap.put(f, cls);
            }
        }
    }

    private Class<? extends Form>[] getClassForms() {
        MenuItem[] menuItems = MyDrawerBuilder.getInstance().getSimpleMenuOption().getMenus();
        List<Class<?>> formClass = new ArrayList<>();
        getMenuClass(menuItems, formClass);
        return formClass.toArray(new Class[0]);
    }

    private void getMenuClass(MenuItem[] menuItems, List<Class<?>> formClass) {
        for (MenuItem menu : menuItems) {
            if (menu.isMenu()) {
                Item item = (Item) menu;
                if (item.getItemClass() != null) {
                    formClass.add(item.getItemClass());
                }
                if (item.isSubmenuAble()) {
                    getMenuClass(item.getSubMenu().toArray(new Item[0]), formClass);
                }
            }
        }
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
        option.setAnimationEnabled(false);
        option.getLayoutOption().setMargin(20, 10, 10, 10).setLocation(Location.CENTER, Location.TOP);
        ModalDialog.showModal(FormManager.getFrame(), new EmptyModalBorder(getSearchPanel(), (controller, action) -> {
            if (action == EmptyModalBorder.OPENED) {
                searchPanel.searchGrabFocus();
            }
        }), option, ID);
    }

    private JPanel getSearchPanel() {
        if (searchPanel == null) {
            searchPanel = new FormSearchPanel(formsMap);
        }
        searchPanel.formCheck();
        searchPanel.clearSearch();
        return searchPanel;
    }
}
