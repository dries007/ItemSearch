package com.briman0094.itemsearch.proxy;

import com.briman0094.itemsearch.ItemSearch;
import com.briman0094.itemsearch.ItemVector;
import com.briman0094.itemsearch.client.render.RenderItemVector;
import com.briman0094.itemsearch.entity.EntityItemMarker;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;

import java.util.ArrayList;

public class ClientProxy extends CommonProxy
{
    public static ArrayList<ItemVector>       currentVectors  = new ArrayList<ItemVector>();
    public static ArrayList<EntityItemMarker> spawnedEntities = new ArrayList<EntityItemMarker>();

    @Override
    public void initRenderers()
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            EntityRegistry.registerModEntity(EntityItemMarker.class, "ItemMarker", 0, ItemSearch.INSTANCE, 256, 1, false);
            RenderingRegistry.registerEntityRenderingHandler(EntityItemMarker.class, new RenderItemVector());
        }
    }
}
