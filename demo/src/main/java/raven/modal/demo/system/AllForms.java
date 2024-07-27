package raven.modal.demo.system;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class AllForms {

    private static AllForms instance;

    private final Map<Class<? extends Form>, Form> formsMap;

    private static AllForms getInstance() {
        if (instance == null) {
            instance = new AllForms();
        }
        return instance;
    }

    private AllForms() {
        formsMap = new HashMap<>();
    }

    public static Form getForm(Class<? extends Form> cls) {
        if (getInstance().formsMap.containsKey(cls)) {
            return getInstance().formsMap.get(cls);
        }
        try {
            Form form = cls.getDeclaredConstructor().newInstance();
            getInstance().formsMap.put(cls, form);
            formInit(form);
            return form;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void formInit(Form form) {
        SwingUtilities.invokeLater(() -> form.formInit());
    }
}
