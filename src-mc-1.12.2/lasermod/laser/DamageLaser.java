package lasermod.laser;

import java.util.List;

import lasermod.api.LaserType;
import lasermod.util.BlockActionPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;

/**
 * @author ProPercivalalb
 */
public class DamageLaser extends LaserType {

	public static DamageSource laserDamage = new DamageSourceLaser().setDamageBypassesArmor();
	
	@Override
	public void performActionOnEntitiesServer(List<Entity> entities, EnumFacing dir) {
		for(Entity entity : entities) {
			entity.attackEntityFrom(this.laserDamage, 3);
		}
	}
	
	@Override
	public void performActionOnEntitiesClient(List<Entity> entities, EnumFacing dir) {
		
	}
	
	@Override
	public void performActionOnEntitiesBoth(List<Entity> entities, EnumFacing dir) {
		
	}

	@Override
	public boolean shouldRenderLaser(EntityPlayer player, EnumFacing dir) {
		return true;
	}

	@Override
	public void actionOnBlock(BlockActionPos action) {
		
	}
}
