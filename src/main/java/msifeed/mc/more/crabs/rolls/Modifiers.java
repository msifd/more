package msifeed.mc.more.crabs.rolls;

import msifeed.mc.more.crabs.character.Ability;

import java.util.EnumMap;

public class Modifiers {
    public int damage = 0;
    public int roll = 0;
    public EnumMap<Ability, Integer> abilities = new EnumMap<>(Ability.class);

    public Modifiers() {
    }

    public Modifiers(Modifiers m) {
        damage = m.damage;
        roll = m.roll;
        abilities.putAll(m.abilities);
    }

    public boolean isZeroed() {
        return damage == 0 && roll == 0 && !hasAbilityMods();
    }

    public boolean hasAbilityMods() {
        return abilities.values().stream().anyMatch(i -> i != 0);
    }

    public int toAbility(Ability ability) {
        return abilities.getOrDefault(ability, 0);
    }
}
