package net.comorevi.NoExplodeNoIgniteForNukkit;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.utils.TextFormat;

public class NoExplodeNoIgnite extends PluginBase implements Listener {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		event.setCancelled();
		getServer().broadcastMessage(TextFormat.RED + "NoExplodeNoIgnite" + TextFormat.WHITE + ">> " + TextFormat.GREEN + "エンティティの爆発をキャンセルしました。");
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
		switch (event.getCause()) {
			case LAVA: 
			case SPREAD:
				event.setCancelled();
				getServer().broadcastMessage(TextFormat.RED + "NoExplodeNoIgnite" + TextFormat.WHITE + ">> " + TextFormat.GREEN + "ブロックへの着火をキャンセルしました。");
				break;
			case FLINT_AND_STEEL:
				event.setCancelled();
				getServer().broadcastMessage(TextFormat.RED + "NoExplodeNoIgnite" + TextFormat.WHITE + ">> " + TextFormat.GREEN + "ブロックへの着火をキャンセルしました。\n" + TextFormat.YELLOW + event.getEntity().getName() + TextFormat.WHITE + "が火打石を使用しました。");
				break;
		}
	}
}