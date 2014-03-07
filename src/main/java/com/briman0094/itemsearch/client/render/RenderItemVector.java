package com.briman0094.itemsearch.client.render;

import com.briman0094.itemsearch.entity.EntityItemMarker;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RenderItemVector extends RenderEntity
{
    private static ItemStack markerIcon = new ItemStack((Item) Item.itemRegistry.getObject("nether_star"));

    @Override
    public void doRender(Entity entity, double x, double y, double z, float i, float j)
    {
        //super.doRender(entity, x, y, z, i, j);
        if (entity instanceof EntityItemMarker)
        {
            EntityItemMarker itemMarker = (EntityItemMarker) entity;
            RenderHelper.renderItemVectorList(itemMarker.worldObj, markerIcon, itemMarker.vectors, (float) x, (float) y, (float) z);
        }
    }
}
