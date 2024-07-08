package test.alerts;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import test.alerts.animator.EasingInterpolator;
import test.alerts.animator.KeyFrames;

import javax.swing.*;
import java.awt.*;

public class AlertsOption {

    protected Icon icon;
    protected Color baseColor;

    protected boolean loopAnimation;

    protected EffectOption effectOption;

    public AlertsOption(Icon icon, Color baseColor) {
        this.icon = icon;
        this.baseColor = baseColor;
    }

    public AlertsOption setEffectOption(EffectOption effectOption) {
        this.effectOption = effectOption;
        return this;
    }

    public AlertsOption setLoopAnimation(boolean loopAnimation) {
        this.loopAnimation = loopAnimation;
        return this;
    }

    public static class EffectOption {

        protected float effectAlpha = 1f;
        protected boolean effectFadeOut = false;
        protected Icon[] randomEffect;

        public EffectOption setEffectAlpha(float effectAlpha) {
            this.effectAlpha = effectAlpha;
            return this;
        }

        public EffectOption setEffectFadeOut(boolean effectFadeOut) {
            this.effectFadeOut = effectFadeOut;
            return this;
        }

        public EffectOption setRandomEffect(Icon[] randomEffect) {
            this.randomEffect = randomEffect;
            return this;
        }
    }

    public static AlertsOption getAlertsOption(Type messageType) {
        if (messageType == Type.SUCCESS) {
            Icon effects[] = new Icon[]{
                    new FlatSVGIcon("raven/alerts/effect/check.svg"),
                    new FlatSVGIcon("raven/alerts/effect/star.svg"),
                    new FlatSVGIcon("raven/alerts/effect/firework.svg"),
                    new FlatSVGIcon("raven/alerts/effect/balloon.svg")
            };
            return getDefaultOption("raven/alerts/icon/success.svg", Color.decode("#10b981"), effects);
        } else if (messageType == Type.WARNING) {
            Icon effects[] = new Icon[]{
                    new FlatSVGIcon("raven/alerts/effect/disclaimer.svg"),
                    new FlatSVGIcon("raven/alerts/effect/warning.svg"),
                    new FlatSVGIcon("raven/alerts/effect/query.svg"),
                    new FlatSVGIcon("raven/alerts/effect/mark.svg")
            };
            return getDefaultOption("raven/alerts/icon/warning.svg", Color.decode("#f59e0b"), effects);
        } else if (messageType == Type.ERROR) {
            Icon effects[] = new Icon[]{
                    new FlatSVGIcon("raven/alerts/effect/error.svg"),
                    new FlatSVGIcon("raven/alerts/effect/sad.svg"),
                    new FlatSVGIcon("raven/alerts/effect/shield.svg"),
                    new FlatSVGIcon("raven/alerts/effect/nothing.svg")
            };
            return getDefaultOption("raven/alerts/icon/error.svg", Color.decode("#ef4444"), effects);
        } else {
            return getDefaultOption("raven/alerts/icon/information.svg", null);
        }
    }

    private static AlertsOption getDefaultOption(String icon, Color color, Icon[] effects) {
        AnimateIcon.AnimateOption option = new AnimateIcon.AnimateOption()
                .setInterpolator(EasingInterpolator.EASE_OUT_BOUNCE)
                .setScaleInterpolator(new KeyFrames(1f, 1.5f, 1f))
                .setRotateInterpolator(new KeyFrames(0f, (float) Math.toRadians(-30f), 0f));
        return new AlertsOption(new AnimateIcon(icon, 4f, option), color)
                .setEffectOption(new EffectOption()
                        .setEffectAlpha(0.9f)
                        .setEffectFadeOut(true)
                        .setRandomEffect(effects))
                .setLoopAnimation(true);
    }

    public static AlertsOption getDefaultOption(String icon, Color color) {
        AnimateIcon.AnimateOption option = new AnimateIcon.AnimateOption()
                .setScaleInterpolator(new KeyFrames(1f, 1.2f, 1f))
                .setRotateInterpolator(new KeyFrames(0f, (float) Math.toRadians(-30), (float) Math.toRadians(30), 0f));
        return new AlertsOption(new AnimateIcon(icon, 4f, option), color)
                .setLoopAnimation(true);
    }

    public enum Type {
        SUCCESS, WARNING, ERROR
    }
}
