package raven.modal.component;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.layout.OptionLayoutUtils;
import raven.modal.option.BorderOption;
import raven.modal.option.LayoutOption;
import raven.modal.utils.ModalWindow;
import raven.modal.utils.ModalWindowBorder;

import java.awt.*;

/**
 * @author Raven
 */
public class ModalHeavyWeightLayout extends HeavyWeightRelativeLayout {

    public ModalHeavyWeightLayout(Component owner) {
        super(owner);
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

    private Dimension getShadowSize(BorderOption borderOption) {
        int borderWidth = borderOption.getBorderWidth();
        if (FlatUIUtils.isInsetsEmpty(borderOption.getShadowSize()) && borderWidth == 0) {
            return null;
        }
        Insets shadowSize = borderOption.getShadowSize();
        int width = shadowSize.left + shadowSize.right + borderWidth * 2;
        int height = shadowSize.top + shadowSize.bottom + borderWidth * 2;
        return UIScale.scale(new Dimension(width, height));
    }

    @Override
    protected void updateLayout() {
        if (modalWindows.isEmpty()) return;

        for (int i = 0; i < modalWindows.size(); i++) {
            ModalWindow modal = modalWindows.get(i);
            HeavyWeightModalController controller = (HeavyWeightModalController) modal.getContents();
            LayoutOption layoutOption = controller.getOption().getLayoutOption();
            Dimension extraSize = getShadowSize(controller.getOption().getBorderOption());
            Rectangle rec = OptionLayoutUtils.getLayoutLocation((Container) owner, null, controller, 1, layoutOption, extraSize);
            int width = rec.width;
            int height = rec.height;
            if (extraSize != null) {
                width -= extraSize.width;
                height -= extraSize.height;
            }
            modal.setBounds(rec.x, rec.y, width, height);
        }
    }
}
