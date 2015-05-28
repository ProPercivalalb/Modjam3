package lasermod.client.render.block;

import lasermod.api.LaserCollisionBoxes;
import lasermod.client.render.LaserRenderer;
import lasermod.tileentity.TileEntityBasicLaser;
import lasermod.util.LaserUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

/**
 * @author ProPercivalalb
 */
public class TileEntityBasicLaserRenderer extends TileEntitySpecialRenderer {

    public void renderBasicLaser(TileEntityBasicLaser basicLaser, double x, double y, double z, float tick) {
    	if(!basicLaser.getWorldObj().isBlockIndirectlyGettingPowered(basicLaser.xCoord, basicLaser.yCoord, basicLaser.zCoord))
    		return;
    	
    	GL11.glPushMatrix();
    	LaserRenderer.preLaserRender();

        AxisAlignedBB laserOutline = LaserUtil.getLaserOutline(basicLaser, basicLaser.getBlockMetadata(), x, y, z);
        LaserCollisionBoxes.addLaserCollision(laserOutline.getOffsetBoundingBox(basicLaser.xCoord, basicLaser.yCoord, basicLaser.zCoord).getOffsetBoundingBox(-x, -y, -z));
    	GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.4F);
    	LaserRenderer.drawBoundingBox(laserOutline);
    	LaserRenderer.drawBoundingBox(laserOutline.contract(0.1D, 0.1D, 0.1D));

    	LaserRenderer.postLaserRender();
        GL11.glPopMatrix();

    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        renderBasicLaser((TileEntityBasicLaser)tileEntity, x, y, z, tick);
    }
}
