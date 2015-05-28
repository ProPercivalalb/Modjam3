package lasermod.client.render.block;

import lasermod.api.LaserCollisionBoxes;
import lasermod.api.LaserInGame;
import lasermod.client.render.LaserRenderer;
import lasermod.helper.ClientHelper;
import lasermod.tileentity.TileEntityColourConverter;
import lasermod.tileentity.TileEntitySmallColourConverter;
import lasermod.util.LaserUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

/**
 * @author ProPercivalalb
 */
public class TileEntitySmallColourConverterRenderer extends TileEntitySpecialRenderer {

    public void renderColourConverter(TileEntitySmallColourConverter colourConverter, double x, double y, double z, float tick) {
    	if(colourConverter.getOutputLaser(colourConverter.getBlockMetadata()) == null)
    		return;
    	LaserInGame laserInGame = colourConverter.getOutputLaser(colourConverter.getBlockMetadata());
    	float alpha = laserInGame.shouldRenderLaser(ClientHelper.getPlayer());

    	if(alpha == 0.0F)
    		return;
    	
    	GL11.glPushMatrix();
    	LaserRenderer.preLaserRender();
    	
		AxisAlignedBB boundingBox = LaserUtil.getLaserOutline(colourConverter, colourConverter.getBlockMetadata(), x, y, z);
		LaserCollisionBoxes.addLaserCollision(boundingBox.getOffsetBoundingBox(colourConverter.xCoord, colourConverter.yCoord, colourConverter.zCoord).getOffsetBoundingBox(-x, -y, -z));
    	GL11.glColor4f(laserInGame.red / 255F, laserInGame.green / 255F, laserInGame.blue / 255F, alpha);
    	LaserRenderer.drawBoundingBox(boundingBox);
    	LaserRenderer.drawBoundingBox(boundingBox.contract(0.1D, 0.1D, 0.1D));
         
    	LaserRenderer.postLaserRender();
        GL11.glPopMatrix();

    }
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        renderColourConverter((TileEntitySmallColourConverter)tileEntity, x, y, z, tick);
    }
}
