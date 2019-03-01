package net.comorevi.NoExplodeNoIgniteForNukkit;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLava;
import cn.nukkit.block.BlockWater;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.util.LinkedHashMap;

public class NoExplodeNoIgnite extends PluginBase implements Listener {

    private static final String prefix = TextFormat.RED + "NoExplodeNoIgnite" + TextFormat.WHITE + ">> " + TextFormat.GREEN;
    private boolean isEnableCancelExplode = true;
    private boolean isEnableCancelIgnite = true;
    private boolean isEnableCancelSpreadLava = true;
    private boolean isEnableCancelSpreadWater = true;
	
	@Override
	public void onEnable() {
		if (!this.getDataFolder().exists()) getDataFolder().mkdirs();
        Config config = new Config(
                new File(this.getDataFolder(), "config.yml"),
                Config.YAML,
                new LinkedHashMap<String, Object>() {
                    {
                        put("CancelExplode", true);
                        put("CancelIgnite", true);
                        put("CancelSpreadLava", true);
                        put("CancelSpreadWater", false);
                    }
                });
        config.save();
        this.isEnableCancelExplode = config.getBoolean("CancelExplode");
        this.isEnableCancelIgnite = config.getBoolean("CancelIgnite");
        this.isEnableCancelSpreadLava = config.getBoolean("CancelSpreadLava");
        this.isEnableCancelSpreadWater = config.getBoolean("CancelSpreadWater");

		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
	    if (!isEnableCancelExplode) return;
	    event.setCancelled();
		getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "エンティティの爆発をキャンセルしました。");
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent event) {
	    if (!isEnableCancelIgnite) return;

		switch (event.getCause()) {
			case LAVA: 
			case SPREAD:
				event.setCancelled();
				getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "ブロックへの着火をキャンセルしました。");
				break;
			case FLINT_AND_STEEL:
				event.setCancelled();
				getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "ブロックへの着火をキャンセルしました。\n" + TextFormat.YELLOW + event.getEntity().getName() + TextFormat.WHITE + "が火打石を使用しました。");
				break;
		}
	}

	@EventHandler
    public void onBlockSpread(BlockUpdateEvent event) {
        Block block = event.getBlock();
        if (block instanceof BlockLava) {
            if (!isEnableCancelSpreadLava) return;
            event.setCancelled();
            getServer().broadcastMessage(NoExplodeNoIgnite.prefix + "マグマの拡大をキャンセルしました。\n - X:" + block.x + " ,Y:" + block.y + " ,Z:" + block.z + " , Level:" + block.level.getName());
        } else if (block instanceof BlockWater) {
            if (!isEnableCancelSpreadWater) return;
            event.setCancelled();
            //コメントはなし。無駄に反応している部分がある？
        }
    }

}