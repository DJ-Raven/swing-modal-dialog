package raven.modal.slider;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.ScaledEmptyBorder;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.image.VolatileImage;

/**
 * @author Raven
 */
public class PanelSlider extends JLayeredPane {

    public Component getSlideComponent() {
        return slideComponent;
    }

    private PanelSnapshot panelSnapshot;
    private Component slideComponent;
    private float roundBorder;

    public PanelSlider(int border, float roundBorder) {
        this.roundBorder = roundBorder;
        init(border);
    }

    private void init(int border) {
        panelSnapshot = new PanelSnapshot();
        setBorder(new ScaledEmptyBorder(border, 0, border, 0));
        setLayout(new CardLayout());
        setLayer(panelSnapshot, JLayeredPane.MODAL_LAYER);
        add(panelSnapshot);
    }

    public void addSlide(Component component, SliderTransition transition) {
        this.slideComponent = component;
        if (getComponentCount() == 1) {
            add(component);
            repaint();
            doLayout();
            panelSnapshot.setVisible(false);
            component.setVisible(true);
        } else {
            Component oldComponent = getComponent(1);
            add(component);
            if (transition != null) {
                doLayout();
                SwingUtilities.invokeLater(() -> {
                    Image oldImage = createImage(oldComponent);
                    Image newImage = createImage(component);
                    remove(oldComponent);
                    panelSnapshot.animate(component, transition, oldImage, newImage);
                });
            } else {
                component.setVisible(true);
                remove(oldComponent);
                doLayout();
                repaint();
            }
        }
    }

    private Image createImage(Component component) {
        VolatileImage snapshot = component.createVolatileImage(getWidth(), getHeight());
        if (snapshot != null) {
            component.paint(snapshot.getGraphics());
        }
        return snapshot;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (roundBorder > 0 && slideComponent != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            FlatUIUtils.setRenderingHints(g2);
            g2.setColor(slideComponent.getBackground());
            float arc = UIScale.scale(roundBorder);
            FlatUIUtils.paintComponentBackground(g2, 0, 0, getWidth(), getHeight(), 0, arc);
            g2.dispose();
        }
        super.paintComponent(g);
    }

    private class PanelSnapshot extends JComponent {

        private Animator animator;
        private Component component;
        private float animate;

        private SliderTransition sliderTransition;
        private Image oldImage;
        private Image newImage;

        public PanelSnapshot() {
            animator = new Animator(400, new Animator.TimingTarget() {
                @Override
                public void timingEvent(float v) {
                    animate = v;
                    repaint();
                }

                @Override
                public void end() {
                    setVisible(false);
                    component.setVisible(true);
                    PanelSlider.this.repaint();
                    if (newImage != null) {
                        newImage.flush();
                    }
                    if (oldImage != null) {
                        oldImage.flush();
                    }
                }
            });
            animator.setInterpolator(CubicBezierEasing.STANDARD_EASING);
        }

        protected void animate(Component component, SliderTransition sliderTransition, Image oldImage, Image newImage) {
            if (animator.isRunning()) {
                animator.stop();
            }
            this.component = component;
            this.oldImage = oldImage;
            this.newImage = newImage;
            this.sliderTransition = sliderTransition;
            this.animate = 0f;
            repaint();
            setVisible(true);
            animator.start();
        }

        @Override
        public void paint(Graphics g) {
            if (sliderTransition != null) {
                int width = getWidth();
                int height = getHeight();
                sliderTransition.render(g, oldImage, newImage, width, height, animate);
            }
        }

        @Override
        public void removeNotify() {
            super.removeNotify();
            animator = null;
            component = null;
            sliderTransition = null;
            oldImage = null;
            newImage = null;
        }
    }
}
