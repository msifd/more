package msifeed.mc.mellow.layout;

import msifeed.mc.mellow.utils.Geom;
import msifeed.mc.mellow.utils.Point;
import msifeed.mc.mellow.widgets.Widget;

import java.util.Collection;

public class FreeLayout implements Layout {
    public static final FreeLayout INSTANCE = new FreeLayout();

    @Override
    public Point layoutIndependent(Widget parent, Collection<Widget> children) {
        for (Widget child : children) {
            final Geom childGeom = child.getGeometry();
            childGeom.reset();
            childGeom.setSize(LayoutUtils.getPreferredSize(child));
            childGeom.translate(child.getPos(), child.getZLevel());
        }

        return parent.getSizeHint();
    }
}
