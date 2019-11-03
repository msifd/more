package msifeed.mc.aorta.chat.commands;

import msifeed.mc.aorta.chat.ChatHandler;
import msifeed.mc.aorta.chat.composer.Composer;
import msifeed.mc.aorta.chat.composer.SpeechType;
import msifeed.mc.aorta.chat.net.ChatMessage;
import msifeed.mc.aorta.sys.cmd.ExtCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class OfftopCommand extends ExtCommand {
    @Override
    public String getCommandName() {
        return "o";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/o <text>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayer)) {
            error(sender, "You should be at least player!");
            return;
        }

        if (args.length == 0)
            return;

        final EntityPlayer player = (EntityPlayer) sender;
        final String text = String.join(" ", args);
        ChatHandler.sendChatMessage(player, Composer.makeMessage(SpeechType.OFFTOP, player, text));
    }
}
