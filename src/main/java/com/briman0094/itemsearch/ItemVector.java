package com.briman0094.itemsearch;

import net.minecraft.item.ItemStack;

public class ItemVector
{
    public int x, y, z;
    public ItemStack item;
    public String    searchTerm;

    public ItemVector(int x, int y, int z, ItemStack item, String searchTerm)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = new ItemStack(item.getItem(), 1, item.getItemDamage());
        this.searchTerm = searchTerm;
    }
}
