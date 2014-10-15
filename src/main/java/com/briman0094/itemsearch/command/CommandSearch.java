package com.briman0094.itemsearch.command;

import com.briman0094.itemsearch.ItemSearch;
import com.briman0094.itemsearch.ItemVector;
import com.briman0094.itemsearch.api.NoItemSearch;
import com.briman0094.itemsearch.network.PacketPipeline;
import com.briman0094.itemsearch.network.VectorListPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CommandSearch extends CommandBase
{
    public static final CommandSearch INSTANCE = new CommandSearch();

    @Override
    public String getCommandName()
    {
        return "search";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "Search for items in nearby chests.";
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
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
        if (!(sender instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) sender;
        if (args.length <= 0)
        {
            player.addChatComponentMessage(new ChatComponentText("Usage: /search <itemName|clear> [radius]"));
        }
        else if (args.length == 1 && args[0].equalsIgnoreCase("clear"))
        {
            VectorListPacket packet = new VectorListPacket(new ArrayList<ItemVector>());
            if (player instanceof EntityPlayerMP)
            {
                PacketPipeline.PIPELINE.sendTo(packet, (EntityPlayerMP) player);
            }
        }
        else
        {
            boolean isLastArgumentNumber = true;
            String numTest = args[args.length - 1];
            for (int c = 0; c < numTest.length(); c++)
            {
                char chr = numTest.charAt(c);
                if (!Character.isDigit(chr))
                {
                    isLastArgumentNumber = false;
                    break;
                }
            }
            String searchString = "";
            for (int i = 0; i < (isLastArgumentNumber ? args.length - 1 : args.length); i++)
            {
                searchString += args[i];
                searchString += " ";
            }
            searchString = searchString.trim();
            try
            {
                int radius = isLastArgumentNumber ? Integer.parseInt(args[args.length - 1]) : ItemSearch.defRadius;
                if (radius <= ItemSearch.maxRadius)
                {
                    VectorListPacket packet = new VectorListPacket(findItemsInRadius(player.worldObj, searchString, (int) player.posX, (int) player.posY, (int) player.posZ, radius, player));
                    if (player instanceof EntityPlayerMP)
                    {
                        PacketPipeline.PIPELINE.sendTo(packet, (EntityPlayerMP) player);
                    }
                }
                else
                {
                    player.addChatComponentMessage(new ChatComponentText("Radius must be " + ItemSearch.maxRadius + " blocks or less"));
                }
            }
            catch (Exception ex)
            {
                if (ex instanceof NumberFormatException)
                {
                    player.addChatComponentMessage(new ChatComponentText("\"" + args[1] + "\" is not a number"));
                }
                else
                {
                    ItemSearch.logger.warn("An error occurred while executing the \"search\" command:", ex);
                    player.addChatComponentMessage(new ChatComponentText("An internal server error occurred while attempting to execute the command"));
                }
            }
        }
    }

    public ArrayList<ItemVector> findItemsInRadius(World world, String search, int x, int y, int z, int radius, EntityPlayer player)
    {
        ArrayList<ItemVector> foundItems = new ArrayList<ItemVector>();
        try
        {
            Pattern p = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
            for (int xx = x - radius; xx <= x + radius; xx++)
            {
                for (int yy = y - radius; yy <= y + radius; yy++)
                {
                    for (int zz = z - radius; zz <= z + radius; zz++)
                    {
                        TileEntity teTest = world.getTileEntity(xx, yy, zz);
                        if (teTest != null)
                        {
                            if (teTest instanceof IInventory)
                            {
                                // Do not search classes that use the API to prevent it
                                if (!teTest.getClass().isAnnotationPresent(NoItemSearch.class))
                                {
                                    IInventory inventory = (IInventory) teTest;
                                    for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
                                    {
                                        ItemStack test = inventory.getStackInSlot(slot);
                                        if (test != null)
                                        {
                                            String iName = test.getDisplayName();
                                            Matcher m = p.matcher(iName);
                                            if (m.find())
                                            {
                                                foundItems.add(new ItemVector(xx, yy, zz, test, search));
                                                // break curInventory;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (PatternSyntaxException e)
        {
            player.addChatComponentMessage(new ChatComponentText("Invalid search syntax"));
        }
        return foundItems;
    }
}
