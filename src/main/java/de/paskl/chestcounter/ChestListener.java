package de.paskl.chestcounter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class ChestListener implements Listener {

    public static final String COUNTER_LINE = "[Counter]";
    //Look at normal and trapped chests (also works on Doublechests or either)
    List<Material> chestsMaterialList = new ArrayList<Material>(Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST));
    private ChestCounter plugin;

    public ChestListener(ChestCounter p) {
        plugin = p;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ev) {
        Block block = ev.getClickedBlock();
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (block.getType().toString().endsWith("WALL_SIGN")) {
                Sign sign = (Sign) ev.getClickedBlock().getState();
                if (block.getType() != Material.ACACIA_WALL_SIGN) {
                    if (sign.getLine(0).equals(COUNTER_LINE)) {
                        org.bukkit.block.data.type.WallSign data = (org.bukkit.block.data.type.WallSign) sign.getBlockData();
                        BlockFace face = null;
                        //Must invert, because I cannot click sign "from behind" - its always facing towards the player
                        face = getBlockFace(data, face);

                        if (face != null) {
                            Block blockRelative = block.getRelative(face);
                            if (chestsMaterialList.contains(blockRelative.getState().getBlock().getType())) {
                                Chest chest = (Chest) blockRelative.getState();
                                int amount;
                                int amountMax;
                                boolean addedMaxAmount;
                                amountMax = 0;
                                amount = 0;
                                Inventory inventory = chest.getInventory();

                                if (inventory instanceof DoubleChestInventory) {
                                    DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
                                    inventory = doubleChest.getInventory();
                                }

                                if (sign != null) { //Only work if sign is available
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

                                    sign.setLine(1, String.valueOf(amount) + " / " + String.valueOf(amountMax));
                                    sign.setMetadata("loc", new FixedMetadataValue(plugin, String.valueOf(sign.getLocation().getBlockX()) + ";"
                                            + String.valueOf(sign.getLocation().getBlockY())
                                            + ";" + String.valueOf(sign.getLocation().getBlockZ())));
                                    sign.update(true);
                                }
                            } else {
                                ev.getPlayer().sendMessage(ChatColor.RED + "Sign must be attached to a chest!");
                            }
                        }
                    }
                } else {
                    if (sign.getLine(0).equals(COUNTER_LINE)) {
                        org.bukkit.block.data.type.WallSign data = (org.bukkit.block.data.type.WallSign) sign.getBlockData();
                        BlockFace face = null;

                        List<Block> nextblocksY = getBlocks(ev.getClickedBlock(), 3);

                        //Must invert, because I cannot click sign "from behind" - its always facing towards the player
                        face = getBlockFace(data, face);

                        if (face != null && !nextblocksY.isEmpty()) {
                            int currentTotalAmount = 0;
                            int maxTotalAmount = 0;
                            for (Block b : nextblocksY) {
                                Sign childSign = (Sign) b.getState();

                                String[] exploded = childSign.getLine(1).split("/");
                                Integer currentAmount = Integer.parseInt(exploded[0].trim());
                                Integer chestMaximumAmount = Integer.parseInt(exploded[1].trim());

                                currentTotalAmount += currentAmount;
                                maxTotalAmount += chestMaximumAmount;


                            }

                            sign.setLine(1, String.valueOf(currentTotalAmount) + " / " + String.valueOf(maxTotalAmount));
                            sign.update(true);
                        }
                    }
                }
            }
        }
    }

    private List<Block> getBlocks(Block start, int howManyBlocksDown) {
        List<Block> blocks = new ArrayList<Block>();

        int i = start.getY() + howManyBlocksDown;
        while (i > 0) {
            Block b = start.getWorld().getBlockAt(start.getX(), i, start.getZ());
            if (b.getType() == Material.DARK_OAK_WALL_SIGN) {
                Sign s = (Sign) b.getState();
                if (s.getLine(0).equals(COUNTER_LINE)) {
                    blocks.add(b);
                }
            }
            i--;
        }

        return blocks;
    }

    private BlockFace getBlockFace(WallSign data, BlockFace face) {
        switch (data.getFacing()) {
            case NORTH:
                face = BlockFace.SOUTH;
                break;
            case EAST:
                face = BlockFace.WEST;
                break;
            case SOUTH:
                face = BlockFace.NORTH;
                break;
            case WEST:
                face = BlockFace.EAST;
                break;
        }
        return face;
    }
}