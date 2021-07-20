package net.geforcemods.securitycraft.api;

import net.geforcemods.securitycraft.SCContent;
//import net.geforcemods.securitycraft.SecurityCraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
//import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
//import net.geforcemods.securitycraft.network.server.RequestTEOwnableUpdate;

/**
 * Used to give this tile entity an owner
 */
public class OwnableTileEntity extends BlockEntity implements IOwnable {

	private Owner owner = new Owner();

	public OwnableTileEntity()
	{
		this(SCContent.teTypeOwnable);
	}

	public OwnableTileEntity(BlockEntityType<?> type)
	{
		super(type);
	}

	/**
	 * Writes a tile entity to NBT.
	 * @return
	 */
	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		super.writeNbt(tag);

		if(owner != null){
			tag.putString("owner", owner.getName());
			tag.putString("ownerUUID", owner.getUUID());
		}

		return tag;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readNbt(BlockState state, NbtCompound tag)
	{
		super.readNbt(state, tag);

		if (tag.contains("owner"))
			owner.setOwnerName(tag.getString("owner"));

		if (tag.contains("ownerUUID"))
			owner.setOwnerUUID(tag.getString("ownerUUID"));
	}

	@Override
	public NbtCompound toInitialChunkDataNbt()
	{
		return writeNbt(new NbtCompound());
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(pos, 1, toInitialChunkDataNbt());
	}

//	@Override
//	public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket packet) {
//		fromTag(getCachedState(), packet.getCompoundTag());
//	}

	@Override
	public Owner getOwner(){
		return owner;
	}

	@Override
	public void setOwner(String uuid, String name) {
		owner.set(uuid, name);
	}

//	@Override
//	public void onLoad()
//	{
//		if(world.isClient)
//			SecurityCraft.channel.sendToServer(new RequestTEOwnableUpdate(this));
//	}
}
