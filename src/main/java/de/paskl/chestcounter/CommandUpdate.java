package de.paskl.chestcounter;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.Map;

public class CommandUpdate implements CommandExecutor {


    private ChestCounter plugin;
    private ChestListener chestListener;

    public CommandUpdate(ChestCounter p) {
        plugin = p;
        chestListener = new ChestListener(p);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            return updateSigns();
        }
        return true;
    }

    public boolean updateSigns() {

        this.plugin.reloadConfig();

        //Update all childs first
        try {
            Map<String, Object> map = this.plugin.getConfig().getConfigurationSection("wallsigns").getValues(true);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String[] exploded = entry.getValue().toString().split(";");
                for (World w : plugin.getServer().getWorlds()) {
                    try {
                        Block b = w.getBlockAt(Integer.valueOf(exploded[0]), Integer.valueOf(exploded[1]), Integer.valueOf(exploded[2]));
                        if (b.getType().toString().endsWith("_WALL_SIGN") &&
                                !(b.getType() == Material.BIRCH_WALL_SIGN)) {

                            Sign sign = (Sign) b.getState();
                            chestListener.updateChildrenSign(b, sign);
                        } else {
                            plugin.getLogger().warning("Could not update block at " + w.getName() + "(" + entry.getValue() + ")");
                        }
                    } catch (Exception e) {
                        plugin.getLogger().warning("Could not update block at " + w.getName() + "(" + entry.getValue() + ")");
                        e.printStackTrace();
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }


        //Then update all main signs
        try {
            Map<String, Object> map = this.plugin.getConfig().getConfigurationSection("mainsigns").getValues(true);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String[] exploded = entry.getValue().toString().split(";");
                for (World w : plugin.getServer().getWorlds()) {
                    try {
                        Block b = w.getBlockAt(Integer.valueOf(exploded[0]), Integer.valueOf(exploded[1]), Integer.valueOf(exploded[2]));
                        if (ChestListener.COUNT_CHILDREN_SIGN_MATERIAL == b.getType()) {
                            Sign sign = (Sign) b.getState();
                            chestListener.updateMainSign(b, sign);
                        } else {
                            plugin.getLogger().warning("Could not update block at " + w.getName() + "(" + entry.getValue() + ")");
                        }
                    } catch (Exception e) {
                        plugin.getLogger().warning("Could not update block at " + w.getName() + "(" + entry.getValue() + ")");
                        e.printStackTrace();
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
        this.plugin.saveConfig();
        return true;
    }
}
