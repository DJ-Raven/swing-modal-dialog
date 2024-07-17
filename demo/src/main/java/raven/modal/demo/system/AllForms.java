package raven.modal.demo.system;

import raven.modal.demo.forms.*;

import javax.swing.*;

public class AllForms {

    private static AllForms instance;

    private FormDashboard formDashboard;
    private FormSetting formSetting;
    private FormModal formModal;
    private FormToast formToast;
    private FormDateTime formDateTime;
    private FormAvatarIcon formAvatarIcon;
    private FormInput formInput;
    private FormTable formTable;
    private FormResponsiveLayout formResponsiveLayout;

    private static AllForms getInstance() {
        if (instance == null) {
            instance = new AllForms();
        }
        return instance;
    }

    private AllForms() {
    }

    public static void formInit(Form form) {
        SwingUtilities.invokeLater(() -> form.formInit());
    }

    public static Form getFormDashboard() {
        if (getInstance().formDashboard == null) {
            getInstance().formDashboard = new FormDashboard();
            formInit(getInstance().formDashboard);
        }
        return getInstance().formDashboard;
    }

    public static Form getFormSetting() {
        if (getInstance().formSetting == null) {
            getInstance().formSetting = new FormSetting();
            formInit(getInstance().formSetting);
        }
        return getInstance().formSetting;
    }

    public static Form getFormModal() {
        if (getInstance().formModal == null) {
            getInstance().formModal = new FormModal();
            formInit(getInstance().formModal);
        }
        return getInstance().formModal;
    }

    public static Form getFormToast() {
        if (getInstance().formToast == null) {
            getInstance().formToast = new FormToast();
            formInit(getInstance().formToast);
        }
        return getInstance().formToast;
    }

    public static Form getFormDateTime() {
        if (getInstance().formDateTime == null) {
            getInstance().formDateTime = new FormDateTime();
            formInit(getInstance().formDateTime);
        }
        return getInstance().formDateTime;
    }

    public static Form getFormAvatarIcon() {
        if (getInstance().formAvatarIcon == null) {
            getInstance().formAvatarIcon = new FormAvatarIcon();
            formInit(getInstance().formAvatarIcon);
        }
        return getInstance().formAvatarIcon;
    }

    public static Form getFormInput() {
        if (getInstance().formInput == null) {
            getInstance().formInput = new FormInput();
            formInit(getInstance().formInput);
        }
        return getInstance().formInput;
    }

    public static Form getFormTable() {
        if (getInstance().formTable == null) {
            getInstance().formTable = new FormTable();
            formInit(getInstance().formTable);
        }
        return getInstance().formTable;
    }

    public static Form getFormResponsiveLayout() {
        if (getInstance().formResponsiveLayout == null) {
            getInstance().formResponsiveLayout = new FormResponsiveLayout();
            formInit(getInstance().formResponsiveLayout);
        }
        return getInstance().formResponsiveLayout;
    }
}
