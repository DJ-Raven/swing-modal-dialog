package raven.modal.demo.auth;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import raven.modal.demo.system.Form;
import raven.modal.demo.system.FormManager;

import javax.swing.*;

public class Login extends Form {

    public Login() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        chRememberMe = new JCheckBox("Remember me");
        cmdLogin = new JButton("Login");
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 35 45", "fill,250:280"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:shade($Panel.background,5%);" +
                "[dark]background:tint($Panel.background,5%);");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:shade($Panel.background,10%);" +
                "[dark]background:tint($Panel.background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        JLabel lbTitle = new JLabel("Welcome back!");
        JLabel description = new JLabel("Please sign in to access your account");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:$Label.disabledForeground;");

        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Username"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(chRememberMe, "grow 0");
        panel.add(cmdLogin, "gapy 10");
        add(panel);

        // event
        cmdLogin.addActionListener((e) -> {
            String userName = txtUsername.getText().trim();
            // this is just for example to check admin user :)
            boolean isAdmin = userName.equals("admin");
            FormManager.login();
        });
    }

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chRememberMe;
    private JButton cmdLogin;
}
