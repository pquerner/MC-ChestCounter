package de.paskl.chestcounter;

import org.bukkit.plugin.java.JavaPlugin;

public class ChestCounter extends JavaPlugin {
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(new ChestListener(this), this);
    }
}