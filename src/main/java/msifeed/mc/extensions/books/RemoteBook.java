package msifeed.mc.extensions.books;

import msifeed.mc.Bootstrap;
import net.minecraft.util.ResourceLocation;

public class RemoteBook {
    public Style style = Style.REGULAR;
    public String title = "";
    public String text = "";

    public enum Style {
        REGULAR("book_regular"), RICH("book_rich"), PAD("book_pad"), NOTE("book_note");

        public final ResourceLocation sprite;

        Style(String tex) {
            this.sprite = new ResourceLocation(Bootstrap.MODID, "textures/gui/" + tex + ".png");
        }
    }
}
