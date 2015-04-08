package com.mrnobody.morecommands.command.server;

import java.text.DecimalFormat;

import net.minecraft.command.ICommandSender;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.ServerCommand;
import com.mrnobody.morecommands.command.CommandBase.Requirement;
import com.mrnobody.morecommands.command.CommandBase.ServerType;
import com.mrnobody.morecommands.wrapper.CommandException;
import com.mrnobody.morecommands.wrapper.CommandSender;
import com.mrnobody.morecommands.wrapper.Player;

import cpw.mods.fml.relauncher.Side;

@Command(
		name = "position",
		description = "command.position.description",
		example = "command.position.example",
		syntax = "command.position.syntax",
		videoURL = "command.position.videoURL"
		)
public class CommandPosition extends ServerCommand {

	@Override
	public String getCommandName() {
		return "position";
	}

	@Override
	public String getUsage() {
		return "command.position.syntax";
	}

	@Override
	public void execute(CommandSender sender, String[] params) throws CommandException {
		DecimalFormat f = new DecimalFormat("#.##");
		
		sender.sendStringMessage("Your current position is:"
				+ " X = " + f.format(sender.getPosition().getX())
				+ "; Y = " + f.format(sender.getPosition().getY())
				+ "; Z = " + f.format(sender.getPosition().getZ()));
	}

	@Override
	public Requirement[] getRequirements() {
		return new Requirement[0];
	}
	
	@Override
	public void unregisterFromHandler() {}

	@Override
	public ServerType getAllowedServerType() {
		return ServerType.ALL;
	}
	
	@Override
	public int getPermissionLevel() {
		return 0;
	}
	
	@Override
	public boolean canSenderUse(ICommandSender sender) {
		return true;
	}
}
