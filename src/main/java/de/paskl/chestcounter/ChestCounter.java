package de.paskl.chestcounter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestCounter extends JavaPlugin {
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChestListener(this), this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                CommandUpdate cu = new CommandUpdate(getPlugin(ChestCounter.class));
                cu.updateSigns();
            }
        }, 0L, 20L * 300); //Every 5 min
        this.getCommand("signupdater").setExecutor(new CommandUpdate(this));
    }
}