package de.paskl.chestcounter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChestListener implements Listener {

    private ChestCounter plugin;
    public ChestListener(ChestCounter p) {
        plugin = p;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void walkEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location loc = p.getLocation();
        double i = loc.distance(p.getLocation());
        int x = 0;
        boolean hasItems;
        Sign signB = null;
        //TODO add only check every ~20s
        for(Block b : getNearestChests(p.getLocation(), 2)) {
            hasItems = false; //Reset
            //b.getState().getBlock().getState();
            TileState chest = (TileState)b.getState().getBlock().getState();
            Inventory inventory = ((Chest) chest).getBlockInventory();

            //Sign must be above chest in y+1
            Location signL = b.getLocation().add(0,1,0);

            if(signL != null && signL.getBlock().getType().toString().contains("WALL_SIGN")) {
                signB = (Sign) signL.getBlock().getState();
            }

            for (ItemStack item : inventory.getContents().clone()) {
                if(item == null) continue; //Will also check for AIR
                hasItems = true;
            }

            if(signB != null) {
                if(hasItems) {
                    //p.sendMessage("Nearest chest has items");
                    signB.setLine(0, "+");
                } else {
                    //p.sendMessage("Nearest chest has got no items");
                    signB.setLine(0, "-");
                }

                signB.update(true);
            }
        }
    }


    public static List<Block> getNearestChests(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    try {
                        Block b = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z);
                        if(b.getType() == Material.CHEST) {
                            blocks.add(b);
                        }
                    } catch (NullPointerException ignored) {

                    }
                }
            }
        }
        return blocks;
    }
}