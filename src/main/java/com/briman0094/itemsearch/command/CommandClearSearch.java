package com.briman0094.itemsearch.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandClearSearch extends CommandBase
{
    public static final CommandClearSearch INSTANCE = new CommandClearSearch();

    @Override
    public String getCommandName()
    {
        return "cs";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "Clear the search entry.";
    }

    @Override
    public List getCommandAliases()
    {
        ArrayList<String> aliases = new ArrayList<String>();
        aliases.add("sc");
        return aliases;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1iCommandSender)
    {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        CommandSearch.INSTANCE.processCommand(sender, new String[] {"clear"});
    }
}
