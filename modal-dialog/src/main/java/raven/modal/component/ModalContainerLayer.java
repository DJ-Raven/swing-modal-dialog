package raven.modal.component;

import com.formdev.flatlaf.ui.FlatTitlePane;
import raven.modal.drawer.DrawerLayoutResponsive;
import raven.modal.layout.FrameModalLayout;
import raven.modal.layout.FullContentLayout;
import raven.modal.utils.ModalUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowStateListener;
import java.awt.image.VolatileImage;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Raven
 */
public class ModalContainerLayer extends AbstractModalContainerLayer {

    private final Map<RootPaneContainer, ModalContainerLayer> map;
    private final RootPaneContainer rootPaneContainer;
    private final Consumer<Boolean> snapshotChanged;
    private Component componentSnapshot;
    private boolean isShowSnapshot;
    private JLayeredPane layeredSnapshot;
    private DrawerLayoutResponsive drawerLayoutResponsive;
    private PropertyChangeListener propertyListener;
    private WindowStateListener stateListener;

    public ModalContainerLayer(Map<RootPaneContainer, ModalContainerLayer> map, RootPaneContainer rootPaneContainer, Consumer<Boolean> snapshotChanged) {
        this.map = map;
        this.rootPaneContainer = rootPaneContainer;
        this.snapshotChanged = snapshotChanged;
    }

    @Override
    protected void animatedBegin() {
        try {
            snapshotChanged.accept(false);
            showSnapshot();
        } finally {
            snapshotChanged.accept(true);
        }
    }

    @Override
    protected void animatedEnd() {
        try {
            snapshotChanged.accept(false);
            hideSnapshot();
        } finally {
            snapshotChanged.accept(true);
        }
    }

    @Override
    public void showContainer(boolean show) {
        layeredPane.setVisible(show);
    }

    protected void showSnapshot() {
        if (isShowSnapshot) {
            return;
        }
        isShowSnapshot = true;
        Component contentPane = rootPaneContainer.getContentPane();
        VolatileImage snapshot = contentPane.createVolatileImage(rootPaneContainer.getLayeredPane().getWidth(), rootPaneContainer.getLayeredPane().getHeight());
        if (snapshot == null) {
            return;
        }
        boolean isFullWindowContent = ModalUtils.isFullWindowContent(rootPaneContainer.getRootPane());
        Graphics g = snapshot.createGraphics();
        int y = contentPane.getY();
        if (!isFullWindowContent) {
            y -= layeredPane.getY();
        }
        g.translate(contentPane.getX(), y);
        contentPane.paint(g);
        g.dispose();

        // paint drawer menu component that outside the contentPane to snapshot
        if (drawerLayoutResponsive != null && !drawerLayoutResponsive.isOpened() && drawerLayoutResponsive.isShowing()) {
            drawEmbedComponent(snapshot);
        }

        // if rootPane use `full window content` as true, paint flatlaf title to snapshot
        if (isFullWindowContent) {
            drawTitleBarToSnapshot(snapshot);
        }
        componentSnapshot = new JComponent() {
            @Override
            public void paint(Graphics g) {
                if (snapshot.contentsLost()) {
                    return;
                }
                g.drawImage(snapshot, 0, 0, null);
                g.dispose();
            }

            @Override
            public void removeNotify() {
                super.removeNotify();
                snapshot.flush();
            }
        };
        layeredSnapshot.setVisible(true);
        contentPane.setVisible(false);
        if (drawerLayoutResponsive != null && !drawerLayoutResponsive.isOpened()) {
            drawerLayoutResponsive.getDrawerPanel().setVisible(false);
        }
        layeredSnapshot.add(componentSnapshot);
    }

    protected void hideSnapshot() {
        layeredSnapshot.setVisible(false);
        rootPaneContainer.getContentPane().setVisible(true);
        if (drawerLayoutResponsive != null && !drawerLayoutResponsive.isOpened()) {
            drawerLayoutResponsive.getDrawerPanel().setVisible(true);
        }
        layeredSnapshot.remove(componentSnapshot);
        isShowSnapshot = false;
    }

    private void drawEmbedComponent(VolatileImage snapshot) {
        Graphics g = snapshot.createGraphics();
        Rectangle rec = drawerLayoutResponsive.getDrawerLayout(rootPaneContainer.getLayeredPane());
        g.translate(rec.x, rec.y);
        drawerLayoutResponsive.getDrawerPanel().paint(g);
        g.dispose();
    }

    private void drawTitleBarToSnapshot(VolatileImage snapshot) {
        for (Component com : rootPaneContainer.getLayeredPane().getComponents()) {
            if (com instanceof FlatTitlePane) {
                Graphics g = snapshot.createGraphics();
                g.translate(com.getX(), com.getY());
                com.paint(g);
                g.dispose();
            }
        }
    }

    public JLayeredPane createLayeredSnapshot() {
        if (layeredSnapshot == null) {
            layeredSnapshot = new JLayeredPane();
            layeredSnapshot.setVisible(false);
            layeredSnapshot.setLayout(new FullContentLayout());
        }
        return layeredSnapshot;
    }

    public void setDrawerLayoutResponsive(DrawerLayoutResponsive drawerLayoutResponsive) {
        this.drawerLayoutResponsive = drawerLayoutResponsive;
    }

    @Override
    public void remove(AbstractModalController controller) {
        super.remove(controller);
        if (containers.isEmpty()) {
            layeredPane.setVisible(false);
        }
    }

    public void remove() {
        closeAllModalImmediately();
        componentSnapshot = null;
        layeredSnapshot = null;
        drawerLayoutResponsive = null;
        propertyListener = null;
        stateListener = null;
    }

    public void installWindowListener() {
        JRootPane rootPane = rootPaneContainer.getRootPane();
        Window window = SwingUtilities.getWindowAncestor(rootPane);
        if (window != null) {
            stateListener = e -> {
                if (e.getNewState() == 6 || e.getNewState() == 0) {
                    SwingUtilities.invokeLater(() -> updateLayout());
                }
            };
            window.addWindowStateListener(stateListener);
        }
        propertyListener = evt -> {
            if (evt.getNewValue() == null && evt.getOldValue() instanceof RootPaneContainer) {
                uninstallWindowListener((RootPaneContainer) evt.getOldValue());
            }
        };
        rootPane.addPropertyChangeListener("ancestor", propertyListener);
    }

    public void uninstallWindowListener(RootPaneContainer rootPaneContainer) {
        if (map.containsKey(rootPaneContainer)) {
            JRootPane rootPane = rootPaneContainer.getRootPane();
            Window window = SwingUtilities.getWindowAncestor(rootPane);
            if (window != null) {
                window.removeWindowStateListener(stateListener);
            }
            JLayeredPane windowLayeredPane = rootPaneContainer.getLayeredPane();
            rootPane.removePropertyChangeListener("ancestor", propertyListener);

            // uninstall layout
            LayoutManager oldLayout = windowLayeredPane.getLayout();
            if (oldLayout != null) {
                if (oldLayout instanceof FrameModalLayout) {
                    FrameModalLayout frameModalLayout = (FrameModalLayout) oldLayout;
                    if (frameModalLayout.getOldOtherComponentLayout() != null) {
                        windowLayeredPane.setLayout(frameModalLayout.getOldOtherComponentLayout());
                    } else {
                        windowLayeredPane.setLayout(null);
                    }
                }
            }
            // remove
            map.remove(rootPaneContainer);
            windowLayeredPane.remove(layeredPane);
            if (componentSnapshot != null) {
                windowLayeredPane.remove(componentSnapshot);
            }
            remove();
        }
    }
}
