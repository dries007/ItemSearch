package com.briman0094.itemsearch.network;

import com.briman0094.itemsearch.ItemVector;
import com.briman0094.itemsearch.entity.EntityItemMarker;
import com.briman0094.itemsearch.proxy.ClientProxy;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class VectorListPacket extends AbstractPacket
{
    ArrayList<ItemVector> itemVectors;

    public VectorListPacket(ArrayList<ItemVector> itemVectors)
    {
        this.itemVectors = itemVectors;
    }

    public VectorListPacket()
    {

    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(itemVectors.size());

        for (ItemVector v : itemVectors)
        {
            buffer.writeInt(v.x).writeInt(v.y).writeInt(v.z);
            ByteBufUtils.writeItemStack(buffer, v.item);
            ByteBufUtils.writeUTF8String(buffer, v.searchTerm);
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        final int size = buffer.readInt();
        itemVectors = new ArrayList<ItemVector>(size);
        for (int i = 0; i < size; i++)
        {
            itemVectors.add(new ItemVector(buffer.readInt(), buffer.readInt(), buffer.readInt(), ByteBufUtils.readItemStack(buffer), ByteBufUtils.readUTF8String(buffer)));
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        for (EntityItemMarker eim : ClientProxy.spawnedEntities) eim.setDead();
        ClientProxy.currentVectors = new ArrayList<ItemVector>();
        ClientProxy.spawnedEntities = new ArrayList<EntityItemMarker>();

        for (ItemVector itemVector : itemVectors)
        {
            ClientProxy.currentVectors.add(itemVector);
            EntityItemMarker eim = new EntityItemMarker(player.worldObj, itemVector);
            player.worldObj.spawnEntityInWorld(eim);
            ClientProxy.spawnedEntities.add(eim);
        }

    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {

    }
}
