package com.briman0094.itemsearch.entity;

import com.briman0094.itemsearch.ItemSearch;
import com.briman0094.itemsearch.ItemVector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EntityItemMarker extends Entity
{
    public  ArrayList<ItemVector> vectors;
    private int                   ticks;

    public EntityItemMarker(World world, ItemVector vector)
    {
        super(world);
        ticks = 0;
        vectors = new ArrayList<ItemVector>();
        vectors.add(vector);
        if (vector != null)
        {
            this.posX = vector.x;
            this.posY = vector.y + 1;
            this.posZ = vector.z;
        }
        else
        {
            kill();
        }
        isAirBorne = true;
        isImmuneToFire = true;
        noClip = true;
        ignoreFrustumCheck = true;
        renderDistanceWeight = 16;
        boundingBox.setBounds(posX, posY, posZ, posX + 0.1, posY + 0.1, posZ + 0.1);
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 0.1, posY + 0.1, posZ + 0.1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire()
    {
        return false;
    }

    @Override
    public float getBrightness(float par1)
    {
        return 1f;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float par1)
    {
        return 15;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0;
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    @Override
    public boolean canAttackWithItem()
    {
        return false;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return false;
    }

    @Override
    protected void entityInit()
    {

    }

    @Override
    public void onUpdate()
    {
        ticks++;
        if (ticks == Integer.MAX_VALUE) ticks = 0;
        if (ItemSearch.markerTimeout > 0 && ticks >= ItemSearch.markerTimeout * 20)
        {
            kill();
        }
        AxisAlignedBB testBox = AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1, posY + 1, posZ + 1);
        ArrayList<EntityItemMarker> entities = (ArrayList<EntityItemMarker>) worldObj.getEntitiesWithinAABB(EntityItemMarker.class, testBox);
        for (EntityItemMarker marker : entities)
        {
            if (marker != this && !marker.isDead)
            {
                for (ItemVector v : marker.vectors)
                {
                    vectors.add(v);
                }
                marker.setDead();
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1)
    {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1)
    {
    }

}
