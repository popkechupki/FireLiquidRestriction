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
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.util.LinkedHashMap;
import java.util.Map;

public class FireLiquidRestriction extends PluginBase implements Listener {
    private static final int CONFIG_VERSION = 1;

    @Override
    public void onEnable() {
        initConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (getConfig().getStringList("IgnoreWorlds").contains(event.getBlock().getLocation().getLevel().getName())) return;
        if (getConfig().getBoolean("RestrictIgnite")) {
            event.setCancelled();
            sendMessage("RestrictIgnite");
        } else {
            switch (event.getCause()) {
                case LAVA:
                    if (getConfig().getBoolean("ByLava")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case SPREAD:
                    if (getConfig().getBoolean("ByFireSpread")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case FIREBALL:
                    if (getConfig().getBoolean("ByFireBall")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case EXPLOSION:
                    if (getConfig().getBoolean("ByExplosion")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case LIGHTNING:
                    if (getConfig().getBoolean("ByLightning")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
                case FLINT_AND_STEEL:
                    if (getConfig().getBoolean("ByFlintAndSteel")) {
                        event.setCancelled();
                        sendMessage("RestrictIgnite");
                    }
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onExplosion(EntityExplodeEvent event) {
        if (getConfig().getStringList("IgnoreWorlds").contains(event.getPosition().getLevel().getName())) return;
        if (getConfig().getBoolean("CancelExplosion")) {
            event.setCancelled();
            sendMessage("CancelExplosion");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (getConfig().getStringList("IgnoreWorlds").contains(event.getBlock().getLocation().getLevel().getName())) return;
        if (getConfig().getBoolean("RestrictPlace")) {
            event.setCancelled();
            sendMessage("RestrictPlace");
        } else {
            switch (event.getBlock().getId()) {
                case Block.LAVA:
                    if (getConfig().getBoolean("RestrictPlaceLava")) {
                        event.setCancelled();
                        sendMessage("RestrictPlace");
                    }
                    break;
                case Block.WATER:
                    if (getConfig().getBoolean("RestrictPlaceWatar")) {
                        event.setCancelled();
                        sendMessage("RestrictPlace");
                    }
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (getConfig().getStringList("IgnoreWorlds").contains(event.getPlayer().getLocation().getLevel().getName())) return;
        if (getConfig().getBoolean("RestrictUsingBucket")) {
            event.setCancelled();
            sendMessage("RestrictUsingBucket");
        } else if (event.getBucket().getName().equals("Lava Bucket") && getConfig().getBoolean("RestrictUsingLavaBucket")) {
            event.setCancelled();
            sendMessage("RestrictUsingBucket");
        } else if (event.getBucket().getName().equals("Watar Bucket") && getConfig().getBoolean("RestrictUsingWatarBucket")) {
            event.setCancelled();
            sendMessage("RestrictUsingBucket");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLiquidFlow(LiquidFlowEvent event) {
        if (getConfig().getStringList("IgnoreWorlds").contains(event.getBlock().getLocation().getLevel().getName())) return;
        if (getConfig().getBoolean("RestrictFlow") || event.getSource() instanceof BlockLava || event.getSource() instanceof BlockWater) {
            event.setCancelled();
            sendMessage("RestrictFlow");
        }
    }

    private void sendMessage(String configKey) {
        if (getConfig().getBoolean(configKey+".message")) {
            Server.getInstance().broadcastMessage(TextFormat.RED+"FLR>>"+TextFormat.YELLOW+getConfig().getString(configKey+".content"));
        }
    }

    private void initConfig() {
        if (getConfig() == null) {
            saveResource("config.yml");
        } else if (getConfig().getInt("version") < CONFIG_VERSION){
            Map<String, Object> map = new LinkedHashMap<>();
            getConfig().getAll().keySet().forEach(key -> {
                map.put(key, getConfig().get(key));
            });
            saveResource("config.yml", true);
            map.keySet().forEach(key -> {
                getConfig().set(key, map.get(key));
            });
            getConfig().set("version", CONFIG_VERSION);
            saveConfig();
        }
    }
}
