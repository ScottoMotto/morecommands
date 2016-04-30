package com.mrnobody.morecommands.command.server;

import java.lang.reflect.Field;
import java.util.Random;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.CommandRequirement;
import com.mrnobody.morecommands.command.ServerCommandProperties;
import com.mrnobody.morecommands.command.StandardCommand;
import com.mrnobody.morecommands.core.MoreCommands.ServerType;
import com.mrnobody.morecommands.event.EventHandler;
import com.mrnobody.morecommands.event.Listeners.EventListener;
import com.mrnobody.morecommands.util.ObfuscatedNames.ObfuscatedField;
import com.mrnobody.morecommands.util.ReflectionHelper;
import com.mrnobody.morecommands.util.ServerPlayerSettings;
import com.mrnobody.morecommands.wrapper.CommandException;
import com.mrnobody.morecommands.wrapper.CommandSender;
import com.mrnobody.morecommands.wrapper.World;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

@Command(
		name = "instantgrow",
		description = "command.instantgrow.description",
		example = "command.instantgrow.example",
		syntax = "command.instantgrow.syntax",
		videoURL = "command.instantgrow.videoURL"
		)
public class CommandInstantgrow extends StandardCommand implements ServerCommandProperties, EventListener<PlaceEvent> {
	private final Field field_149877_a = ReflectionHelper.getField(ObfuscatedField.BlockStem_crop);
	
	public CommandInstantgrow() {
		EventHandler.PLACE.register(this);
	}

	@Override
	public void onEvent(PlaceEvent event) {
		if (event.getPlayer() instanceof EntityPlayerMP && getPlayerSettings((EntityPlayerMP) event.getPlayer()).instantgrow) 
			this.growPlant(new World(event.getWorld()), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new Random());
	}
	
	@Override
	public String getCommandName() {
		return "instantgrow";
	}

	@Override
	public String getCommandUsage() {
		return "command.instantgrow.syntax";
	}

	@Override
	public void execute(CommandSender sender, String[] params) throws CommandException {
		ServerPlayerSettings settings = getPlayerSettings(getSenderAsEntity(sender.getMinecraftISender(), EntityPlayerMP.class));
    	
		try {settings.instantgrow = parseTrueFalse(params, 0, settings.instantgrow);}
		catch (IllegalArgumentException ex) {throw new CommandException("command.instantgrow.failure", sender);}
		
		sender.sendLangfileMessage(settings.instantgrow ? "command.instantgrow.on" : "command.instantgrow.off");
	}
	
	private void growPlant(World world, int x, int y, int z, Random rand) {
		Block block = world.getBlock(x, y, z);
		
		if (block instanceof BlockSapling) {
			((BlockSapling) block).grow(world.getMinecraftWorld(), new BlockPos(x, y, z), ((BlockSapling) block).getStateFromMeta(8), rand);
		}
		else if (block instanceof BlockCrops) {
			world.setBlockMeta(new BlockPos(x, y, z), 7);
		}
		else if (block instanceof BlockCactus || block instanceof BlockReed) {
			int length = 1;
			
			while (true) {
				int blen = length;
				
				if (world.getBlock(x, y + length, z) == block) length++;
				if (world.getBlock(x, y - length, z) == block) length++;
				
				if (blen == length) break;
			}
			
			if (length < 3) {
				for (int i = 0; i <= 3 - length; i++) {
					world.setBlock(new BlockPos(x, y + i, z), block);
				}
			}
		}
		else if (block instanceof BlockStem) {
			world.setBlockMeta(new BlockPos(x, y, z), 7);
			Block stemBlock = ReflectionHelper.get(ObfuscatedField.BlockStem_crop, field_149877_a, (BlockStem) block);
			
			if (stemBlock != null) {
				if (world.getBlock(x - 1, y, z) == stemBlock) return;
                if (world.getBlock(x + 1, y, z) == stemBlock) return;
                if (world.getBlock(x, y, z - 1) == stemBlock) return;
                if (world.getBlock(x, y, z + 1) == stemBlock) return;

                int i = rand.nextInt(4);
                int j = x;
                int k = z;

                if (i == 0) j = x - 1;
                if (i == 1) ++j;
                if (i == 2) k = z - 1;
                if (i == 3) ++k;

                Block b = world.getBlock(j, y - 1, k);

                if (world.getMinecraftWorld().isAirBlock(new BlockPos(j, y, k)) && (b.canSustainPlant(
                    world.getMinecraftWorld().getBlockState(new BlockPos(j, y - 1, k)), world.getMinecraftWorld(), 
                   	new BlockPos(j, y - 1, k), EnumFacing.UP, (BlockStem) block) || b == Blocks.dirt || b == Blocks.grass))
                {
                    world.setBlock(new BlockPos(j, y, k), stemBlock);
                }
			}
		}
	}
	
	@Override
	public CommandRequirement[] getRequirements() {
		return new CommandRequirement[0];
	}

	@Override
	public ServerType getAllowedServerType() {
		return ServerType.ALL;
	}
	
	@Override
	public int getDefaultPermissionLevel() {
		return 2;
	}
	
	@Override
	public boolean canSenderUse(String commandName, ICommandSender sender, String[] params) {
		return isSenderOfEntityType(sender, EntityPlayerMP.class);
	}
}
