package lasermod.api.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lasermod.api.ILaserReceiver;
import lasermod.api.LaserInGame;
import lasermod.api.LaserType;
import lasermod.util.LaserUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public abstract class TileEntityMultiSidedReceiver extends TileEntityLaserDevice implements ILaserReceiver {

	public LaserInGame lastCombinedLaser;
	public ArrayList<LaserInGame> lasers = new ArrayList<LaserInGame>();
	
	/**
	 * Override and will be called when there is an update to the lasers
	 */
	public abstract void sendUpdateDescription();
	
	public abstract void onLaserPass(World world);
	
	public abstract void onLaserRemoved(World world);
	
	public boolean checkPowerOnSide(EnumFacing dir) {
		return true;
	}
	
	public List<EnumFacing> getInputDirections() {
		return Arrays.asList(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		this.lasers.clear();
		int amount = tag.getInteger("laserCount");
		 for(int i = 0; i < amount; ++i)
			 this.lasers.add(LaserInGame.readFromNBT(tag.getCompoundTag("laser" + i)));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setInteger("laserCount", this.lasers.size());
		
		 for(int i = 0; i < lasers.size(); ++i)
			 tag.setTag("laser" + i, this.lasers.get(i).writeToNBT(new NBTTagCompound()));
		 
		 return tag;
	}
	
	@Override
	public void tickLaserLogic() {
		
		if(!this.noLaserInputs()) {
			boolean change = false;
				
			for(EnumFacing dir : this.getInputDirections())
				if(this.checkPowerOnSide(dir) && !LaserUtil.isValidSourceOfPowerOnSide(this, dir))
					if(this.removeAllLasersFromSide(dir))
						change = true;

			if(change) {
				this.sendUpdateDescription();
				this.onLaserRemoved(this.world);
			}
					
		}
	}
	
	@Override
	public void tickLaserAction(boolean client) {
		
	}

	@Override
	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean canReceive(World world, BlockPos orginPos, EnumFacing dir, LaserInGame laserInGame) {
		return this.getInputDirections().contains(dir) && !Objects.equals(laserInGame, this.getLaserFromSide(dir));
	}

	@Override
	public void onLaserIncident(World world, BlockPos orginPos, EnumFacing dir, LaserInGame laserInGame) {
		this.addLaser(laserInGame, dir);
		this.onLaserPass(world);
		this.sendUpdateDescription();
		this.setUpdateRequired();
	}

	@Override
	public void removeLaser(World world, BlockPos orginPos, EnumFacing dir) {
		boolean flag = this.removeAllLasersFromSide(dir);
		
		if(flag) {
			this.onLaserRemoved(world);
			this.sendUpdateDescription();
			this.setUpdateRequired();
		}
	}

	@Override
	public List<LaserInGame> getInputLasers() {
		return this.lasers;
	}

	//** TileEntityMultiSidedReciever helper methods **//
	
	public LaserInGame getLaserFromSide(EnumFacing dir) {
		for(int i = 0; i < this.lasers.size(); ++i)
			if(this.lasers.get(i).getDirection() == dir.getOpposite())
				return this.lasers.get(i);
		return null;
	}
	
	public int getIndexOfLaserSide(EnumFacing dir) {
		for(int i = 0; i < this.lasers.size(); ++i)
			if(this.lasers.get(i).getDirection() == dir.getOpposite())
				return i;
		return -1;
	}
	
	public boolean addLaser(LaserInGame laserInGame, EnumFacing dir) {
		if(laserInGame == null)
			return false;
		
		int i = this.getIndexOfLaserSide(dir);
		if(i == -1)
			this.lasers.add(laserInGame);
		else
			this.lasers.set(i, laserInGame);
		return true;
	}
	
	public boolean removeAllLasersFromSide(EnumFacing side) {
		boolean change = false;
		for(int i = 0; i < lasers.size(); ++i) {
			if(this.lasers.get(i).getDirection() == side.getOpposite()) {
				this.lasers.remove(i);
				change = true;
			}
		}
		return change;
	}
	
	public boolean containsInputSide(EnumFacing dir) {
		for(int i = 0; i < this.lasers.size(); ++i)
			if(this.lasers.get(i).getDirection() == dir.getOpposite())
				return true;
		return false;
	}
	
	public boolean noLaserInputs() {
		return this.lasers.size() == 0;
	}
	
	public LaserInGame getCombinedOutputLaser(EnumFacing dir) {
		if(this.noLaserInputs())
			return null;
		
		ArrayList<LaserType> laserList = new ArrayList<LaserType>();
		for(LaserInGame lig : this.lasers)
			for(LaserType laser : lig.getLaserType()) 
				if(!laserList.contains(laser))
					laserList.add(laser);
		
		LaserInGame laserInGame = new LaserInGame(laserList);
		int red = 0;
		int green = 0;
		int blue = 0;
		
		double blendFactor = 1.0D / this.lasers.size();
		
		for(int i = 0; i < this.lasers.size(); ++i) {
			red += (int)(this.lasers.get(i).red * blendFactor);
			green += (int)(this.lasers.get(i).green * blendFactor);
			blue += (int)(this.lasers.get(i).blue * blendFactor);
		}
	
		laserInGame.red = red;
		laserInGame.green = green;
		laserInGame.blue = blue;
				
		double totalPower = 0.0D;
		for(LaserInGame laser : lasers)
			totalPower += laser.getStrength();
		
		laserInGame.setDirection(dir);
		laserInGame.setStrength(totalPower / lasers.size());
		
		return laserInGame;
	}
}
