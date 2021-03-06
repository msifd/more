package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Margins;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class GridLayout implements Layout {
    protected int spacing = 2;

    public GridLayout() {
    }

    public GridLayout(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public Point layoutIndependent(Widget parent, Collection<Widget> children) {
        int yOffset = 0;
        int maxLeftWidth = 0;
        int maxRightWidth = 0;

        boolean leftWidget = true;
        int leftWidth = 0;
        int lineHeight = 0;

        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.setSize(LayoutUtils.getPreferredSize(child));
            childGeom.translate(child.getPos(), child.getZLevel());
            childGeom.translate(0, yOffset);

            if (leftWidget) {
                leftWidth = childGeom.w + spacing;
                lineHeight = childGeom.h;
                maxLeftWidth = Math.max(maxLeftWidth, childGeom.w);
            } else {
                childGeom.translate(leftWidth, 0);
                lineHeight = Math.max(lineHeight, childGeom.h);
                yOffset += lineHeight + spacing;
                maxRightWidth = Math.max(maxRightWidth, childGeom.w);
            }

            leftWidget = !leftWidget;
        }

        if (!leftWidget)
            yOffset += lineHeight + spacing;

        final Point contentSize = new Point(maxLeftWidth + maxRightWidth + spacing, yOffset - spacing);
        final Margins margin = parent.getMargin();
        contentSize.translate(margin.horizontal(), margin.vertical());

        return contentSize;
    }

    @Override
    public void layoutRelativeParent(Widget parent, Collection<Widget> children) {
        final Geom geometry = parent.getGeomWithMargin();

        int maxLeftWidth = 0;
        int maxRightWidth = 0;
        int maxRowHeight = 0;

        boolean isLeftWidget = true;
        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            if (isLeftWidget && childGeom.w > maxLeftWidth)
                maxLeftWidth = childGeom.w;
            if (!isLeftWidget && childGeom.w > maxRightWidth)
                maxRightWidth = childGeom.w;
            if (childGeom.h > maxRowHeight)
                maxRowHeight = childGeom.h;
            isLeftWidget = !isLeftWidget;
        }

        isLeftWidget = true;
        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.translate(geometry);
            childGeom.y += (maxRowHeight - childGeom.h) / 2;
            childGeom.w = isLeftWidget ? maxLeftWidth : maxRightWidth;
            if (!isLeftWidget)
                childGeom.x = geometry.x + maxLeftWidth + spacing;
            isLeftWidget = !isLeftWidget;
        }
    }
}
