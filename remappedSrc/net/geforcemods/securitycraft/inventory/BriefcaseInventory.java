package net.geforcemods.securitycraft.inventory;

//import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.util.NbtType;
//import net.geforcemods.securitycraft.SecurityCraft;
//import net.geforcemods.securitycraft.network.server.UpdateNBTTagOnServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
//import net.minecraftforge.common.util.Constants;
//import net.minecraftforge.fml.DistExecutor;

public class BriefcaseInventory implements Inventory {

	public static final int SIZE = 12;
	private final ItemStack briefcase;
	private DefaultedList<ItemStack> briefcaseInventory = DefaultedList.<ItemStack>ofSize(SIZE, ItemStack.EMPTY);

	public BriefcaseInventory(ItemStack briefcaseItem) {
		briefcase = briefcaseItem;

		if (!briefcase.hasNbt())
			briefcase.setNbt(new NbtCompound());

		readFromNBT(briefcase.getNbt());
	}

	@Override
	public int size() {
		return SIZE;
	}

	@Override
	public ItemStack getStack(int index) {
		return briefcaseInventory.get(index);
	}

	public void readFromNBT(NbtCompound tag) {
		NbtList items = tag.getList("ItemInventory", NbtType.COMPOUND);

		for(int i = 0; i < items.size(); i++) {
			NbtCompound item = items.getCompound(i);
			int slot = item.getInt("Slot");

			if(slot < size())
				briefcaseInventory.set(slot, ItemStack.fromNbt(item));
		}
	}

	public void writeToNBT(NbtCompound tag) {
		NbtList items = new NbtList();

		for(int i = 0; i < size(); i++)
			if(getStack(i) != null) {
				NbtCompound item = new NbtCompound();
				item.putInt("Slot", i);
				getStack(i).writeNbt(item);

				items.add(item);
			}

		tag.put("ItemInventory", items);
//		DistExecutor.runWhenOn(EnvType.CLIENT, () -> () -> SecurityCraft.channel.sendToServer(new UpdateNBTTagOnServer(briefcase))); // TODO
	}

	@Override
	public ItemStack removeStack(int index, int size) {
		ItemStack stack = getStack(index);

		if(!stack.isEmpty())
			if(stack.getCount() > size) {
				stack = stack.split(size);
				markDirty();
			}
			else
				setStack(index, ItemStack.EMPTY);

		return stack;
	}

	@Override
	public ItemStack removeStack(int index) {
		ItemStack stack = getStack(index);
		setStack(index, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setStack(int index, ItemStack itemStack) {
		briefcaseInventory.set(index, itemStack);

		if(!itemStack.isEmpty() && itemStack.getCount() > getMaxCountPerStack())
			itemStack.setCount(getMaxCountPerStack());

		markDirty();
	}

	@Override
	public int getMaxCountPerStack() {
		return 64;
	}

	@Override
	public void markDirty() {
		for(int i = 0; i < size(); i++)
			if(!getStack(i).isEmpty() && getStack(i).getCount() == 0)
				briefcaseInventory.set(i, ItemStack.EMPTY);

		writeToNBT(briefcase.getNbt());
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void onOpen(PlayerEntity player) {}

	@Override
	public void onClose(PlayerEntity player) {}

	@Override
	public boolean isValid(int index, ItemStack itemStack) {
		return true;
	}

	@Override
	public void clear() {
		for(int i = 0; i < SIZE; i++)
			briefcaseInventory.set(i, ItemStack.EMPTY);
	}

	@Override
	public boolean isEmpty()
	{
		for(ItemStack stack : briefcaseInventory)
			if(!stack.isEmpty())
				return false;

		return true;
	}
}
