package raven.modal.demo.component.chart.utils;

import com.formdev.flatlaf.util.UIScale;
import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.text.TextUtils;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.util.Args;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;
import raven.modal.demo.component.chart.themes.ChartDrawingSupplier;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;

public class MultiXYTextAnnotation extends AbstractXYAnnotation {


    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        Args.nullNotPermitted(font, "font");
        this.font = font;
        fireAnnotationChanged();
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.paint = paint;
        fireAnnotationChanged();
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void setBackgroundPaint(Paint backgroundPaint) {
        this.backgroundPaint = backgroundPaint;
        fireAnnotationChanged();
    }

    public boolean isOutlineVisible() {
        return outlineVisible;
    }

    public void setOutlineVisible(boolean outlineVisible) {
        this.outlineVisible = outlineVisible;
        fireAnnotationChanged();
    }

    public Paint getOutlinePaint() {
        return outlinePaint;
    }

    public void setOutlinePaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.outlinePaint = paint;
        fireAnnotationChanged();
    }

    public Paint getGridLinePaint() {
        return gridLinePaint;
    }

    public void setGridLinePaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.gridLinePaint = paint;
        fireAnnotationChanged();
    }

    public Paint getTitlePaint() {
        return titlePaint;
    }

    public void setTitlePaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.titlePaint = paint;
        fireAnnotationChanged();
    }

    public Paint getValuePaint() {
        return valuePaint;
    }

    public void setValuePaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.valuePaint = paint;
        fireAnnotationChanged();
    }

    public Paint getTitleLinePain() {
        return titleLinePain;
    }

    public void setTitleLinePain(Paint paint) {
        this.titleLinePain = paint;
        fireAnnotationChanged();
    }

    public Stroke getOutlineStroke() {
        return outlineStroke;
    }

    public void setOutlineStroke(Stroke stroke) {
        Args.nullNotPermitted(stroke, "stroke");
        this.outlineStroke = stroke;
        fireAnnotationChanged();
    }

    public Stroke getTitleLineStroke() {
        return titleLineStroke;
    }

    public void setTitleLineStroke(Stroke stroke) {
        Args.nullNotPermitted(stroke, "stroke");
        this.titleLineStroke = stroke;
        fireAnnotationChanged();
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
        fireAnnotationChanged();
    }

    public double getVerticalTextGap() {
        return verticalTextGap;
    }

    public void setVerticalTextGap(double verticalTextGap) {
        this.verticalTextGap = verticalTextGap;
        fireAnnotationChanged();
    }

    public RectangleInsets getPadding() {
        return padding;
    }

    public void setPadding(RectangleInsets padding) {
        Args.nullNotPermitted(padding, "padding");
        this.padding = padding;
        fireAnnotationChanged();
    }

    public RectangleInsets getSeriesPadding() {
        return seriesPadding;
    }

    public void setSeriesPadding(RectangleInsets padding) {
        Args.nullNotPermitted(padding, "padding");
        this.seriesPadding = padding;
        fireAnnotationChanged();
    }

    public RectangleInsets getTitlePadding() {
        return titlePadding;
    }

    public void setTitlePadding(RectangleInsets padding) {
        Args.nullNotPermitted(padding, "padding");
        this.titlePadding = padding;
        fireAnnotationChanged();
    }

    public RectangleInsets getTitleLinePadding() {
        return titleLinePadding;
    }

    public void setTitleLinePadding(RectangleInsets padding) {
        Args.nullNotPermitted(padding, "padding");
        this.titleLinePadding = padding;
        fireAnnotationChanged();
    }

    public double getRound() {
        return round;
    }

    public void setRound(double round) {
        this.round = round;
        fireAnnotationChanged();
    }

    public double getSeriesSize() {
        return seriesSize;
    }

    public void setSeriesSize(double seriesSize) {
        this.seriesSize = seriesSize;
        fireAnnotationChanged();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        fireAnnotationChanged();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        fireAnnotationChanged();
    }

    public Label[] getLabels() {
        return labels;
    }

    public void setLabels(Label[] labels) {
        if (this.labels != labels) {
            this.labels = labels;
            fireAnnotationChanged();
        }
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        Args.nullNotPermitted(numberFormat, "numberFormat");
        this.numberFormat = numberFormat;
        fireAnnotationChanged();
    }

    public TitleGenerator getTitleGenerator() {
        return titleGenerator;
    }

    public void setTitleGenerator(TitleGenerator titleGenerator) {
        this.titleGenerator = titleGenerator;
        fireAnnotationChanged();
    }

    public void setDefaultPaint(Paint paint) {
        Args.nullNotPermitted(paint, "paint");
        this.paint = paint;
        this.titlePaint = paint;
        this.valuePaint = paint;
        fireAnnotationChanged();
    }

    private Font font;
    private Paint paint;
    private Paint backgroundPaint;
    private boolean outlineVisible;
    private Paint outlinePaint;
    private Paint gridLinePaint;
    private Paint titlePaint;
    private Paint valuePaint;
    private Paint titleLinePain;
    private Stroke outlineStroke;
    private Stroke titleLineStroke;
    private double gap;
    private double verticalTextGap;
    private RectangleInsets padding;
    private RectangleInsets seriesPadding;
    private RectangleInsets titlePadding;
    private RectangleInsets titleLinePadding;
    private double round;
    private double seriesSize;
    private double x;
    private double y;
    private Label[] labels;
    private NumberFormat numberFormat;
    private TitleGenerator titleGenerator;

    public MultiXYTextAnnotation() {
        this.paint = XYTextAnnotation.DEFAULT_PAINT;
        this.font = XYTextAnnotation.DEFAULT_FONT;
        this.backgroundPaint = new Color(255, 255, 255, 200);
        this.outlinePaint = new Color(190, 190, 190);
        this.gridLinePaint = new Color(25, 104, 148);
        this.titlePaint = XYTextAnnotation.DEFAULT_PAINT;
        this.valuePaint = XYTextAnnotation.DEFAULT_PAINT;
        this.titleLinePain = new Color(190, 190, 190);
        this.outlineVisible = true;
        this.numberFormat = NumberFormat.getNumberInstance();
        this.padding = new RectangleInsets(10, 10, 10, 10);
        this.seriesPadding = new RectangleInsets(3, 2, 3, 5);
        this.titlePadding = new RectangleInsets(0, 0, 8, 0);
        this.titleLinePadding = new RectangleInsets(0, 0, 8, 0);
        this.outlineStroke = new BasicStroke(0.5f);
        this.titleLineStroke = new BasicStroke(0.5f);
        this.round = 10;
        this.seriesSize = 10;
        this.gap = 10;
        this.verticalTextGap = 5;
    }

    public void autoCalculateX(double x, XYDataset dataset) {
        if (this.x != x || labels == null) {
            this.x = x;
            createValues(dataset);
        }
    }

    private void createValues(XYDataset dataset) {
        int seriesCount = dataset.getSeriesCount();
        Label[] labels = new Label[seriesCount];
        double value = DatasetUtils.findYValue(dataset, 0, this.x);
        double closestY = value;
        labels[0] = new Label(dataset.getSeriesKey(0).toString(), getNumberFormat().format(value));
        for (int i = 1; i < seriesCount; i++) {
            double y = DatasetUtils.findYValue(dataset, i, this.x);
            String v = getNumberFormat().format(y);
            String t = dataset.getSeriesKey(i).toString();
            labels[i] = new Label(t, v);
            closestY = Math.max(closestY, y);
        }
        y = closestY;
        this.labels = labels;
        fireAnnotationChanged();
    }

    @Override
    public void draw(Graphics2D g, XYPlot plot, Rectangle2D dataArea, ValueAxis domainAxis, ValueAxis rangeAxis, int rendererIndex, PlotRenderingInfo info) {
        Graphics2D g2 = (Graphics2D) g.create();
        if (labels != null) {
            UIScale.scaleGraphics(g2);
            PlotOrientation orientation = plot.getOrientation();
            RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(plot.getDomainAxisLocation(), orientation);
            RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(plot.getRangeAxisLocation(), orientation);

            float anchorX = UIScale.unscale((float) domainAxis.valueToJava2D(this.getX(), dataArea, domainEdge));
            float anchorY = UIScale.unscale((float) rangeAxis.valueToJava2D(this.getY(), dataArea, rangeEdge));

            if (orientation == PlotOrientation.HORIZONTAL) {
                float tempAnchor = anchorX;
                anchorX = anchorY;
                anchorY = tempAnchor;
            }
            Font font = getFont();
            g2.setFont(font.deriveFont(UIScale.unscale((float) font.getSize())));

            String title = getTitleGenerator() == null ? null : titleGenerator.getTitle(this.x);
            Rectangle2D bgRec = getBackgroundRectangle(g2, anchorX, anchorY, title);

            // adjust annotation
            int shadow = 5;
            int space = 2;
            Rectangle2D rec = ChartDrawingSupplier.scaleRectangleInsets(space, space, space + shadow, space + shadow).createInsetRectangle(dataArea);

            Rectangle2D bgRecScale = ChartDrawingSupplier.scaleRectangle(bgRec);
            if (!rec.contains(bgRecScale)) {
                double x = bgRecScale.getX();
                double y = bgRecScale.getY();
                double width = bgRecScale.getWidth();
                double height = bgRecScale.getHeight();
                if (x < rec.getX()) {
                    x = rec.getX();
                } else if (x + width > rec.getX() + rec.getWidth()) {
                    x = rec.getX() + rec.getWidth() - width;
                }
                if (y < rec.getY()) {
                    y = rec.getY();
                } else if (y + height > rec.getY() + rec.getHeight()) {
                    y = rec.getY() + rec.getHeight() - height;
                }
                bgRec = ChartDrawingSupplier.unscaleRectangle(new Rectangle2D.Double(x, y, width, height));
            }
            Shape shape = round > 0 ?
                    new RoundRectangle2D.Double(bgRec.getX(), bgRec.getY(), bgRec.getWidth(), bgRec.getHeight(), round, round) :
                    bgRec;

            // draw domain grid line without scale ui
            g.setStroke(plot.getDomainGridlineStroke());
            g.setPaint(getGridLinePaint());
            double gx = UIScale.scale(anchorX);
            g.draw(new Line2D.Double(gx, dataArea.getY(), gx, dataArea.getY() + dataArea.getHeight()));

            if (getBackgroundPaint() != null) {
                g2.setPaint(getBackgroundPaint());
                g2.fill(shape);
            }
            float x = (float) (bgRec.getX() + getSeriesSizeWidth() + getPadding().getLeft());
            float y = (float) (bgRec.getY() + getPadding().getTop());
            float x2 = (float) (bgRec.getX() + bgRec.getWidth() - getPadding().getRight());
            float seriesX = (float) (bgRec.getX() + getPadding().getLeft());

            // draw title
            if (title != null) {
                y += getTitlePadding().getTop();
                float titleX = (float) (bgRec.getX() + getPadding().getLeft() + getTitlePadding().getLeft());
                y += drawTitle(g2, titleX, y, title);
                y += getTitlePadding().getBottom();
                if (getTitleLinePain() != null) {
                    float titleLineWidth = getTitleLineStrokeWidth();
                    g2.setStroke(getTitleLineStroke());
                    g2.setPaint(getTitleLinePain());
                    double lineWidth = bgRec.getWidth() - getTitleLinePadding().getLeft() - getTitleLinePadding().getRight();
                    double lineX = bgRec.getX() + getTitleLinePadding().getLeft();
                    y += getTitleLinePadding().getTop();
                    g2.draw(new Line2D.Double(lineX, y + titleLineWidth / 2f, lineX + lineWidth, y + titleLineWidth / 2f));
                    y += titleLineWidth + getTitleLinePadding().getBottom();
                }
            }

            // draw label
            for (int i = 0; i < labels.length; i++) {
                Label label = labels[i];
                float size = (float) drawLabel(g2, x, y, x2, label);
                drawSeries(g2, seriesX, y, size, plot.getRenderer().getSeriesPaint(i));
                y += size + getVerticalTextGap();
            }
            if (isOutlineVisible()) {
                g2.setStroke(getOutlineStroke());
                g2.setPaint(getOutlinePaint());
                g2.draw(shape);
            }
            String toolTip = getToolTipText();
            String url = getURL();
            if (toolTip != null || url != null) {
                addEntity(info, shape, rendererIndex, toolTip, url);
            }
        }
        g2.dispose();
    }

    protected double drawTitle(Graphics2D g2, float x, float y, String title) {
        g2.setPaint(getTitlePaint());
        double textHeight = TextUtils.drawAlignedString(title, g2, x, y, TextAnchor.TOP_LEFT).getHeight();
        return textHeight;
    }

    protected double drawLabel(Graphics2D g2, float x, float y, float x2, Label label) {
        g2.setPaint(getPaint());
        double textHeight = TextUtils.drawAlignedString(label.getText(), g2, x, y, TextAnchor.TOP_LEFT).getHeight();
        g2.setPaint(getValuePaint());
        double valueHeight = TextUtils.drawAlignedString(label.getValue(), g2, x2, y, TextAnchor.TOP_RIGHT).getHeight();
        return Math.max(textHeight, valueHeight);
    }

    protected void drawSeries(Graphics2D g2, float x, float y, float height, Paint paint) {
        double size = Math.min(height - (getSeriesPadding().getTop() + getSeriesPadding().getBottom()), getSeriesSize());
        double lx = x + (getSeriesSize() - size) / 2f;
        double ly = y + ((height - size) / 2);
        g2.setPaint(paint);
        g2.fill(new Ellipse2D.Double(lx, ly, size, size));
    }

    protected Rectangle2D getBackgroundRectangle(Graphics2D g2, float anchorX, float anchorY, String title) {
        if (labels == null || labels.length == 0) return null;

        double textWidth = 0, valueWidth = 0, titleWidth = 0, minLineWidth = 0, totalHeight = 0;
        FontMetrics fm = g2.getFontMetrics();
        for (int i = 0; i < labels.length; i++) {
            Label label = labels[i];
            Rectangle2D textBounds = TextUtils.getTextBounds(label.getText(), g2, fm);
            Rectangle2D valueBounds = TextUtils.getTextBounds(label.getValue(), g2, fm);
            totalHeight += Math.max(textBounds.getHeight(), valueBounds.getHeight());
            textWidth = Math.max(textWidth, textBounds.getWidth());
            valueWidth = Math.max(valueWidth, valueBounds.getWidth());
        }
        if (title != null) {
            Rectangle2D titleBounds = TextUtils.getTextBounds(title, g2, fm);
            titleWidth = titleBounds.getWidth() + getTitlePadding().getLeft() + getTitlePadding().getRight();
            totalHeight += titleBounds.getHeight();
            totalHeight += getTitlePadding().getTop() + getTitlePadding().getBottom();
            if (getTitleLinePain() != null) {
                totalHeight += getTitleLineStrokeWidth() + getTitleLinePadding().getTop() + getTitleLinePadding().getBottom();
                minLineWidth = getTitleLinePadding().getLeft() + getTitleLinePadding().getRight();
            }
        }
        if (labels.length > 1) {
            totalHeight += (getVerticalTextGap() * (labels.length - 1));
        }
        totalHeight += getPadding().getTop() + getPadding().getBottom();

        double totalLabelWidth = textWidth + valueWidth + getSeriesSizeWidth() + getGap();
        double totalWidth = Math.max(totalLabelWidth, Math.max(titleWidth, minLineWidth)) + getPadding().getLeft() + getPadding().getRight();
        double space = 10;
        return new Rectangle2D.Double(anchorX - space, anchorY - totalHeight - space, totalWidth, totalHeight);
    }

    private double getSeriesSizeWidth() {
        return getSeriesSize() + getSeriesPadding().getLeft() + getSeriesPadding().getRight();
    }

    private float getTitleLineStrokeWidth() {
        Stroke stroke = getTitleLineStroke();
        if (stroke instanceof BasicStroke) {
            float lineSize = ((BasicStroke) stroke).getLineWidth();
            return lineSize;
        }
        return 0;
    }

    public static class Label {

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Label(String text, String value) {
            this.text = text;
            this.value = value;
        }

        private String text;
        private String value;
    }

    public interface TitleGenerator {
        String getTitle(double xValue);
    }
}
