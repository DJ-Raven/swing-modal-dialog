package raven.modal.slider;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.ScaledEmptyBorder;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.component.ModalContainer;
import raven.modal.layout.AnimatedLayout;

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
    private AnimatedLayout animatedLayout;
    private ModalContainer modalContainer;
    private float roundBorder;

    public PanelSlider(ModalContainer modalContainer, int border, float roundBorder) {
        this.modalContainer = modalContainer;
        this.roundBorder = roundBorder;
        init(border);
    }

    private void init(int border) {
        panelSnapshot = new PanelSnapshot();
        animatedLayout = new AnimatedLayout(modalContainer);
        setBorder(new ScaledEmptyBorder(border, 0, border, 0));
        setLayout(animatedLayout);
        setLayer(panelSnapshot, JLayeredPane.MODAL_LAYER);
        add(panelSnapshot);
    }

    public void addSlide(Component component, SliderTransition transition) {
        addSlide(component, transition, 400);
    }

    public void addSlide(Component component, SliderTransition transition, int duration) {
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
                Dimension fromSize = modalContainer.getModalComponentSize(oldComponent, this);
                Dimension targetSize = modalContainer.getModalComponentSize(slideComponent, this);
                animatedLayout.addAnimateSize(fromSize, targetSize);
                doLayout();
                SwingUtilities.invokeLater(() -> {
                    Image oldImage = createImage(oldComponent, fromSize);
                    Image newImage = createImage(component, targetSize);
                    remove(oldComponent);
                    panelSnapshot.animate(component, transition, oldImage, newImage, duration);
                });
            } else {
                component.setVisible(true);
                remove(oldComponent);
                doLayout();
                repaint();
            }
        }
    }

    private Image createImage(Component component, Dimension size) {
        VolatileImage snapshot = component.createVolatileImage(size.width, size.height);
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

    public class PanelSnapshot extends JComponent {

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
                    animatedLayout.setAnimate(animate);
                    if (animatedLayout.isUpdateAble()) {
                        PanelSlider.this.revalidate();
                    }
                }

                @Override
                public void end() {
                    setVisible(false);
                    component.setVisible(true);
                    animatedLayout.reset();
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

        protected void animate(Component component, SliderTransition sliderTransition, Image oldImage, Image newImage, int duration) {
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
            animator.setDuration(duration);
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
