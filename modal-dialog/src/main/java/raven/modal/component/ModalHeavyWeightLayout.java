package raven.modal.component;

import raven.modal.layout.OptionLayoutUtils;
import raven.modal.option.BorderOption;
import raven.modal.option.LayoutOption;
import raven.modal.option.Option;
import raven.modal.utils.ModalWindow;
import raven.modal.utils.ModalWindowBorder;
import raven.modal.utils.ModalWindowFactory;

import java.awt.*;

/**
 * @author Raven
 */
public class ModalHeavyWeightLayout extends HeavyWeightRelativeLayout {

    public ModalHeavyWeightLayout(Component owner) {
        super(owner);
    }

    @Override
    protected ModalWindow createModalWindow(Component contents) {
        HeavyWeightModalController controller = (HeavyWeightModalController) contents;
        ModalWindow modalWindow = super.createModalWindow(contents);
        Option option = controller.getOption();
        if (option.getBackgroundClickType() != Option.BackgroundClickType.NONE) {
            modalWindow = ModalWindowFactory.getInstance().createWindowBackground(modalWindow);
        }
        return modalWindow;
    }

    @Override
    protected ModalWindowBorder getModalWindowBorder(Component contents) {
        if (contents instanceof HeavyWeightModalController) {
            HeavyWeightModalController modal = (HeavyWeightModalController) contents;
            BorderOption borderOption = modal.getOption().getBorderOption();
            return new ModalWindowBorder(
                    borderOption.getShadowSize(),
                    borderOption.getShadowOpacity(),
                    borderOption.getShadowColor(),
                    borderOption.getBorderWidth(),
                    borderOption.getBorderColor(),
                    borderOption.getRound()
            );
        }
        return null;
    }

    @Override
    protected void updateLayout() {
        if (modalWindows.isEmpty()) return;

        for (int i = 0; i < modalWindows.size(); i++) {
            ModalWindow modal = modalWindows.get(i);
            HeavyWeightModalController controller = (HeavyWeightModalController) modal.getContents();
            LayoutOption layoutOption = controller.getOption().getLayoutOption();
            Rectangle modalBorderSize = getModalBorderSize(modal);
            Dimension extraSize = modalBorderSize == null ? null : modalBorderSize.getSize();

            Rectangle rec = OptionLayoutUtils.getLayoutLocation((Container) owner, null, controller, 1, layoutOption, extraSize, true);
            int width = rec.width;
            int height = rec.height;
            if (modalBorderSize != null) {
                width -= modalBorderSize.width;
                height -= modalBorderSize.height;
                rec.x += modalBorderSize.x;
                rec.y += modalBorderSize.y;
            }
            modal.setBounds(rec.x, rec.y, width, height);
        }
    }
}
