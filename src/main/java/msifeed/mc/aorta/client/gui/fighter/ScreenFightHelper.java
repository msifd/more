package msifeed.mc.aorta.client.gui.fighter;

import msifeed.mc.aorta.core.attributes.CharacterAttribute;
import msifeed.mc.aorta.core.attributes.StatusAttribute;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.status.CharStatus;
import msifeed.mc.mellow.mc.MellowGuiScreen;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.basic.Label;
import msifeed.mc.mellow.widgets.basic.Separator;
import msifeed.mc.mellow.widgets.button.ButtonLabel;
import msifeed.mc.mellow.widgets.window.Window;
import net.minecraft.entity.EntityLivingBase;

public class ScreenFightHelper extends MellowGuiScreen {
    private final EntityLivingBase entity;
    private Character character;
    private CharStatus charStatus;

    public ScreenFightHelper(EntityLivingBase entity) {
        this.entity = entity;

        final Window window = new Window();
        window.setTitle("Fight Helper");
        scene.addChild(window);

        final Widget windowContent = window.getContent();

        CharacterAttribute.get(entity).ifPresent(c -> character = new Character(c));
        StatusAttribute.get(entity).ifPresent(c -> charStatus = new CharStatus(c));

        if (character == null) {
            windowContent.addChild(new Label("Missing character data!"));
            return;
        }
        if (charStatus == null) {
            windowContent.addChild(new Label("Missing status data!"));
            return;
        }

        windowContent.addChild(new BodypartHealthView(character, charStatus));

        final ButtonLabel submitBtn = new ButtonLabel("Apply");
        submitBtn.setClickCallback(() -> {
            if (!entity.isEntityAlive())
                System.out.println("entity is actually dead");
            else if (character != null) {
//                CharacterAttribute.INSTANCE.set(entity, character);
                StatusAttribute.INSTANCE.set(entity, charStatus);
            }
        });
        windowContent.addChild(new Separator());
        windowContent.addChild(submitBtn);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
