package msifeed.mc.mellow.widgets;

import msifeed.mc.mellow.layout.FreeLayout;
import msifeed.mc.mellow.layout.Layout;
import msifeed.mc.mellow.render.RenderShapes;
import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.utils.SizePolicy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

public class Widget {
    public static Widget hoveredWidget = null;
    public static Widget pressedWidget = null;
    public static Widget focusedWidget = null;

    private boolean visible = true;

    private int zLevel = 0;
    private Point pos = new Point();
    private Point sizeHint = new Point();
    private Margins margin = new Margins();
    private SizePolicy sizePolicy = new SizePolicy();

    private Layout layout = FreeLayout.INSTANCE;
    private Point contentSize = new Point();
    private Geom geometry = new Geom();

    private Widget parent;
    private int widgetTreeDepth = 0;
    private ArrayList<Widget> children = new ArrayList<>();

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos.set(pos);
    }

    public void setPos(int x, int y) {
        this.pos.set(x, y);
    }

    public int getZLevel() {
        return zLevel;
    }

    public void setZLevel(int z) {
        this.zLevel = z;
    }

    public Point getSizeHint() {
        return sizeHint;
    }

    public void setSizeHint(Point sizeHint) {
        this.sizeHint.set(sizeHint);
    }

    public void setSizeHint(int w, int h) {
        this.sizeHint.set(w, h);
    }

    public Point getContentSize() {
        return contentSize;
    }

    public SizePolicy getSizePolicy() {
        return sizePolicy;
    }

    public void setSizePolicy(SizePolicy sizePolicy) {
        this.sizePolicy = sizePolicy;
    }

    public void setSizePolicy(SizePolicy.Policy h, SizePolicy.Policy v) {
        this.sizePolicy.horizontalPolicy = h;
        this.sizePolicy.verticalPolicy = v;
    }

    public void setVerSizePolicy(SizePolicy.Policy v) {
        this.sizePolicy.verticalPolicy = v;
    }

    public Margins getMargin() {
        return margin;
    }

    public void setMargin(Margins margin) {
        this.margin = margin;
    }

    public Geom getGeometry() {
        return geometry;
    }

    public int getGeometryZ() {
        return getGeometry().z;
    }

    public Geom getGeomWithMargin() {
        final Geom geometry = new Geom(getGeometry());
        final Margins margin = getMargin();
        geometry.offsetPos(margin);
        geometry.offsetSize(margin);
        return geometry;
    }

    public Widget getParent() {
        return parent;
    }

    public Widget getTopParent() {
        Widget w = getParent();
        while (w != null && w.getParent() != null)
            w = w.getParent();
        return w;
    }

    public void setParent(Widget parent) {
        if (parent != null) {
            this.parent = parent;
//            this.widgetTreeDepth = parent.widgetTreeDepth + 1;
        } else {
            this.parent = null;
//            this.widgetTreeDepth = 0;
        }
    }

    public int getWidgetTreeDepth() {
        return widgetTreeDepth;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void update() {
        updateIndependentLayout();
        updateRelativeLayout();
    }

    protected void updateIndependentLayout() {
        for (Widget child : getChildren())
            child.updateIndependentLayout();

        contentSize = layout.layoutIndependent(this, getChildren());
    }

    protected void updateRelativeLayout() {
        updateWidgetTreeDepth();

        layout.layoutRelativeParent(this, getChildren());

        for (Widget child : getChildren())
            child.updateRelativeLayout();

        updateSelf();
    }

    protected void updateWidgetTreeDepth() {
        if (parent == null)
            this.widgetTreeDepth = 0;
        else
            this.widgetTreeDepth = parent.widgetTreeDepth + 1;
    }

    protected void updateSelf() {
    }

    public void render() {
        if (isVisible()) {
            renderSelf();
            renderChildren();
            postRenderSelf();
//            if (isHovered())
//                renderDebug();
        }
    }

    public void renderDebug() {
        RenderShapes.frame(getGeometry(), 1, hashCode()); // for debug purposes
    }

    protected void renderSelf() {
    }

    protected void postRenderSelf() {
    }

    protected void renderChildren() {
        for (Widget w : children)
            w.render();
    }

    public Collection<Widget> getChildren() {
        return children;
    }

    public void addChild(Widget widget) {
        children.add(widget);
        if (widget.parent == null) {
            widget.setParent(this);
        }
    }

    public void removeChild(Widget widget) {
        children.remove(widget);
    }

    public void clearChildren() {
        children.clear();
    }

    public Stream<Widget> getLookupChildren() {
        return getChildren().stream();
    }

    public boolean containsAnyChildren(Point p) {
        return true;
    }

    public boolean containsPoint(Point p) {
        return isVisible() && getGeometry().contains(p);
    }

    public boolean isHovered() {
        return this == hoveredWidget;
    }

    public boolean isPressed() {
        return this == pressedWidget;
    }

    public boolean isFocused() {
        return this == focusedWidget;
    }

    public static void setFocused(Widget widget) {
        if (focusedWidget == widget)
            return;
        final Widget lastFocus = focusedWidget;
        focusedWidget = widget;
        if (lastFocus != null)
            lastFocus.onFocusLoss();
    }

    protected void onFocusLoss() {
    }

    public int compareDepth(Widget another) {
        return Comparator.comparingInt(Widget::getGeometryZ)
            .thenComparing(Widget::getWidgetTreeDepth)
            .reversed()
            .compare(this, another);
    }
}
