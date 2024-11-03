package raven.modal.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.VolatileImage;

/**
 * @author Raven
 */
public class BlurImageUtils {

    public static BufferedImage applyGaussianBlur(VolatileImage image, int radius) {
        BufferedImage bufferedImage = toBufferedImage(image);
        image.flush();
        return applyGaussianBlur(bufferedImage, radius);
    }

    /**
     * Applies a separable gaussian blur by blurring horizontally and vertically in two passes
     */
    public static BufferedImage applyGaussianBlur(BufferedImage image, int radius) {
        // generate a 1D gaussian kernel
        float[] kernelData = createGaussianKernel(radius);

        // blur horizontally
        BufferedImage tempImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        ConvolveOp horizontalBlur = new ConvolveOp(new Kernel(kernelData.length, 1, kernelData));
        horizontalBlur.filter(image, tempImage);

        // blur vertically
        BufferedImage blurredImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        ConvolveOp verticalBlur = new ConvolveOp(new Kernel(1, kernelData.length, kernelData));
        verticalBlur.filter(tempImage, blurredImage);

        image.flush();
        return blurredImage;
    }

    /**
     * Method to create BufferedImage from VolatileImage
     */
    public static BufferedImage toBufferedImage(VolatileImage vImage) {
        BufferedImage bImage = new BufferedImage(vImage.getWidth(), vImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bImage.createGraphics();
        g2d.drawImage(vImage, 0, 0, null);
        g2d.dispose();
        return bImage;
    }

    /**
     * Generates a 1D gaussian kernel with a specified radius
     */
    private static float[] createGaussianKernel(int radius) {
        int size = radius * 2 + 1;
        float[] kernel = new float[size];
        float sigma = radius / 2.0f;
        float sum = 0f;

        for (int i = 0; i < size; i++) {
            int x = i - radius;
            kernel[i] = (float) Math.exp(-0.5 * (x * x) / (sigma * sigma));
            sum += kernel[i];
        }

        // normalize the kernel so the sum is 1
        for (int i = 0; i < size; i++) {
            kernel[i] /= sum;
        }
        return kernel;
    }
}
