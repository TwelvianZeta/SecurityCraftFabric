package net.geforcemods.securitycraft.tileentity;

import net.geforcemods.securitycraft.SCContent;
import net.geforcemods.securitycraft.api.CustomizableTileEntity;
import net.geforcemods.securitycraft.api.Option;
import net.geforcemods.securitycraft.api.Option.BooleanOption;
import net.geforcemods.securitycraft.api.Option.DoubleOption;
import net.geforcemods.securitycraft.blocks.SecurityCameraBlock;
import net.geforcemods.securitycraft.misc.ModuleType;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;

public class SecurityCameraTileEntity extends CustomizableTileEntity {

	private final double CAMERA_SPEED = 0.0180D;
	public double cameraRotation = 0.0D;
	public boolean addToRotation = true;
	public boolean down = false, downSet = false;
	public float lastPitch = Float.MAX_VALUE;
	public float lastYaw = Float.MAX_VALUE;
	private DoubleOption rotationSpeedOption = new DoubleOption("rotationSpeed", CAMERA_SPEED, 0.0100D, 0.0250D, 0.001D);
	private BooleanOption shouldRotateOption = new BooleanOption("shouldRotate", true);
	private DoubleOption customRotationOption = new DoubleOption(this, "customRotation", cameraRotation, 1.55D, -1.55D, rotationSpeedOption.get(), true);

	public SecurityCameraTileEntity()
	{
		super(SCContent.teTypeSecurityCamera);
	}

	@Override
	public void tick(){
		super.tick();

		if(!downSet)
		{
			down = getCachedState().get(SecurityCameraBlock.FACING) == Direction.DOWN;
			downSet = true;
		}

		if(!shouldRotateOption.get())
		{
			cameraRotation = customRotationOption.get();
			return;
		}

		if(addToRotation && cameraRotation <= 1.55F)
			cameraRotation += rotationSpeedOption.get();
		else
			addToRotation = false;

		if(!addToRotation && cameraRotation >= -1.55F)
			cameraRotation -= rotationSpeedOption.get();
		else
			addToRotation = true;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag)
	{
		tag.putFloat("LastPitch", lastPitch);
		tag.putFloat("LastYaw", lastYaw);
		return super.writeNbt(tag);
	}

	@Override
	public void readNbt(BlockState state, NbtCompound tag)
	{
		super.readNbt(state, tag);
		lastPitch = tag.getFloat("LastPitch");
		lastYaw = tag.getFloat("LastYaw");
	}

	@Override
	public ModuleType[] acceptedModules(){
		return new ModuleType[] { ModuleType.REDSTONE, ModuleType.SMART };
	}

	@Override
	public Option<?>[] customOptions() {
		return new Option[]{ rotationSpeedOption, shouldRotateOption, customRotationOption };
	}

	@Override
	public void onModuleRemoved(ItemStack stack, ModuleType module)
	{
		super.onModuleRemoved(stack, module);

		if(module == ModuleType.REDSTONE)
			world.setBlockState(pos, getCachedState().with(SecurityCameraBlock.POWERED, false));
	}
}
