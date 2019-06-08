package de.paskl.chestcounter;

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
            return updateSigns(((Player) sender).getPlayer());
        }
        return true;
    }

    public boolean updateSigns(Player player) {

        this.plugin.reloadConfig();

        //Update all childs first
        try {
            Map<String, Object> map = this.plugin.getConfig().getConfigurationSection("wallsigns").getValues(true);
            int x = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String[] exploded = entry.getValue().toString().split(";");
                Block b = player.getWorld().getBlockAt(Integer.valueOf(exploded[0]), Integer.valueOf(exploded[1]), Integer.valueOf(exploded[2]));
                try {
                    Sign sign = (Sign) b.getState();
                    chestListener.setPlayer(player);
                    chestListener.updateChildrenSign(b, sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException ignored) {
        }


        //Then update all main signs
        try {
            Map<String, Object> map = this.plugin.getConfig().getConfigurationSection("mainsigns").getValues(true);
            int x = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String[] exploded = entry.getValue().toString().split(";");
                Block b = player.getWorld().getBlockAt(Integer.valueOf(exploded[0]), Integer.valueOf(exploded[1]), Integer.valueOf(exploded[2]));
                try {
                    Sign sign = (Sign) b.getState();
                    chestListener.setPlayer(player);
                    chestListener.updateMainSign(b, sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException ignored) {
        }

        return true;
    }

    public boolean updateSigns() {

        this.plugin.reloadConfig();

        //Update all childs first
        try {
            Map<String, Object> map = this.plugin.getConfig().getConfigurationSection("wallsigns").getValues(true);
            int x = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String[] exploded = entry.getValue().toString().split(";");
                Block b = plugin.getServer().getWorld("World").getBlockAt(Integer.valueOf(exploded[0]), Integer.valueOf(exploded[1]), Integer.valueOf(exploded[2]));
                try {
                    Sign sign = (Sign) b.getState();
                    chestListener.updateChildrenSign(b, sign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException ignored) {
        }


        //Then update all main signs
        try {
            Map<String, Object> map = this.plugin.getConfig().getConfigurationSection("mainsigns").getValues(true);
            int x = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String[] exploded = entry.getValue().toString().split(";");
                Block b = plugin.getServer().getWorld("World").getBlockAt(Integer.valueOf(exploded[0]), Integer.valueOf(exploded[1]), Integer.valueOf(exploded[2]));
                try {
                    Sign sign = (Sign) b.getState();
                    chestListener.updateMainSign(b, sign);
                } catch (Exception ignored) {
                }
            }
        } catch (NullPointerException ignored) {
        }

        return true;
    }
}
