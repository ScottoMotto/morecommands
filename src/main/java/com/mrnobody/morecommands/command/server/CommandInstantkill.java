package com.mrnobody.morecommands.command.server;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.ServerCommand;
import com.mrnobody.morecommands.command.CommandBase.Requirement;
import com.mrnobody.morecommands.command.CommandBase.ServerType;
import com.mrnobody.morecommands.patch.EntityPlayerMP;
import com.mrnobody.morecommands.util.ServerPlayerSettings;
import com.mrnobody.morecommands.wrapper.CommandException;
import com.mrnobody.morecommands.wrapper.CommandSender;
import com.mrnobody.morecommands.wrapper.Player;

import cpw.mods.fml.relauncher.Side;

@Command(
		name = "instantkill",
		description = "command.instantkill.description",
		example = "command.instantkill.example",
		syntax = "command.instantkill.syntax",
		videoURL = "command.instantkill.videoURL"
		)
public class CommandInstantkill extends ServerCommand {
	@Override
	public String getCommandName() {
		return "instantkill";
	}

	@Override
	public String getUsage() {
		return "command.instantkill.syntax";
	}

	@Override
	public void execute(CommandSender sender, String[] params)throws CommandException {
		EntityPlayerMP player = (EntityPlayerMP) sender.getMinecraftISender();
		
		ServerPlayerSettings ability = ServerPlayerSettings.playerSettingsMapping.get(sender.getMinecraftISender());
    		
        boolean instant = false;
        boolean success = false;
        	
        if (params.length >= 1) {
        	if (params[0].equalsIgnoreCase("true")) {instant = true; success = true;}
        	else if (params[0].equalsIgnoreCase("false")) {instant = false; success = true;}
        	else if (params[0].equalsIgnoreCase("0")) {instant = false; success = true;}
        	else if (params[0].equalsIgnoreCase("1")) {instant = true; success = true;}
        	else if (params[0].equalsIgnoreCase("on")) {instant = true; success = true;}
        	else if (params[0].equalsIgnoreCase("off")) {instant = false; success = true;}
    		else if (params[0].equalsIgnoreCase("enable")) {instant = true; success = true;}
    		else if (params[0].equalsIgnoreCase("disable")) {instant = false; success = true;}
        	else {success = false;}
        }
        else {instant = !player.getInstantkill(); success = true;}
        	
        if (success) player.setInstantkill(instant);
        	
        sender.sendLangfileMessage(success ? instant ? "command.instantkill.on" : "command.instantkill.off" : "command.instantkill.failure", new Object[0]);
	}
	
	@Override
	public Requirement[] getRequirements() {
		return new Requirement[] {Requirement.PATCH_ENTITYPLAYERMP};
	}
	
	@Override
	public void unregisterFromHandler() {}

	@Override
	public ServerType getAllowedServerType() {
		return ServerType.ALL;
	}
	
	@Override
	public int getPermissionLevel() {
		return 2;
	}
	
	@Override
	public boolean canSenderUse(ICommandSender sender) {
		return sender instanceof EntityPlayerMP;
	}
}
