package raven.modal.drawer.simple.footer;

import net.miginfocom.swing.MigLayout;
import raven.extras.LightDarkButton;
import raven.modal.drawer.menu.MenuOption;

/**
 * @author Raven
 */
public class LightDarkButtonFooter extends SimpleFooter {

    public static final int LIGHT_DARK_BUTTON_STYLE = 2;

    public LightDarkButtonFooter(SimpleFooterData simpleFooterData) {
        super(simpleFooterData);
    }

    @Override
    protected void initComponent() {
        layout = new MigLayout("hidemode 3,wrap,fillx,insets 5 5 15 5,fill,gap 3", "25[fill]25");
        setLayout(layout);
        lightDarkButton = new LightDarkButton(999);
        lightDarkButton.installAutoLafChangeListener();

        if (getSimpleFooterData().getSimpleFooterStyle() != null) {
            getSimpleFooterData().getSimpleFooterStyle().styleComponent(lightDarkButton, LIGHT_DARK_BUTTON_STYLE);
        }

        add(lightDarkButton);
    }

    @Override
    public void layoutOptionChanged(MenuOption.MenuOpenMode menuOpenMode) {
        if (menuOpenMode == MenuOption.MenuOpenMode.FULL) {
            lightDarkButton.setButtonStyle(LightDarkButton.ButtonStyle.DUAL_BUTTON);
            layout.setColumnConstraints("25[fill]25");
        } else {
            lightDarkButton.setButtonStyle(LightDarkButton.ButtonStyle.TOGGLE_BUTTON);
            layout.setColumnConstraints("[center]");
        }
    }

    public void addModeChangeListener(LightDarkButton.ModeChangeListener listener) {
        lightDarkButton.addModeChangeListener(listener);
    }

    public LightDarkButton getLightDarkButton() {
        return lightDarkButton;
    }

    protected LightDarkButton lightDarkButton;
}
