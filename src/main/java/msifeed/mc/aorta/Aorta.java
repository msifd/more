package msifeed.mc.aorta;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import msifeed.mc.aorta.books.RemoteBookManager;
import msifeed.mc.aorta.chat.Speechat;
import msifeed.mc.aorta.client.GuiHandler;
import msifeed.mc.aorta.core.Core;
import msifeed.mc.aorta.defines.DefineCommand;
import msifeed.mc.aorta.defines.Defines;
import msifeed.mc.aorta.environment.EnvironmentManager;
import msifeed.mc.aorta.genesis.Genesis;
import msifeed.mc.aorta.genesis.rename.RenameCommand;
import msifeed.mc.aorta.locks.Locks;
import msifeed.mc.aorta.logs.Logs;
import msifeed.mc.aorta.sys.attributes.AttributeHandler;
import msifeed.mc.aorta.sys.config.ConfigManager;
import msifeed.mc.aorta.sys.rpc.Rpc;
import msifeed.mc.aorta.tools.AortaCommand;
import msifeed.mc.aorta.tools.ToolItems;
import msifeed.mc.aorta.tweaks.*;
import msifeed.mc.aorta.tweaks.nametag.Nametag;
import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;

public class Aorta {
    public static final String MODID = "aorta";
    public static final String NAME = "Aorta";
    public static final String VERSION = "@VERSION@";

    public static Defines DEFINES = new Defines();

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.core.Core",
            clientSide = "msifeed.mc.aorta.core.CoreClient"
    )
    public static Core CORE;

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.genesis.Genesis",
            clientSide = "msifeed.mc.aorta.genesis.GenesisClient"
    )
    public static Genesis GENESIS;

    @SidedProxy(
            serverSide = "msifeed.mc.aorta.client.GuiHandler",
            clientSide = "msifeed.mc.aorta.client.GuiHandlerClient"
    )
    public static GuiHandler GUI_HANDLER;

    public void preInit(FMLPreInitializationEvent event) {
        AttributeHandler.init();
        Rpc.init();
        EnableDesertRain.apply();

        Logs.init();
        EntityControl.init();
        EnvironmentManager.init();
        ConfigManager.init(event);
    }

    public void init() {
        CORE.init();
        GENESIS.init();
        ToolItems.init();
        RemoteBookManager.init();
        Locks.init();
        Speechat.init();
        Nametag.INSTANCE.init();
        MakeEveryoneHealthy.apply();
    }

    public void postInit() {
        MakeFoodEdible.apply();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        final CommandHandler commandHandler = (CommandHandler) server.getCommandManager();

        commandHandler.registerCommand(new AortaCommand());
        commandHandler.registerCommand(new DefineCommand());
        commandHandler.registerCommand(new RenameCommand());
        commandHandler.registerCommand(new UnstuckCommand());

        CORE.registerCommands(commandHandler);
        Speechat.registerCommands(commandHandler);
        EnvironmentManager.registerCommands(commandHandler);
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        ConfigManager.save();
    }
}