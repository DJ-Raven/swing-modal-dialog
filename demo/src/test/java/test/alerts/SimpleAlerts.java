package test.alerts;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.Graphics2DProxy;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.SimpleModalBorder;
import raven.modal.listener.ModalCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class SimpleAlerts extends SimpleModalBorder {

    public SimpleAlerts(Component component, String title, AlertsOption alertsOption, int actionType, ModalCallback callback) {
        super(new PanelEffect(component, alertsOption), title, actionType, callback);
    }

    protected static class PanelEffect extends JPanel {

        private AlertsOption alertsOption;
        private Effect effects[];
        private float animate;
        private Animator animator;
        private JLabel labelIcon;
        private Component component;
        private AnimateIcon animateIcon;
        private MigLayout layout;

        protected void start() {
            if (animator == null) {
                animator = new Animator(2000, new Animator.TimingTarget() {
                    @Override
                    public void timingEvent(float v) {
                        animate = v;
                        if (animateIcon != null) {
                            animateIcon.setAnimate(v);
                        }
                        repaint();
                    }

                    @Override
                    public void end() {
                        if (alertsOption.loopAnimation && isShowing()) {
                            SwingUtilities.invokeLater(() -> {
                                startAnimation();
                            });
                        }
                    }
                });
                animator.setInterpolator(CubicBezierEasing.EASE);
            }
            if (animator.isRunning()) {
                animator.stop();
            }
            animator.start();
        }

        private void startAnimation() {
            createEffect();
            start();
        }

        private void createEffect() {
            if (alertsOption.effectOption != null) {
                effects = new Effect[30];
                for (int i = 0; i < effects.length; i++) {
                    effects[i] = new Effect(alertsOption.effectOption.randomEffect);
                }
            }
        }

        public PanelEffect(Component component, AlertsOption alertsOption) {
            this.component = component;
            this.alertsOption = alertsOption;
            layout = new MigLayout("fillx,wrap,insets 0", "[fill,center]", "0[]3[]20[]5");
            setLayout(layout);
            if (alertsOption.icon instanceof AnimateIcon) {
                animateIcon = (AnimateIcon) alertsOption.icon;
            }
            labelIcon = new JLabel(alertsOption.icon);
            labelIcon.putClientProperty(FlatClientProperties.STYLE, "" +
                    "border:25,5,10,5");
            add(labelIcon);
            add(component);
            startAnimation();
        }

        @Override
        protected void paintChildren(Graphics g) {
            if (alertsOption.effectOption != null && effects != null && effects.length > 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                FlatUIUtils.setRenderingHints(g2);
                int width = getWidth();
                int height = getHeight();
                int x = labelIcon.getX() + labelIcon.getWidth() / 2;
                int y = labelIcon.getY() + labelIcon.getHeight() / 2;
                float size = Math.min(width, height) / 2;

                float l = size * animate;

                g2.translate(x, y);

                for (int i = 0; i < effects.length; i++) {
                    Effect effect = effects[i];
                    double sp = effect.speed * 0.05f;
                    double xx = Math.cos(Math.toRadians(effect.direction)) * l * sp;
                    double yy = Math.sin(Math.toRadians(effect.direction)) * l * sp;
                    AffineTransform oldTran = g2.getTransform();
                    Icon icon = alertsOption.effectOption.randomEffect[effect.effectIndex];
                    int iw = icon.getIconWidth() / 2;
                    int ih = icon.getIconHeight() / 2;
                    xx -= iw;
                    yy -= ih;
                    g2.translate(xx, yy);
                    g2.rotate(Math.toRadians(animate * 360), iw, ih);
                    if (alertsOption.effectOption.effectAlpha < 1f) {
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alertsOption.effectOption.effectAlpha));
                    }
                    if (alertsOption.effectOption.effectFadeOut) {
                        float remove = 0.7f;
                        if (animate >= remove) {
                            float f = ((animate - remove) / (1f - remove)) * alertsOption.effectOption.effectAlpha;
                            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alertsOption.effectOption.effectAlpha - f));
                        }
                    }
                    icon.paintIcon(null, new GraphicsColorFilter(g2, effect.color), 0, 0);
                    g2.setTransform(oldTran);
                }
                g2.dispose();
            }
            super.paintChildren(g);
        }
    }

    protected static class Effect {

        protected int effectIndex;
        protected Color color;
        protected float direction;
        protected float speed;

        public Effect(Icon[] randomEffect) {
            Random random = new Random();
            color = effectColors[random.nextInt(effectColors.length)];
            if (randomEffect != null && randomEffect.length > 0) {
                effectIndex = random.nextInt(randomEffect.length);
            }
            direction = random.nextInt(360);
            speed = random.nextInt(25) + 5;
        }
    }

    protected static final Color effectColors[] = {
            Color.decode("#f43f5e"), Color.decode("#6366f1"),
            Color.decode("#ec4899"), Color.decode("#3b82f6"),
            Color.decode("#d946ef"), Color.decode("#0ea5e9"),
            Color.decode("#a855f7"), Color.decode("#06b6d4"),
            Color.decode("#8b5cf6"), Color.decode("#14b8a6"),
            Color.decode("#10b981"), Color.decode("#22c55e"),
            Color.decode("#84cc16"), Color.decode("#eab308"),
            Color.decode("#f59e0b"), Color.decode("#f97316"),
            Color.decode("#ef4444"), Color.decode("#78716c"),
            Color.decode("#737373"), Color.decode("#71717a"),
            Color.decode("#6b7280"), Color.decode("#64748b")
    };

    private static class GraphicsColorFilter extends Graphics2DProxy {

        protected Color color;

        public GraphicsColorFilter(Graphics2D delegate, Color color) {
            super(delegate);
            this.color = color;
        }

        @Override
        public Graphics create() {
            return new GraphicsColorFilter((Graphics2D) super.create(), this.color);
        }

        @Override
        public void setPaint(Paint paint) {
            super.setPaint(color);
        }
    }
}
