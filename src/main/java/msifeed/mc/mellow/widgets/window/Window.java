package msifeed.mc.mellow.widgets.window;

import msifeed.mc.mellow.Mellow;
import msifeed.mc.mellow.layout.VerticalLayout;
import msifeed.mc.mellow.render.RenderParts;
import msifeed.mc.mellow.theme.Part;
import msifeed.mc.mellow.utils.SizePolicy;
import msifeed.mc.mellow.widgets.Widget;

public class Window extends Widget {
    private Part backgroundPart = Mellow.THEME.parts.get("window");
    private WindowHeader header = new WindowHeader(this);
    private Widget content = new Widget();

    public Window() {
        setLayout(new VerticalLayout(0));
        getMargin().set(1);

        header.setVerSizePolicy(SizePolicy.Policy.FIXED);
        header.setSizeHint(0, 13);
        header.setZLevel(1);

        content.getMargin().set(0, 2, 4, 2);
        content.setLayout(VerticalLayout.INSTANCE);

        super.addChild(header);
        super.addChild(content);
    }

    public void setTitle(String text) {
        header.setLabel(text);
    }

    public Widget getContent() {
        return content;
    }

    @Override
    protected void renderSelf() {
        RenderParts.nineSlice(backgroundPart, getGeometry());
    }
}
