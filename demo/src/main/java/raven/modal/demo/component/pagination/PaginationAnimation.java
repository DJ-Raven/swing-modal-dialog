package raven.modal.demo.component.pagination;

import com.formdev.flatlaf.util.Animator;
import com.formdev.flatlaf.util.CubicBezierEasing;
import com.formdev.flatlaf.util.UIScale;
import raven.extras.pagination.DefaultPaginationItemRenderer;
import raven.extras.pagination.DefaultPaginationModel;
import raven.extras.pagination.Page;
import raven.extras.pagination.Pagination;
import raven.extras.pagination.event.PaginationModelEvent;
import raven.extras.pagination.event.PaginationModelListener;
import raven.modal.utils.FlatLafStyleUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class PaginationAnimation extends Pagination {

    private Animator animator;
    private float animate;

    private Rectangle2D fromRec;
    private Rectangle2D toRec;
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
            public Component getPaginationItemRendererComponent(Pagination pagination, Page page, boolean isSelected, boolean isPressed, boolean hasFocus, int index) {
                this.index = index;
                super.getPaginationItemRendererComponent(pagination, page, isSelected, isPressed, hasFocus, index);
                if (index == -2) {
                    // for custom animation
                    setContentAreaFilled(false);
                } else {
                    setContentAreaFilled(true);
                    FlatLafStyleUtils.appendStyle(this, "" +
                            "background:$Panel.background;" +
                            "arc:10;" +
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
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            }
        });
        getModel().addPaginationModelListener(new PaginationModelListener() {
            @Override
            public void paginationModelChanged(PaginationModelEvent event) {
                if (animator.isRunning()) {
                    animator.stop();
                    fromRec = createRec(fromRec, toRec, animate);
                } else {
                    fromRec = getRectangleOfIndex(event.getOldIndex());
                }
                toRec = getRectangleOfIndex(event.getNewIndex());
                animate = 0f;
                animator.start();
            }
        });
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
        accentColor1 = UIManager.getColor("Button.default.background");
        accentColor2 = UIManager.getColor("Component.accentColor");
    }

    private void paintSelected(Graphics g, Rectangle2D rec, float animate) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float arc = UIScale.scale(18);
        AffineTransform oldTran = g2.getTransform();
        g2.translate(rec.getX(), rec.getY());
        g2.setPaint(new GradientPaint(0, 0, accentColor1, (float) rec.getWidth(), (float) rec.getHeight(), accentColor2));
        g2.fill(new RoundRectangle2D.Double(0, 0, rec.getWidth(), rec.getHeight(), arc, arc));
        g2.setTransform(oldTran);

        // paint selected page text
        Component newCom = getItemRenderer().getPaginationItemRendererComponent(this, new Page(getSelectedPage(), Page.Type.PAGE), false, false, false, -2);
        rendererPane.paintComponent(g2, newCom, this, rec.getBounds());
        g2.dispose();
    }

    private Rectangle getRectangleOfIndex(int index) {
        if (checkCreateNextAndPreviousButton(getModel().getPagination().length)) {
            index++;
        }
        return rectangleAt(index);
    }

    private Rectangle2D createRec(Rectangle2D recFrom, Rectangle2D recTo, float animated) {
        double diffX = recTo.getX() - recFrom.getX();
        double newX = recFrom.getX() + (diffX * animated);
        return new Rectangle2D.Double(newX, recFrom.getY(), recFrom.getWidth(), recFrom.getHeight());
    }
}
