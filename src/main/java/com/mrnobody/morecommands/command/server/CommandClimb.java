package com.mrnobody.morecommands.command.server;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.ServerCommand;
import com.mrnobody.morecommands.core.MoreCommands;
import com.mrnobody.morecommands.packet.server.S02PacketClimb;
import com.mrnobody.morecommands.util.ServerPlayerSettings;
import com.mrnobody.morecommands.wrapper.CommandException;
import com.mrnobody.morecommands.wrapper.CommandSender;

@Command(
		name = "climb",
		description = "command.climb.description",
		example = "command.climb.example",
		syntax = "command.climb.syntax",
		videoURL = "command.climb.videoURL"
		)
public class CommandClimb extends ServerCommand {
	private Map<EntityPlayerMP, Boolean> playerClimbMapping = new HashMap<EntityPlayerMP, Boolean>();

	@Override
	public String getCommandName() {
		return "climb";
	}

	@Override
	public String getUsage() {
		return "command.climb.usage";
	}

	@Override
	public void execute(CommandSender sender, String[] params)throws CommandException {
		EntityPlayerMP player = (EntityPlayerMP) sender.getMinecraftISender();
		ServerPlayerSettings ability = ServerPlayerSettings.playerSettingsMapping.get(sender.getMinecraftISender());
    		
    	boolean allowClimb = false;
    	boolean success = false;
    		
        if (params.length >= 1) {
        	if (params[0].equalsIgnoreCase("true")) {allowClimb = true; success = true;}
        	else if (params[0].equalsIgnoreCase("false")) {allowClimb = false; success = true;}
        	else if (params[0].equalsIgnoreCase("0")) {allowClimb = false; success = true;}
        	else if (params[0].equalsIgnoreCase("1")) {allowClimb = true; success = true;}
        	else if (params[0].equalsIgnoreCase("on")) {allowClimb = true; success = true;}
        	else if (params[0].equalsIgnoreCase("off")) {allowClimb = false; success = true;}
    		else if (params[0].equalsIgnoreCase("enable")) {allowClimb = true; success = true;}
    		else if (params[0].equalsIgnoreCase("disable")) {allowClimb = false; success = true;}
        	else {success = false;}
        }
        else {allowClimb = !ability.climb; success = true;}
        	
        if (success) {
        	ability.climb = allowClimb;
        	S02PacketClimb packet = new S02PacketClimb();
        	packet.allowClimb = allowClimb;
        	MoreCommands.getMoreCommands().getNetwork().sendTo(packet, player);
        }
        	
        sender.sendLangfileMessage(success ? allowClimb ? "command.climb.on" : "command.climb.off" : "command.climb.failure", new Object[0]);
	}
	
	@Override
	public Requirement[] getRequirements() {
		return new Requirement[] {Requirement.MODDED_CLIENT, Requirement.PATCH_ENTITYCLIENTPLAYERMP};
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
