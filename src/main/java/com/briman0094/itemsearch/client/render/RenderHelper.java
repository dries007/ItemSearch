package com.briman0094.itemsearch.client.render;

import com.briman0094.itemsearch.ItemSearch;
import com.briman0094.itemsearch.ItemVector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;

public class RenderHelper
{
    public static final char COLOR_CODE = '\u00A7';

    private static HashMap<ItemStack, EntityItem> itemEntities = new HashMap<ItemStack, EntityItem>();

    public static void renderItemVectorList(World world, ItemStack icon, ArrayList<ItemVector> vectors, float x, float y, float z)
    {
        RenderManager renderManager = RenderManager.instance;
        FontRenderer fontRenderer = renderManager.getFontRenderer();
        float scale = 0.027f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;
        for (int i = 0; i < vectors.size(); i++)
        {
            EntityItem toRender;
            if (itemEntities.containsKey(vectors.get(i).item))
            {
                toRender = itemEntities.get(vectors.get(i).item);
            }
            else
            {
                toRender = new EntityItem(world);
                toRender.setEntityItemStack(vectors.get(i).item);
                itemEntities.put(vectors.get(i).item, toRender);
            }
            toRender.setLocationAndAngles(x, y, z, 0f, 0f);
            toRender.hoverStart = 0f;
            String text = vectors.get(i).item.getDisplayName();
            String search = vectors.get(i).searchTerm;
            if (ItemSearch.DEF_RENDER_SEARCH)
            {
                int sPos = text.toLowerCase().indexOf(search.toLowerCase());
                if (sPos >= 0)
                {
                    int len = search.length();
                    if (sPos + len <= text.length())
                    {
                        String beforeSearch = text.substring(0, sPos);
                        String afterSearch = text.substring(sPos + len);
                        String midSearch = text.substring(sPos, sPos + len);
                        // Insert color codes to highlight search term
                        text = beforeSearch + COLOR_CODE + 'c' + midSearch + COLOR_CODE + 'f' + afterSearch;
                    }
                }
            }
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            int yOffset = -i * 9;
            int strHalfWidth = fontRenderer.getStringWidth(text) / 2;
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
            tessellator.addVertex((double) (-strHalfWidth - 1), (double) (-1 + yOffset) - 10, 0.0D);
            tessellator.addVertex((double) (-strHalfWidth - 1), (double) (8 + yOffset) - 10, 0.0D);
            tessellator.addVertex((double) (strHalfWidth + 1), (double) (8 + yOffset) - 10, 0.0D);
            tessellator.addVertex((double) (strHalfWidth + 1), (double) (-1 + yOffset) - 10, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset - 10, 0xFFFFFF);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset - 10, 0xFFFFFF);
            GL11.glScalef(24f, -24f, 24f);
            net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_CULL_FACE);
            if (ItemSearch.renderIcons) renderManager.renderEntityWithPosYaw(toRender, -strHalfWidth / 32f - 0.75f, 0.125f + -yOffset / 24f, 0, 0f, 0f);
            GL11.glScalef(1f / 24f, -1f / 24f, 1f / 24f);
        }
        RenderItem renderItem = ((RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class));
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        renderItem.renderItemIntoGUI(fontRenderer, Minecraft.getMinecraft().renderEngine, icon, -9, 0);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
