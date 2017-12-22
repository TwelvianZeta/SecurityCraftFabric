package net.geforcemods.securitycraft.network.packets;

import io.netty.buffer.ByteBuf;
import net.geforcemods.securitycraft.api.CustomizableSCTE;
import net.geforcemods.securitycraft.misc.EnumCustomModules;
import net.geforcemods.securitycraft.util.BlockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSAddModules implements IMessage{

	private int x, y, z;
	private ItemStack[] modules;
	private int arrayLength;

	public PacketSAddModules(){

	}

	public PacketSAddModules(int x, int y, int z, ItemStack... modules){
		this.x = x;
		this.y = y;
		this.z = z;
		this.modules = modules;
		arrayLength = modules.length;
	}

	@Override
	public void toBytes(ByteBuf par1ByteBuf) {
		par1ByteBuf.writeInt(x);
		par1ByteBuf.writeInt(y);
		par1ByteBuf.writeInt(z);
		par1ByteBuf.writeInt(arrayLength);
		for(ItemStack stack : modules)
			ByteBufUtils.writeItemStack(par1ByteBuf, stack);
	}

	@Override
	public void fromBytes(ByteBuf par1ByteBuf) {
		x = par1ByteBuf.readInt();
		y = par1ByteBuf.readInt();
		z = par1ByteBuf.readInt();
		arrayLength = par1ByteBuf.readInt();
		for(int i = 0; i < arrayLength; i++){
			if(modules == null)
				modules = new ItemStack[arrayLength];

			modules[i] = ByteBufUtils.readItemStack(par1ByteBuf);
		}
	}

	public static class Handler extends PacketHelper implements IMessageHandler<PacketSAddModules, IMessage> {

		@Override
		public IMessage onMessage(PacketSAddModules packet, MessageContext context) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				BlockPos pos = BlockUtils.toPos(packet.x, packet.y, packet.z);
				ItemStack[] modules = packet.modules;
				EntityPlayer par1EntityPlayer = context.getServerHandler().playerEntity;

				if(getWorld(par1EntityPlayer).getTileEntity(pos) != null && getWorld(par1EntityPlayer).getTileEntity(pos) instanceof CustomizableSCTE)
					for(ItemStack module : modules)
						if(!((CustomizableSCTE) getWorld(par1EntityPlayer).getTileEntity(pos)).hasModule(EnumCustomModules.getModuleFromStack(module)))
							((CustomizableSCTE) getWorld(par1EntityPlayer).getTileEntity(pos)).insertModule(module);
			});

			return null;
		}
	}


}
