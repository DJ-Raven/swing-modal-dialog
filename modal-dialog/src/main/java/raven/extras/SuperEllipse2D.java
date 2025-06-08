package raven.extras;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * This concepts from "Tom White"
 * Link here "https://github.com/tomwhite/superellipse"
 *
 * @author Raven
 */
public class SuperEllipse2D {
    private final double x, y, width, height, eccentricity;

    private GeneralPath path;

    public SuperEllipse2D(double x, double y, double width, double height, double eccentricity) {
        if (eccentricity < 0.0) {
            throw new IllegalArgumentException("eccentricity must be between 0 and infinity");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.eccentricity = eccentricity;
        initialise();
    }

    private void initialise() {
        path = new GeneralPath();
        if (eccentricity == Double.POSITIVE_INFINITY) {
            // special case
            path.moveTo((float) x, (float) y);
            path.lineTo((float) x, (float) (y + height));
            path.lineTo((float) (x + width), (float) (y + height));
            path.lineTo((float) (x + width), (float) y);
            path.closePath();
        } else {

            double halfWidth = width / 2.0;
            double halfHeight = height / 2.0;

            double centreX = x + halfWidth;
            double centreY = y + halfHeight;

            double TWO_PI = Math.PI * 2.0;
            int resolution = 100;

            path.moveTo((float) (x + width), (float) centreY);

            for (double theta = 0.0; theta < TWO_PI; theta += TWO_PI / resolution) {
                double sineTheta = Math.sin(theta);
                double cosineTheta = Math.cos(theta);
                double r =
                        Math.pow(1 / (Math.pow(Math.abs(cosineTheta) / halfWidth, eccentricity) + Math.pow(Math.abs(sineTheta) / halfHeight, eccentricity)),
                                1 / eccentricity);
                path.lineTo((float) (centreX + r * cosineTheta), (float) (centreY + r * sineTheta));
            }
        }
    }

    public Shape getShape() {
        return path;
    }
}
