package net.geforcemods.securitycraft.tileentity;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.api.OwnableTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;

public class TrackMineTileEntity extends OwnableTileEntity
{
	private boolean active = true;

	public TrackMineTileEntity()
	{
		super(SCContent.teTypeTrackMine);
	}

	public void activate()
	{
		if(!active)
		{
			active = true;
			markDirty();
		}
	}

	public void deactivate()
	{
		if(active)
		{
			active = false;
			markDirty();
		}
	}

	public boolean isActive()
	{
		return active;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		tag.putBoolean("TrackMineEnabled", active);
		return super.writeNbt(tag);
	}

	@Override
	public void readNbt(BlockState state, NbtCompound tag)
	{
		super.readNbt(state, tag);
		active = tag.getBoolean("TrackMineEnabled");
	}
}
