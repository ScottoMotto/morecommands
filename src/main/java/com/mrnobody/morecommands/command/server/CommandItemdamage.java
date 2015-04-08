package com.mrnobody.morecommands.command.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;

import com.mrnobody.morecommands.command.Command;
import com.mrnobody.morecommands.command.ServerCommand;
import com.mrnobody.morecommands.util.GlobalSettings;
import com.mrnobody.morecommands.wrapper.CommandException;
import com.mrnobody.morecommands.wrapper.CommandSender;

@Command(
		name = "itemdamage",
		description = "command.itemdamage.description",
		example = "command.itemdamage.example",
		syntax = "command.itemdamage.syntax",
		videoURL = "command.itemdamage.videoURL"
		)
public class CommandItemdamage extends ServerCommand {
	private final Map<Item, Integer> damgeValues = new HashMap<Item, Integer>();
	
	public CommandItemdamage() {
		Iterator<Item> items = Item.itemRegistry.iterator();
		
		while (items.hasNext()) {
			Item item = items.next();
			this.damgeValues.put(item, item.getMaxDamage());
		}
	}
	
	
	@Override
	public String getCommandName() {
		return "itemdamage";
	}

	@Override
	public String getUsage() {
		return "command.itemdamage.syntax";
	}

	@Override
	public void execute(CommandSender sender, String[] params) throws CommandException {
    	boolean damage = false;
    	boolean success = false;
    	
    	if (params.length >= 1) {
    		if (params[0].equalsIgnoreCase("true")) {damage = true; success = true;}
    		else if (params[0].equalsIgnoreCase("false")) {damage = false; success = true;}
    		else if (params[0].equalsIgnoreCase("0")) {damage = false; success = true;}
    		else if (params[0].equalsIgnoreCase("1")) {damage = true; success = true;}
    		else if (params[0].equalsIgnoreCase("on")) {damage = true; success = true;}
    		else if (params[0].equalsIgnoreCase("off")) {damage = false; success = true;}
    		else if (params[0].equalsIgnoreCase("enable")) {damage = true; success = true;}
    		else if (params[0].equalsIgnoreCase("disable")) {damage = false; success = true;}
    		else {success = false;}
    	}
    	else {damage = !GlobalSettings.itemdamage; success = true;}
    	
    	if (success) {
    		if (damage) {
    			GlobalSettings.itemdamage = true;
    			Iterator<Item> items = this.damgeValues.keySet().iterator();
    			
    			while (items.hasNext()) {
    				Item item = items.next();
    				
    				item.setMaxDamage(this.damgeValues.get(item));
    			}
    		}
    		else {
    			GlobalSettings.itemdamage = false;
    			Iterator<Item> items = this.damgeValues.keySet().iterator();
    			
    			while (items.hasNext()) items.next().setMaxDamage(-1);
    		}
    	}
    	
    	sender.sendLangfileMessage(success ? damage ? "command.itemdamage.on" : "command.itemdamage.off" : "command.itemdamage.failure", new Object[0]);
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
		return 2;
	}
	
	@Override
	public boolean canSenderUse(ICommandSender sender) {
		return true;
	}
}
