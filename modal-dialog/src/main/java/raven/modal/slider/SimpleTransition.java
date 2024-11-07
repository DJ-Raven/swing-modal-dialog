package raven.modal.slider;

import java.awt.*;

/**
 * @author Raven
 */
public class SimpleTransition {

    public static SliderTransition get(SliderType sliderType) {
        SliderTransition transition = null;
        if (sliderType == SliderType.DEFAULT) {
            transition = new SliderTransition() {

                @Override
                public void renderImageNew(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setComposite(AlphaComposite.SrcOver.derive(animate));
                    g.drawImage(image, 0, 0, null);
                    g.dispose();
                }

                @Override
                public void renderImageOld(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f - animate));
                    g.drawImage(image, 0, 0, null);
                    g.dispose();
                }
            };
        } else if (sliderType == SliderType.BACK) {
            transition = new SliderTransition() {

                @Override
                public void renderImageNew(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setComposite(AlphaComposite.SrcOver.derive(animate));
                    int x = -(int) (width * (1f - animate));
                    g.drawImage(image, x, 0, null);
                    g.dispose();
                }

                @Override
                public void renderImageOld(Graphics g, Image image, int width, int height, float animate) {
                    int x = (int) (width * animate);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f - animate));
                    g.drawImage(image, x, 0, null);
                    g.dispose();
                }
            };
        } else if (sliderType == SliderType.FORWARD) {
            transition = new SliderTransition() {

                @Override
                public void renderImageNew(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setComposite(AlphaComposite.SrcOver.derive(animate));
                    int x = (int) (width - width * animate);
                    g.drawImage(image, x, 0, null);
                    g.dispose();
                }

                @Override
                public void renderImageOld(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f - animate));
                    int x = -(int) (width * animate);
                    g.drawImage(image, x, 0, null);
                    g.dispose();
                }
            };
        } else if (sliderType == SliderType.ZOOM_IN) {
            transition = new SliderTransition() {

                @Override
                public void renderImageNew(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setComposite(AlphaComposite.SrcOver.derive(animate));
                    g.drawImage(image, 0, 0, null);
                    g2.dispose();
                }

                @Override
                public void renderImageOld(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    float sx = 1f - animate;
                    float sy = 1f - animate;
                    float x = width / 2f;
                    float y = height / 2f;
                    g2.translate(x, y);
                    g2.scale(sx, sy);
                    g2.translate(-x, -y);
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f - animate));
                    g.drawImage(image, 0, 0, null);
                    g2.dispose();
                }
            };
        } else if (sliderType == SliderType.ZOOM_OUT) {
            transition = new SliderTransition() {

                @Override
                public void renderImageNew(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setComposite(AlphaComposite.SrcOver.derive(animate));
                    g.drawImage(image, 0, 0, null);
                    g2.dispose();
                }

                @Override
                public void renderImageOld(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    float sx = 1f + animate;
                    float sy = 1f + animate;
                    float x = width / 2f;
                    float y = height / 2f;
                    g2.translate(x, y);
                    g2.scale(sx, sy);
                    g2.translate(-x, -y);
                    g2.setComposite(AlphaComposite.SrcOver.derive(1f - animate));
                    g.drawImage(image, 0, 0, null);
                    g2.dispose();
                }
            };
        } else if (sliderType == SliderType.TOP_DOWN) {
            transition = new SliderTransition() {

                @Override
                public void renderImageNew(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    int y = (int) (-height + height * animate);
                    g.drawImage(image, 0, y, null);
                    g2.dispose();
                }

                @Override
                public void renderImageOld(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    g.drawImage(image, 0, 0, null);
                    g2.dispose();
                }
            };
        } else if (sliderType == SliderType.DOWN_TOP) {
            transition = new SliderTransition() {

                @Override
                public void renderImageNew(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    g.drawImage(image, 0, 0, null);
                    g2.dispose();
                }

                @Override
                public void renderImageOld(Graphics g, Image image, int width, int height, float animate) {
                    Graphics2D g2 = (Graphics2D) g;
                    int y = (int) (-height * animate);
                    g.drawImage(image, 0, y, null);
                    g2.dispose();
                }

                @Override
                public void render(Graphics g, Image imageOld, Image imageNew, int width, int height, float animate) {
                    renderImageNew(g.create(), imageNew, width, height, animate);
                    renderImageOld(g.create(), imageOld, width, height, animate);
                }
            };
        }
        return transition;
    }

    public enum SliderType {
        DEFAULT, BACK, FORWARD, ZOOM_IN, ZOOM_OUT, TOP_DOWN, DOWN_TOP
    }
}
