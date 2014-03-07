package com.briman0094.itemsearch.proxy;

import com.briman0094.itemsearch.command.CommandClearSearch;
import com.briman0094.itemsearch.command.CommandSearch;
import net.minecraft.command.ServerCommandManager;

public class CommonProxy
{
    public void registerCommands(ServerCommandManager commandHandler)
    {
        commandHandler.registerCommand(CommandSearch.INSTANCE);
        commandHandler.registerCommand(CommandClearSearch.INSTANCE);
    }

    public void initRenderers()
    {

    }
}
