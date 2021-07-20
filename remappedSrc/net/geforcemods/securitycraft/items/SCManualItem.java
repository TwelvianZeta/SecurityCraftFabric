package net.geforcemods.securitycraft.items;

//import net.geforcemods.securitycraft.SecurityCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SCManualItem extends Item {

	public SCManualItem(Settings properties){
		super(properties);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
//		if(world.isClient) // TODO
//			SecurityCraft.proxy.displaySCManualGui();

		return TypedActionResult.pass(player.getStackInHand(hand));
	}

	@Override
	public void inventoryTick(ItemStack par1ItemStack, World world, Entity entity, int slotIndex, boolean isSelected){
		if(par1ItemStack.getNbt() == null){
			NbtList bookPages = new NbtList();

			par1ItemStack.setSubNbt("pages", bookPages);
			par1ItemStack.setSubNbt("author", NbtString.of("Geforce"));
			par1ItemStack.setSubNbt("title", NbtString.of("SecurityCraft"));
		}
	}

}
