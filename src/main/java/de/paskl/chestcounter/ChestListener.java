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
        Sign signB = null;
        int amount;
        int amountMax;
        boolean addedMaxAmount;
        //TODO add only check every ~20s
        //TODO also check the next -20 y-axis block beneath this chest
        for(Block b : getNearestChests(p.getLocation(), 5)) {
            amountMax = 0;
            amount = 0;

            //b.getState().getBlock().getState();
            TileState chest = (TileState)b.getState().getBlock().getState();
            Inventory inventory = ((Chest) chest).getBlockInventory();

            //Sign must be before chest at z+1
            Location signL = b.getLocation().add(0,0,1);

            if(signL != null && signL.getBlock().getType().equals(Material.DARK_OAK_WALL_SIGN)) {
                signB = (Sign) signL.getBlock().getState();
            }

            addedMaxAmount = false;
            for (ItemStack item : inventory.getContents().clone()) {
                if(item == null) continue; //Will also check for AIR
                amount += item.getAmount();
                if(inventory.getSize() > 0 && !addedMaxAmount) { //Only calc Y once
                    amountMax += inventory.getSize() * item.getMaxStackSize();
                }
                addedMaxAmount = true;
            }


            if(signB != null) {
                signB.setLine(1, String.valueOf(amount) + " / " + String.valueOf(amountMax));
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