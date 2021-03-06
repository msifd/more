package msifeed.mc.more.client.combat.other;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import msifeed.mc.more.crabs.action.effects.Buff;
import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.more.crabs.utils.CombatAttribute;
import msifeed.mc.more.tools.ItemCombatTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.stream.Collectors;

public enum CombatOverlay {
    INSTANCE;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null || !(heldItem.getItem() instanceof ItemCombatTool))
            return;

        final FontRenderer fr = RenderManager.instance.getFontRenderer();

        if (fr == null)
            return;

        final EntityLivingBase entity = getEntity();

        final ArrayList<String> lines = new ArrayList<>();
        lines.add(String.format("Entity: %s (%d)", entity.getCommandSenderName(), entity.getEntityId()));
        lines.add(String.format("  HP: %f/%f", entity.getHealth(), entity.getMaxHealth()));
        lines.add(String.format("  Armor: %d", entity.getTotalArmorValue()));

        CharacterAttribute.get(entity).ifPresent(chr -> {
            lines.add("Character");
            lines.add("  Armor override: " + chr.armor);
            lines.add("  Damage threshold: " + chr.damageThreshold);
        });

        CombatAttribute.get(entity).ifPresent(com -> {
            lines.add("Combat");
            lines.add("  Training health: " + com.healthBeforeJoin);
            lines.add("  Puppet: " + com.puppet);
            lines.add("  Knocked out: " + com.knockedOut);
            lines.add("  Buffs: " + com.buffs.stream().map(Buff::encode).collect(Collectors.joining(", ")));
            lines.add("  Prev actions: " + String.join(", ", com.prevActions));
            lines.add("  Stage: " + com.phase);
            lines.add("  Offender: " + com.offender);
            lines.add("  Defenders: " + com.defenders.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            lines.add("  Dmg dealt: " + com.damageDealt.stream().mapToDouble(da -> da.amount).sum() + " in " + com.damageDealt.size() + " parts");
            lines.add("  Dmg to receive: " + com.damageToReceive.stream().mapToDouble(da -> da.amount).sum() + " in " + com.damageToReceive.size() + " parts");
            lines.add("  Action: " + (com.action != null ? com.action.getTitle() : "null"));
        });

        GL11.glPushMatrix();
        int y = 5;
        for (String l : lines) {
            fr.drawStringWithShadow(l, 5, y, 0xffffffff);
            y += fr.FONT_HEIGHT;
        }
        GL11.glPopMatrix();
    }

    private static EntityLivingBase getEntity() {
        final Minecraft mc = Minecraft.getMinecraft();
        final MovingObjectPosition mop = mc.objectMouseOver;
        if (mop != null
            && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
            && mop.entityHit instanceof EntityLivingBase)
            return (EntityLivingBase) mop.entityHit;
        return mc.thePlayer;
    }
}
