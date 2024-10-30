package raven.modal.demo.icons;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public class SVGIconUIColor extends FlatSVGIcon {

    private String colorKey;


    public SVGIconUIColor(String name, float scale, String colorKey) {
        super(name, scale);
        this.colorKey = colorKey;
        setColorFilter(new ColorFilter(color -> {
            Color uiColor = UIManager.getColor(getColorKey());
            if (uiColor != null) {
                return uiColor;
            }
            return color;
        }));
    }

    public String getColorKey() {
        return colorKey;
    }

    public void setColorKey(String colorKey) {
        this.colorKey = colorKey;
    }
}
