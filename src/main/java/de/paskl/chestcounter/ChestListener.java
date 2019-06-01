package de.paskl.chestcounter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.DoubleChestInventory;
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
        for(Block b : getNearestObject(p.getLocation(), 10, Material.CHEST)) {
            amountMax = 0;
            amount = 0;

            TileState chest = (TileState)b.getState().getBlock().getState();
            Inventory inventory = ((Chest) chest).getInventory();

            if (inventory instanceof DoubleChestInventory) {
                DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
                inventory = doubleChest.getInventory();
            }

            //Sign must be before chest at z+1
            Location signL = b.getLocation().add(0,0,1);

            if(signL != null && signL.getBlock().getType().equals(Material.DARK_OAK_WALL_SIGN)) {
                signB = (Sign) signL.getBlock().getState();
            }

            if(signB != null) { //Only work if sign is available
                addedMaxAmount = false;
                for (ItemStack item : inventory.getContents().clone()) {
                    if(item == null) continue; //Will also check for AIR
                    amount += item.getAmount();
                    if(inventory.getSize() > 0 && !addedMaxAmount) { //Only calc Y once
                        amountMax += inventory.getSize() * item.getMaxStackSize();
                    }
                    addedMaxAmount = true;
                }

                signB.setLine(1, String.valueOf(amount) + " / " + String.valueOf(amountMax));
                signB.update(true);
            }
        }
    }


    public static List<Block> getNearestObject(Location location, int radius, Material mat) {
        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    try {
                        Block b = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z);
                        if(b.getType() == mat) {
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