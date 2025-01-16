package raven.modal.component;

import raven.modal.layout.RelativeLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Raven
 */
public abstract class AbstractRelativeContainer {

    protected final JLayeredPane layeredPane;
    protected final List<RelativeLayerPane> listLayer;
    protected final LayoutManager layerLayout;
    private RelativeLayerPane.LayoutCallback layoutCallback;

    public AbstractRelativeContainer(LayoutManager layerLayout) {
        this.layerLayout = layerLayout;
        layeredPane = new JLayeredPane();
        listLayer = new ArrayList<>();
        layeredPane.setLayout(new RelativeLayout());
    }

    protected void setLayoutCallback(RelativeLayerPane.LayoutCallback callback) {
        this.layoutCallback = callback;
    }

    protected RelativeLayerPane getLayer(Component owner, boolean controlVisibility, boolean fixedLayout) {
        for (int i = 0; i < listLayer.size(); i++) {
            RelativeLayerPane l = listLayer.get(i);
            if (l.getOwner() == owner
                    && l.isControlVisibility() == controlVisibility
                    && l.isFixedLayout() == fixedLayout
            ) {
                return l;
            }
        }
        return null;
    }

    protected void removeLayer(Component component, Component owner, boolean controlVisibility, boolean fixedLayout) {
        RelativeLayerPane layer = getLayer(owner, controlVisibility, fixedLayout);
        if (layer != null) {
            layer.remove(component);
            if (layer.getComponentCount() == 0) {
                layeredPane.remove(layer);
                listLayer.remove(layer);
            }
            layer.repaint();
            layer.revalidate();
        }
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    protected RelativeLayerPane getLayerAndCreate(Component owner, boolean controlVisibility, boolean fixedLayout) {
        RelativeLayerPane layer = getLayer(owner, controlVisibility, fixedLayout);
        if (layer == null) {
            layer = new RelativeLayerPane(owner, controlVisibility, fixedLayout, layoutCallback);
            layer.setComponentOrientation(layeredPane.getComponentOrientation());
            layer.setLayout(layerLayout);
            listLayer.add(layer);
            layeredPane.add(layer, JLayeredPane.PALETTE_LAYER, 0);
        }
        return layer;
    }

    public void setEnableHierarchy(boolean enable) {
        listLayer.forEach(layer -> layer.setEnableHierarchy(enable));
    }

    public void updateLayout(Component owner) {
        listLayer.forEach(layer -> {
            if (layer.getOwner() == owner) {
                layer.revalidate();
            }
        });
    }

    public void updateLayout() {
        layeredPane.revalidate();
        listLayer.forEach(layer -> layer.revalidate());
    }

    public void initComponentOrientation(ComponentOrientation orientation) {
        layeredPane.setComponentOrientation(orientation);
    }

    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }
}
