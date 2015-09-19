/*******************************************************************************
 * Copyright 2014, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package biomesoplenty.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class EntityTrailFX extends EntityFX
{
	private static ResourceLocation TEST_TRAIL_LOC = new ResourceLocation("biomesoplenty:textures/particles/test_trail.png");
	
    /**The index of the flower to be spawned, values are 0-3*/
	private int particleIndex;
	
	public EntityTrailFX(World world, double x, double y, double z) 
	{
		super(world, x, y, z);
		
        this.motionX = this.motionY = this.motionZ = 0.0D; //Trail particles should not move
		this.particleMaxAge = 550;
		this.particleIndex = this.rand.nextInt(4); //Choose a random index on creation
	}
    
    @Override
    public void renderParticle(WorldRenderer renderer, Entity entity, float partialTicks, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY)
    {
        // EffectRenderer will by default bind the vanilla particles texture, override with our own
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TEST_TRAIL_LOC);
        
        particleIndex = 1;
        
        //The overall maxU and maxV of the particle sheet is 1.0 (representing 16px)
        float minU = (particleIndex % 2) * 0.5F; //Particles on the left side are 0, right are 0.5
        float maxU = minU + 0.5F; //Each flower is 8px wide (half of the overall sheet)
        
        float minV = (particleIndex / 2) * 0.5F; //Uses integer rounding errors (0 and 1 = 0, 2 and 3 = 1)
        float maxV = minV + 0.5F; //Each flower is 8px high (half of the overall sheet)
        
        //Vanilla particle rendering
        float alpha = 1.0F - Math.min(1.0F, 2.0F * this.particleAge / this.particleMaxAge);
        float width = 0.15F;
        float x = (float)(prevPosX + (posX - prevPosX) - interpPosX);
        float y = (float)(prevPosY + (posY - prevPosY) - interpPosY);
        float z = (float)(prevPosZ + (posZ - prevPosZ) - interpPosZ);

        renderer.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, alpha);
        renderer.addVertexWithUV(x - width, y, z + width, minU, maxV);
        renderer.addVertexWithUV(x + width, y, z + width, minU, minV);
        renderer.addVertexWithUV(x + width, y, z - width, maxU, minV);
        renderer.addVertexWithUV(x - width, y, z - width, maxU, maxV);
    }

    @Override
    public int getFXLayer()
    {
        return 2;
    }
}