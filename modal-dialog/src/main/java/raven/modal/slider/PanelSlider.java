package raven.modal.slider;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
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

    public boolean isRequestFocusAfterSlide() {
        return requestFocusAfterSlide;
    }

    public void setRequestFocusAfterSlide(boolean requestFocusAfterSlide) {
        this.requestFocusAfterSlide = requestFocusAfterSlide;
    }

    public boolean isUseSlideAsBackground() {
        return useSlideAsBackground;
    }

    public void setUseSlideAsBackground(boolean useSlideAsBackground) {
        this.useSlideAsBackground = useSlideAsBackground;
    }

    private final PaneSliderLayoutSize paneSliderLayoutSize;
    private PanelSnapshot panelSnapshot;
    private Component slideComponent;
    private AnimatedLayout animatedLayout;
    private boolean requestFocusAfterSlide;
    private boolean useSlideAsBackground;

    public PanelSlider(PaneSliderLayoutSize paneSliderLayoutSize) {
        this.paneSliderLayoutSize = paneSliderLayoutSize;
        init();
    }

    private void init() {
        panelSnapshot = new PanelSnapshot();
        animatedLayout = new AnimatedLayout(paneSliderLayoutSize);
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
                Dimension fromSize = paneSliderLayoutSize.getComponentSize(this, oldComponent);
                Dimension targetSize = paneSliderLayoutSize.getComponentSize(this, slideComponent);
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
    public Color getBackground() {
        if (useSlideAsBackground == false || slideComponent == null) {
            return super.getBackground();
        }
        return slideComponent.getBackground();
    }

    public interface PaneSliderLayoutSize {
        Dimension getComponentSize(Container container, Component component);
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
                    if (isRequestFocusAfterSlide()) {
                        slideComponent.requestFocus();
                    }
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
            component = null;
            sliderTransition = null;
            oldImage = null;
            newImage = null;
        }
    }
}
