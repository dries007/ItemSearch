package com.briman0094.itemsearch;

import com.briman0094.itemsearch.network.PacketPipeline;
import com.briman0094.itemsearch.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

@Mod(modid = ItemSearch.MOD_ID, name = "Briman's Item Search", useMetadata = true)
public class ItemSearch
{
    public static final String  MOD_ID             = "ItemSearch";
    public static final int     DEF_MARKER_TIMEOUT = 90;
    public static final boolean DEF_RENDER_ICONS   = true;
    public static final boolean DEF_RENDER_SEARCH  = true;
    public static final int     DEF_DEFAULT_RADIUS = 32;
    public static final int     DEF_MAX_RADIUS     = 64;

    public static int     markerTimeout   = 0;
    public static boolean renderIcons     = true;
    public static boolean highlightSearch = true;
    public static int     defRadius       = 32;
    public static int     maxRadius       = 64;

    @Mod.Instance(ItemSearch.MOD_ID)
    public static ItemSearch INSTANCE;

    @SidedProxy(serverSide = "com.briman0094.itemsearch.proxy.CommonProxy", clientSide = "com.briman0094.itemsearch.proxy.ClientProxy")
    public static CommonProxy PROXY;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();
        config.addCustomCategoryComment("settings", "searchTimeout: The amount of time before the search clears itself. Set to 0 to disable.\nrenderIcons: Whether or not to render the result item next to the name");
        markerTimeout = config.get("settings", "searchTimeout", DEF_MARKER_TIMEOUT).getInt();
        renderIcons = config.get("settings", "renderIcons", DEF_RENDER_ICONS).getBoolean(DEF_RENDER_ICONS);
        highlightSearch = config.get("settings", "highlightSearchTerm", true).getBoolean(DEF_RENDER_SEARCH);
        defRadius = config.get("settings", "defaultSearchRadius", DEF_DEFAULT_RADIUS).getInt();
        maxRadius = config.get("settings", "maximumSearchRadius", DEF_MAX_RADIUS).getInt();
        config.save();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        PROXY.initRenderers();
        PacketPipeline.PIPELINE.initialise();
    }

    @Mod.EventHandler()
    public void fmlEvent(FMLPostInitializationEvent event)
    {
        PacketPipeline.PIPELINE.postInitialise();
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) throws Exception
    {
        //System.out.println(FMLCommonHandler.instance().getEffectiveSide());
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            ICommandManager cmdManager = MinecraftServer.getServer().getCommandManager();
            if (cmdManager instanceof ServerCommandManager) PROXY.registerCommands((ServerCommandManager) cmdManager);
            else
            {
                throw new Exception("ItemSearch cannot load due to an invalid server instance");
            }
        }
    }

}
