package raven.modal.demo.component.pagination;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.ScaledEmptyBorder;
import com.formdev.flatlaf.util.UIScale;
import raven.modal.utils.FlatLafStyleUtils;
import raven.swingpack.JPagination;
import raven.swingpack.pagination.DefaultPaginationItemRenderer;
import raven.swingpack.pagination.DefaultPaginationModel;
import raven.swingpack.pagination.Page;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class PaginationAnimation extends JPagination {

    private Animator animator;
    private float animate;

    private Rectangle2D fromRec;
    private Rectangle2D toRec;

    private boolean colorSet;
    private Color accentColor1;
    private Color accentColor2;

    public PaginationAnimation() {
        init();
    }

    public PaginationAnimation(int maxItem) {
        super(maxItem);
        init();
    }

    public PaginationAnimation(int selectedPage, int pageSize) {
        super(selectedPage, pageSize);
        init();
    }

    public PaginationAnimation(int maxItem, int selectedPage, int pageSize) {
        super(maxItem, selectedPage, pageSize);
        init();
    }

    private void init() {
        animator = new Animator(350, fraction -> {
            this.animate = fraction;
            repaint();
        });
        animator.setInterpolator(CubicBezierEasing.STANDARD_EASING);
        setItemRenderer(new DefaultPaginationItemRenderer() {

            private int index;

            @Override
            public Component getPaginationItemRendererComponent(JPagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
                this.index = index;
                super.getPaginationItemRendererComponent(pagination, page, isSelected, isPressed, hasFocus, index);
                if (index == -2) {
                    // for custom animation
                    setContentAreaFilled(false);
                } else {
                    setContentAreaFilled(true);
                    FlatLafStyleUtils.appendStyle(this, "" +
                            "background:$Panel.background;" +
                            "arc:18;" +
                            "borderWidth:0;" +
                            "focusWidth:0;");
                }
                return this;
            }

            @Override
            public boolean isDefaultButton() {
                return index == -2;
            }
        });
        setItemGap(0);
        getModel().addPaginationModelListener(event -> {
            if (animator.isRunning()) {
                animator.stop();
                fromRec = createRec(fromRec, toRec, animate);
            } else {
                fromRec = getRectangleOfIndex(event.getOldIndex());
            }
            toRec = getRectangleOfIndex(event.getNewIndex());
            animate = 0f;
            animator.start();
        });
        setBorder(new ScaledEmptyBorder(5, 5, 5, 5));
    }

    public void setColor(Color color1, Color color2) {
        colorSet = (color1 != null || color2 != null);

        if (!colorSet) {
            // use defaults
            accentColor1 = UIManager.getColor("Button.default.background");
            accentColor2 = UIManager.getColor("Component.accentColor");
            return;
        }

        if (color1 == null) color1 = color2;
        if (color2 == null) color2 = color1;

        if (color1.equals(color2)) {
            accentColor1 = color1;
            accentColor2 = null;
        } else {
            accentColor1 = color1;
            accentColor2 = color2;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getModel().getPageSize() == 0) {
            return;
        }
        Rectangle2D rec;
        if (animator.isRunning()) {
            rec = createRec(fromRec, toRec, animate);
        } else {
            rec = getRectangleOfIndex(((DefaultPaginationModel) getModel()).getIndexOf(getSelectedPage()));
        }
        if (rec != null) {
            paintSelected(g, rec, animate);
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (!colorSet) {
            accentColor1 = UIManager.getColor("Button.default.background");
            accentColor2 = UIManager.getColor("Component.accentColor");
            if (accentColor1.equals(accentColor2)) {
                accentColor2 = null;
            }
        }
    }

    private void paintSelected(Graphics g, Rectangle2D rec, float animate) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float arc = UIScale.scale(18);
        AffineTransform oldTran = g2.getTransform();
        float scale = 1f + customEase(animate) * 0.1f;
        double cx = rec.getCenterX();
        double cy = rec.getCenterY();

        g2.translate(cx - rec.getWidth() * scale / 2f, cy - rec.getHeight() * scale / 2f);
        g2.scale(scale, scale);

        if (accentColor1 != null && accentColor2 != null) {
            g2.setPaint(new GradientPaint(0, 0, accentColor1, (float) rec.getWidth(), (float) rec.getHeight(), accentColor2));
        } else {
            g2.setColor(accentColor1);
        }
        g2.fill(new RoundRectangle2D.Double(0, 0, rec.getWidth(), rec.getHeight(), arc, arc));
        g2.setTransform(oldTran);

        // paint selected page text
        Component newCom = getItemRenderer().getPaginationItemRendererComponent(this, new Page(getSelectedPage(), Page.Type.PAGE), false, false, false, -2);
        rendererPane.paintComponent(g2, newCom, this, rec.getBounds());
        g2.dispose();
    }

    private Rectangle getRectangleOfIndex(int index) {
        if (checkCreateNavigationButton(getModel().getPagination().length)) {
            index++;
        }
        return rectangleAt(index);
    }

    private Rectangle2D createRec(Rectangle2D recFrom, Rectangle2D recTo, float animated) {
        double diffX = recTo.getX() - recFrom.getX();
        double newX = recFrom.getX() + (diffX * animated);
        return new Rectangle2D.Double(newX, recFrom.getY(), recFrom.getWidth(), recFrom.getHeight());
    }

    private float customEase(float x) {
        // clamp between 0–1
        x = Math.max(0f, Math.min(1f, x));

        if (x <= 0.75f) {
            // from 0 → 0.75: ease-in-sine from 0 → 1
            float t = x / 0.75f;
            return (float) (1 - Math.cos((t * Math.PI) / 2f));
        } else {
            // from 0.75 → 1: ease-out-sine from 1 → 0
            float t = (x - 0.75f) / 0.25f;
            return (float) (Math.cos((t * Math.PI) / 2f));
        }
    }
}
