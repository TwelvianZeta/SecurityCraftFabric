package net.geforcemods.securitycraft.tileentity;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.api.SecurityCraftTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class BlockPocketTileEntity extends SecurityCraftTileEntity
{
	private BlockPocketManagerTileEntity manager;
	private BlockPos managerPos;

	public BlockPocketTileEntity()
	{
		super(SCContent.teTypeBlockPocket);
	}

	public void setManager(BlockPocketManagerTileEntity manager)
	{
		this.manager = manager;
		managerPos = manager.getPos();
	}

	public void removeManager()
	{
		managerPos = null;
		manager = null;
	}

	public BlockPocketManagerTileEntity getManager()
	{
		return manager;
	}

	@Override
	public void tick()
	{
		super.tick();

		if(manager == null && managerPos != null)
		{
			BlockEntity te = world.getBlockEntity(managerPos);

			if(te instanceof BlockPocketManagerTileEntity)
				manager = (BlockPocketManagerTileEntity)te;
		}
	}

	@Override
	public void onTileEntityDestroyed()
	{
		super.onTileEntityDestroyed();

		BlockState state = world.getBlockState(pos);

		if(manager != null && state.getBlock() != SCContent.BLOCK_POCKET_WALL && state.getBlock() != SCContent.REINFORCED_CHISELED_CRYSTAL_QUARTZ && state.getBlock() != SCContent.REINFORCED_CRYSTAL_QUARTZ_PILLAR)
			manager.disableMultiblock();
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		if(manager != null)
			tag.putLong("ManagerPos", manager.getPos().asLong());
		return super.writeNbt(tag);
	}

	@Override
	public void readNbt(BlockState state, NbtCompound tag)
	{
		super.readNbt(state, tag);

		if(tag.contains("ManagerPos"))
			managerPos = BlockPos.fromLong(tag.getLong("ManagerPos"));
	}
}
