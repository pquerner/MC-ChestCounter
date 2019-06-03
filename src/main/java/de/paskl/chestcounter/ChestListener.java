package de.paskl.chestcounter;

//import de.paskl.chestcounter.utils.CacheMap;

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
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class ChestListener implements Listener {

    //Look at normal and trapped chests (also works on Doublechests or either)
    List<Material> mats = new ArrayList<Material>(Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST));

    //private CacheMap cacheMap;

    private ChestCounter plugin;

    public ChestListener(ChestCounter p) {
        //cacheMap = new CacheMap(100);
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
        //^ maybe, but doesnt seem that expensive
        //TODO also check the next -20 y-axis block beneath this chest
        //^ seems like a bad idea
        for (Block b : getNearestObject(p.getLocation(), 10, mats)) {
            amountMax = 0;
            amount = 0;

            TileState chest = (TileState) b.getState().getBlock().getState();
            Inventory inventory = ((Chest) chest).getInventory();

            if (inventory instanceof DoubleChestInventory) {
                DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
                inventory = doubleChest.getInventory();
            }
            signB = getSign(signB, b);

            if (signB != null) { //Only work if sign is available
                //Check if sign has metadata attached. Should just fetch that data before and then update if needed, ie
                //add tstamp to metadata field and then only check once every 5 minutes?
                addedMaxAmount = false;
                for (ItemStack item : inventory.getContents().clone()) {
                    if (item == null) continue; //Will also check for AIR
                    amount += item.getAmount();
                    if (inventory.getSize() > 0 && !addedMaxAmount) { //Only calc Y once
                        amountMax += inventory.getSize() * item.getMaxStackSize();
                    }
                    addedMaxAmount = true;
                }

                signB.setLine(1, String.valueOf(amount) + " / " + String.valueOf(amountMax));
                signB.setMetadata("loc", new FixedMetadataValue(plugin, String.valueOf(signB.getLocation().getBlockX()) + ";"
                        + String.valueOf(signB.getLocation().getBlockY())
                        + ";" + String.valueOf(signB.getLocation().getBlockZ())));
                signB.update(true);

                Map<Integer, Sign> sign = new HashMap<>();
                sign.put(signB.getY(), signB);


                //cacheMap.put(signB.getX(), sign);
            }
        }

//        List<Material> mats = new ArrayList<Material>(Arrays.asList(Material.DARK_OAK_WALL_SIGN));
//        List<Material> mats2 = new ArrayList<Material>(Arrays.asList(Material.ACACIA_WALL_SIGN));
//        for (Block b : getNearestObject(p.getLocation(), 10, mats)) {
//            if (b.hasMetadata("loc")) {
//                for (Block b2 : getNearestObject(p.getLocation(), 10, mats2)) {
//                    MetadataValue meta = b.getMetadata("loc").get(0);
//                    if (null != meta) {
//                        String[] exploded = Objects.requireNonNull(meta.value()).toString().split(";");
//                        int x = Integer.valueOf(exploded[0]);
//                        int y = Integer.valueOf(exploded[1]);
//                        int z = Integer.valueOf(exploded[2]);
//                    }
//                }
//            }
//        }
    }

    private Sign getSign(Sign signB, Block b) {
        //Sign must be attached to chest
        Location signL = b.getLocation().add(0, 0, 1);

        if (signL != null && signL.getBlock().getType().equals(Material.DARK_OAK_WALL_SIGN)) {
            signB = (Sign) signL.getBlock().getState();
        }

        if (signB == null) {
            signL = b.getLocation().subtract(0, 0, 1);
            if (signL != null && signL.getBlock().getType().equals(Material.DARK_OAK_WALL_SIGN)) {
                signB = (Sign) signL.getBlock().getState();
            }
        }

        if (signB == null) {
            signL = b.getLocation().add(1, 0, 0);
            if (signL != null && signL.getBlock().getType().equals(Material.DARK_OAK_WALL_SIGN)) {
                signB = (Sign) signL.getBlock().getState();
            }
        }

        if (signB == null) {
            signL = b.getLocation().subtract(1, 0, 0);
            if (signL != null && signL.getBlock().getType().equals(Material.DARK_OAK_WALL_SIGN)) {
                signB = (Sign) signL.getBlock().getState();
            }
        }

        if (signB == null) {
            signL = b.getLocation().add(0, 1, 0);
            if (signL != null && signL.getBlock().getType().equals(Material.DARK_OAK_WALL_SIGN)) {
                signB = (Sign) signL.getBlock().getState();
            }
        }

        if (signB == null) {
            signL = b.getLocation().subtract(0, 1, 0);
            if (signL != null && signL.getBlock().getType().equals(Material.DARK_OAK_WALL_SIGN)) {
                signB = (Sign) signL.getBlock().getState();
            }
        }

        return signB;
    }


    public static List<Block> getNearestObject(Location location, int radius, List mats) {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    try {
                        Block b = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z);
                        if (mats.contains(b.getType())) {
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