package lasermod.block;

import lasermod.LaserMod;
import lasermod.tileentity.TileEntityLuminousLamp;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
public class BlockLuminousLamp extends BlockContainer {

	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	public BlockLuminousLamp() {
		super(Material.GLASS);
		this.setHardness(1.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false));
		this.setCreativeTab(LaserMod.TAB_LASER);
	}
	

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityLuminousLamp(); 
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return this.getMetaFromState(state) * 15;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) { 
		return false; 
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
	    return EnumBlockRenderType.MODEL;
	}	
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

	@SideOnly(Side.CLIENT)
    public IBlockState getStateForEntityRender(IBlockState state) {
        return this.getDefaultState().withProperty(ACTIVE, false);
    }

	@Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, meta % 2 == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {ACTIVE});
    }
}
