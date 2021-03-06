package lasermod.tileentity;

import lasermod.api.base.TileEntityMultiSidedReciever;
import lasermod.block.BlockLuminousLamp;
import lasermod.network.PacketDispatcher;
import lasermod.network.packet.client.LuminousLampMessage;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class TileEntityLuminousLamp extends TileEntityMultiSidedReciever {
	
	@Override
	public void sendUpdateDescription() {
		PacketDispatcher.sendToAllAround(new LuminousLampMessage(this), this, 512);
	}

	@Override
	public void onLaserPass(World world) {
		world.setBlockState(this.pos, world.getBlockState(pos).withProperty(BlockLuminousLamp.ACTIVE, true));
	}

	@Override
	public void onLaserRemoved(World world) {
		if(this.lasers.size() == 0)
			world.setBlockState(this.pos, world.getBlockState(pos).withProperty(BlockLuminousLamp.ACTIVE, false));
	}
	
	@Override
	public Packet getDescriptionPacket() {
	    return PacketDispatcher.getPacket(new LuminousLampMessage(this));
	}
}
