package msifeed.mc.aorta.client.gui.status;

import msifeed.mc.aorta.core.character.BodyShield;
import msifeed.mc.aorta.core.character.Character;
import msifeed.mc.aorta.core.traits.Trait;
import msifeed.mc.aorta.sys.utils.L10n;
import msifeed.mc.mellow.layout.GridLayout;
import msifeed.mc.mellow.layout.ListLayout;
import msifeed.mc.mellow.widgets.Widget;
import msifeed.mc.mellow.widgets.droplist.DropList;
import msifeed.mc.mellow.widgets.text.Label;
import msifeed.mc.mellow.widgets.text.TextInput;

import java.util.Arrays;

class ParamsView extends Widget {
    private final Character character;
    private final boolean editable;

    ParamsView(Character character, boolean editable) {
        this.character = character;
        this.editable = editable;
        refill();
    }

    @Override
    public void refill() {
        clearChildren();
        if (editable)
            fillEditable();
        else
            fillNonEditable();
    }

    private void fillEditable() {
        setLayout(new GridLayout());

        final int vitalityThreshold = character.countVitalityThreshold();
        final int vitality = character.countVitality(vitalityThreshold);
        final int vitalityLevel = character.vitalityLevel(vitality, vitalityThreshold);
        final String vitalityLevelStr = L10n.tr("aorta.status.vitality." + vitalityLevel);

        addChild(new Label(L10n.tr("aorta.gui.status.vitality")));
        addChild(new Label(String.format("%d/%d (%s)", vitality, vitalityThreshold, vitalityLevelStr)));

        addChild(new Label(L10n.tr("aorta.gui.status.sanity")));

        final TextInput sanityInput = new TextInput();
        sanityInput.getSizeHint().x = 25;
        sanityInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 1, 125));
        sanityInput.setCallback(s -> character.sanity = (byte) sanityInput.getInt());
        sanityInput.setText(String.valueOf(character.sanity));
        addChild(sanityInput);

        addChild(new Label(L10n.tr("aorta.gui.status.shield_type")));
        final DropList<BodyShield.Type> shieldType = new DropList<>(Arrays.asList(BodyShield.Type.values()));
        shieldType.selectItem(character.shield.type.ordinal());
        shieldType.setSelectCallback(type -> character.shield.type = type);
        addChild(shieldType);

        addChild(new Label(L10n.tr("aorta.gui.status.shield_power")));
        final TextInput shieldPower = new TextInput();
        shieldPower.getSizeHint().x = 30;
        shieldPower.setText(String.valueOf(character.shield.power));
        shieldPower.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, 100));
        shieldPower.setCallback(s -> character.shield.power = (short) shieldPower.getInt());
        addChild(shieldPower);

        if (character.has(Trait.psionic)) {
            addChild(new Label(L10n.tr("aorta.gui.status.maxPsionics")));

            final Widget psiRight = new Widget();
            psiRight.setLayout(ListLayout.HORIZONTAL);
            addChild(psiRight);

            final Label psiDesc = new Label();
            psiDesc.getPos().y = 2;

            final TextInput psionicsInput = new TextInput();
            psionicsInput.getSizeHint().x = 25;
            psionicsInput.setFilter(s -> TextInput.isUnsignedIntBetween(s, 0, character.maxPsionics));
            psionicsInput.setCallback(s -> {
                character.psionics = (byte) psionicsInput.getInt();
                final int psiPercent = character.maxPsionics > 0 ? Math.floorDiv(character.psionics * 100, character.maxPsionics) : 0;
                psiDesc.setText(String.format("/%d (%d%%)", character.maxPsionics, psiPercent));
            });
            psionicsInput.setText(String.valueOf(character.psionics));

            psiRight.addChild(psionicsInput);
            psiRight.addChild(psiDesc);
        }
    }

    private void fillNonEditable() {
        setLayout(new GridLayout(5));

        final int vitalityThreshold = character.countVitalityThreshold();
        final int vitality = character.countVitality(vitalityThreshold);
        final int vitalityLevel = character.vitalityLevel(vitality, vitalityThreshold);
        addChild(new Label(L10n.tr("aorta.gui.status.vitality")));
        addChild(new Label(String.format("%d/%d (%s)",
                vitality,
                vitalityThreshold,
                L10n.tr("aorta.status.vitality." + vitalityLevel))));

        if (character.shield.type != BodyShield.Type.NONE && character.shield.power > 0) {
            addChild(new Label(L10n.tr("aorta.gui.status.shield_type")));
            addChild(new Label(character.shield.type.toString()));

            addChild(new Label(L10n.tr("aorta.gui.status.shield_power")));
            addChild(new Label(String.valueOf(character.shield.power)));
        }

        if (character.has(Trait.psionic)) {
            final int psionicsLevel = character.psionicsLevel();
            addChild(new Label(L10n.tr("aorta.gui.status.maxPsionics")));
            addChild(new Label(String.format("%d (%s)",
                    character.psionics,
                    L10n.tr("aorta.status.maxPsionics." + psionicsLevel))));
        }
    }
}
