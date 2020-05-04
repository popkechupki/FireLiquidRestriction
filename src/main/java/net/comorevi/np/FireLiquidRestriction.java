package net.comorevi.np;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockLava;
import cn.nukkit.block.BlockWater;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.LiquidFlowEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerBucketFillEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;

public class FireLiquidRestriction extends PluginBase implements Listener {
    private static final int CONFIG_VERSION = 1;
    private Config config;

    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        config = new Config(new File("./plugins/FireLiquidRestriction", "config.yml"), Config.YAML);
        if (config.getInt("version") < CONFIG_VERSION) {
            getServer().getLogger().warning("[FireLiquidRestriction] Please delete old config file.");
            getServer().getPluginManager().disablePlugin(this);
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (config.getStringList("IgnoreWorlds").contains(event.getBlock().getLocation().getLevel().getName())) return;
        if (config.getBoolean("RestrictIgnite.force")) {
            event.setCancelled();
            sendMessage("RestrictIgnite");
        } else {
            switch (event.getCause()) {
                case LAVA:
                    if (config.getBoolean("ByLava")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case SPREAD:
                    if (config.getBoolean("ByFireSpread")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case FIREBALL:
                    if (config.getBoolean("ByFireBall")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case EXPLOSION:
                    if (config.getBoolean("ByExplosion")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case LIGHTNING:
                    if (config.getBoolean("ByLightning")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case FLINT_AND_STEEL:
                    if (config.getBoolean("ByFlintAndSteel")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onExplosion(EntityExplodeEvent event) {
        if (config.getStringList("IgnoreWorlds").contains(event.getPosition().getLevel().getName())) return;
        if (config.getBoolean("CancelExplosion.force")) {
            event.setCancelled();
            sendMessage("CancelExplosion");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (config.getStringList("IgnoreWorlds").contains(event.getBlock().getLocation().getLevel().getName())) return;
        if (config.getBoolean("RestrictPlace.force")) {
            if (event.getBlock().getId() != Block.LAVA && event.getBlock().getId() != Block.WATER) return;
            event.setCancelled();
            sendMessage("RestrictPlace");
        } else {
            switch (event.getBlock().getId()) {
                case Block.LAVA:
                    if (config.getBoolean("RestrictPlaceLava")) {
                        event.setCancelled();
                        sendMessage("RestrictPlace");
                    }
                    break;
                case Block.WATER:
                    if (config.getBoolean("RestrictPlaceWatar")) {
                        event.setCancelled();
                        sendMessage("RestrictPlace");
                    }
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (config.getStringList("IgnoreWorlds").contains(event.getPlayer().getLocation().getLevel().getName())) return;
        if (config.getBoolean("RestrictUsingBucket.force")) {
            event.setCancelled();
            sendMessage("RestrictUsingBucket");
        } else if (event.getBucket().getName().equals("Lava Bucket") && config.getBoolean("RestrictUsingLavaBucket")) {
            event.setCancelled();
            sendMessage("RestrictUsingBucket");
        } else if (event.getBucket().getName().equals("Water Bucket") && config.getBoolean("RestrictUsingWatarBucket")) {
            event.setCancelled();
            sendMessage("RestrictUsingBucket");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (config.getStringList("IgnoreWorlds").contains(event.getPlayer().getLocation().getLevel().getName())) return;
        if (config.getBoolean("RestrictUsingBucket.force")) {
            event.setCancelled();
            sendMessage("RestrictUsingBucket");
        } else if (event.getBlockClicked() instanceof BlockLava && config.getBoolean("RestrictUsingLavaBucket")) {
            event.setCancelled();
            sendMessage("RestrictUsingBucket");
        } else if (event.getBlockClicked() instanceof BlockWater && config.getBoolean("RestrictUsingWatarBucket")) {
            event.setCancelled();
            sendMessage("RestrictUsingBucket");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLiquidFlow(LiquidFlowEvent event) {
        if (config.getStringList("IgnoreWorlds").contains(event.getBlock().getLocation().getLevel().getName())) return;
        if (config.getBoolean("RestrictFlow.force") || event.getSource() instanceof BlockLava || event.getSource() instanceof BlockWater) {
            event.setCancelled();
            sendMessage("RestrictFlow");
        }
    }

    private void sendMessage(String configKey) {
        if (config.getBoolean(configKey+".message")) {
            Server.getInstance().broadcastMessage(TextFormat.RED+"FLR>>"+TextFormat.YELLOW+config.getString(configKey+".content"));
        }
    }
}
