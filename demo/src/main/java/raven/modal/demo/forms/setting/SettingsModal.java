package raven.modal.demo.forms.setting;

import net.miginfocom.swing.MigLayout;
import raven.modal.demo.forms.setting.tabb.Appearance;
import raven.modal.demo.system.Form;
import raven.modal.drawer.DrawerBuilder;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.simple.SimpleDrawerBuilder;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SettingsModal extends JPanel {

    private DrawerPanel drawerPanel;

    public SettingsModal() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 0", "[fill,250!,grow 0]0[fill,2!,grow 0][fill]", "[fill]"));

        createDrawerMenu();
        createContainer();

        SimpleDrawerBuilder drawerBuilder = (SimpleDrawerBuilder) drawerPanel.getDrawerBuilder();
        drawerBuilder.getDrawerMenu().setMenuSelectedClass(Appearance.class);
    }

    private void createDrawerMenu() {
        DrawerBuilder drawerBuilder = new SettingDrawerBuilder((action, index) -> {
            showForm(action.getItem().getItemClass());
        });

        drawerPanel = new DrawerPanel(drawerBuilder, drawerBuilder.getOption());
        add(drawerPanel);

        add(new JSeparator(JSeparator.VERTICAL));
    }

    private void createContainer() {
        classForms = new HashMap<>();
        panel = new JPanel(new MigLayout("fill", "[fill]", "[fill]"));
        add(panel);
    }

    private void showForm(Class<?> clazz) {
        if (clazz != null) {
            Form form;
            if (classForms.containsKey(clazz)) {
                form = classForms.get(clazz);
            } else {
                try {
                    form = (Form) clazz.getDeclaredConstructor().newInstance();
                    classForms.put(clazz, form);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            if (form != null) {
                form.formCheck();
                panel.removeAll();
                panel.add(form);
                panel.repaint();
                panel.revalidate();
            }
        }
    }

    private Map<Class<?>, Form> classForms;
    private JPanel panel;
}
