package raven.modal.demo.system;

import javax.swing.*;

public class Form extends JPanel {

    private LookAndFeel oldTheme = UIManager.getLookAndFeel();

    public Form() {
        init();
    }

    private void init() {
    }

    public void formInit() {
    }

    public void formOpen() {
    }

    public void formRefresh() {
    }

    protected final void formCheck() {
        if (oldTheme != UIManager.getLookAndFeel()) {
            oldTheme = UIManager.getLookAndFeel();
            SwingUtilities.updateComponentTreeUI(this);
        }
    }
}
