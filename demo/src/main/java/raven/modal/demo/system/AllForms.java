package raven.modal.demo.system;

import raven.modal.demo.forms.*;

public class AllForms {

    private static AllForms instance;

    private FormDashboard formDashboard;
    private FormSetting formSetting;
    private FormModal formModal;
    private FormToast formToast;
    private FormDateTime formDateTime;
    private FormInput formInput;
    private FormTable formTable;

    private static AllForms getInstance() {
        if (instance == null) {
            instance = new AllForms();
        }
        return instance;
    }

    private AllForms() {
    }

    public static Form getFormDashboard() {
        if (getInstance().formDashboard == null) {
            getInstance().formDashboard = new FormDashboard();
            getInstance().formDashboard.formInit();
        }
        return getInstance().formDashboard;
    }

    public static Form getFormSetting() {
        if (getInstance().formSetting == null) {
            getInstance().formSetting = new FormSetting();
            getInstance().formSetting.formInit();
        }
        return getInstance().formSetting;
    }

    public static Form getFormModal() {
        if (getInstance().formModal == null) {
            getInstance().formModal = new FormModal();
            getInstance().formModal.formInit();
        }
        return getInstance().formModal;
    }

    public static Form getFormToast() {
        if (getInstance().formToast == null) {
            getInstance().formToast = new FormToast();
            getInstance().formToast.formInit();
        }
        return getInstance().formToast;
    }

    public static Form getFormDateTime() {
        if (getInstance().formDateTime == null) {
            getInstance().formDateTime = new FormDateTime();
            getInstance().formDateTime.formInit();
        }
        return getInstance().formDateTime;
    }

    public static Form getFormInput() {
        if (getInstance().formInput == null) {
            getInstance().formInput = new FormInput();
            getInstance().formInput.formInit();
        }
        return getInstance().formInput;
    }

    public static Form getFormTable() {
        if (getInstance().formTable == null) {
            getInstance().formTable = new FormTable();
            getInstance().formTable.formInit();
        }
        return getInstance().formTable;
    }
}
