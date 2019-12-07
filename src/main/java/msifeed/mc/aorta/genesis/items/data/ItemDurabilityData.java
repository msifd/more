package msifeed.mc.aorta.genesis.items.data;

import java.util.Random;
import com.google.gson.JsonObject;
import msifeed.mc.aorta.genesis.JsonUtils;

public class ItemDurabilityData {
    public int maxDurability = 0;
    public int minBreakage = 1;
    public int maxBreakage = 1;
    public int minSpecialBreakage = 1;
    public int maxSpecialBreakage = 1;

    private Random rand = new Random();

    public ItemDurabilityData() {

    }

    public ItemDurabilityData(JsonObject json) {
        if (!json.has("durability") || !json.get("durability").isJsonObject())
            return;
        final JsonObject durability = json.get("durability").getAsJsonObject();
        JsonUtils.consumeInt(durability, "maxDurability", i -> maxDurability = i);
        JsonUtils.consumeInt(durability, "minBreakage", i -> minBreakage = i);
        JsonUtils.consumeInt(durability, "maxBreakage", i -> maxBreakage = i);
        JsonUtils.consumeInt(durability, "minSpecialBreakage", i -> minSpecialBreakage = i);
        JsonUtils.consumeInt(durability, "maxSpecialBreakage", i -> maxSpecialBreakage = i);
    }

    public int getBreakage() {
        return minBreakage + rand.nextInt(maxBreakage - minBreakage + 1);
    }

    public int getSpecialBreakage() {
        return minSpecialBreakage + rand.nextInt(maxSpecialBreakage - minSpecialBreakage + 1);
    }
}
