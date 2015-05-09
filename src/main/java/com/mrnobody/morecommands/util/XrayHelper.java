package com.mrnobody.morecommands.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import com.mrnobody.morecommands.handler.EventHandler;
import com.mrnobody.morecommands.handler.Listeners.TwoEventListener;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameData;

/**
 * The class which handles everything what has to do with xray
 * 
 * @author MrNobody98
 *
 */
public class XrayHelper implements TwoEventListener<TickEvent, RenderWorldLastEvent> {
	private static XrayHelper xrayHelper;
	
	public static XrayHelper getInstance() {
		return XrayHelper.xrayHelper;
	}
	
	public static void init() {
		XrayHelper.xrayHelper = new XrayHelper();
	}
	
	public int localPlayerX, localPlayerY, localPlayerZ;
	public boolean xrayEnabled = false;
	public int blockRadius = 32;
	public List<Block> blockList = new ArrayList<Block>();
	public Map<Block, BlockSettings> blockMapping = new HashMap<Block, BlockSettings>();
	
	private XrayClientTick clTick;
	private XrayRenderTick rTick;
	private XrayConfGui confGUI = new XrayConfGui(Minecraft.getMinecraft(), this);
	
	public static class BlockSettings {
		public Block block;
		public Color color;
		public boolean draw;
		
		public BlockSettings(Block block, Color color, boolean draw){
			this.block = block;
			this.color = color;
			this.draw = draw;
		}
		
		public void disable() {this.draw = false;}
		
		public void enable() {this.draw = true;}
	}
	
	public static class BlockPosition {
		public int x, y, z;
		public Color color;
		
		public BlockPosition(int bx, int by, int bz, Color c){
			this.x = bx;
			this.y = by;
			this.z = bz;
			this.color = c;
		}
	}
	
	public void onEvent1(TickEvent event) {
		if (event instanceof TickEvent.ClientTickEvent)
			this.clTick.tick((TickEvent.ClientTickEvent) event);
	}
	  
	public void onEvent2(RenderWorldLastEvent event) {
		this.rTick.onRenderEvent(event);
	}
	
	private XrayHelper() {
		this.clTick = new XrayClientTick(this);
		this.rTick = new XrayRenderTick(this);
		
		EventHandler.TICK.getHandler().register(this, true);
		EventHandler.RENDERWORLD.getHandler().register(this, false);
		
		Block block;
		Iterator<Block> blocks = GameData.getBlockRegistry().iterator();
		
		while (blocks.hasNext()) {
			block = blocks.next();
			blockList.add(block);
			this.blockMapping.put(block, new BlockSettings(block, Color.WHITE, false));
		}
	}
	
	public void showConfig() {
		this.confGUI.displayGUI();
	}
	
	public void changeSettings(int blockRadius, boolean enableXray) {
		this.blockRadius = blockRadius;
		this.xrayEnabled = enableXray;
	}
}
