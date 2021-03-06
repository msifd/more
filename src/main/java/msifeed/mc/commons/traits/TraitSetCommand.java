package msifeed.mc.commons.traits;

import msifeed.mc.more.crabs.utils.CharacterAttribute;
import msifeed.mc.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class TraitSetCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "trait";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/trait <trait> [player]";
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2 && isGm(sender))
            return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        else
            return null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            error(sender, "Usage: " + getCommandUsage(sender));
            return;
        }

        final String traitName = args[0].toLowerCase();
        final EntityLivingBase target;

        if (args.length > 1) {
            final EntityLivingBase player = findPlayer(args[1]);
            if (player == null) {
                error(sender, "Unknown player");
                return;
            }
            target = player;
        } else {
            if (!(sender instanceof EntityLivingBase)) {
                error(sender, "You should be at least entity!");
                return;
            }
            target = (EntityLivingBase) sender;
        }

        toggleTrait(sender, target, traitName);
    }

    private void toggleTrait(ICommandSender sender, EntityLivingBase entity, String traitName) {
        try {
            final Trait trait = Trait.valueOf(traitName);
            if (!isAllowedToSet(sender, trait)) {
                error(sender, "You are are not allowed to set this trait!");
                return;
            }

            final boolean added = CharacterAttribute.toggle(entity, trait);
            info(sender, "Trait '%s' %s", traitName, added ? "added" : "removed");
        } catch (IllegalArgumentException e) {
            error(sender, "Unknown trait '%s'", traitName);
        }
    }

    private boolean isAllowedToSet(ICommandSender sender, Trait trait) {
        if (trait.type == TraitType.SYSTEM)
            return isAdmin(sender);
        return isGm(sender);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 1;
    }
}
