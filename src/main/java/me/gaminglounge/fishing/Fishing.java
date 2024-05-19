package me.gaminglounge.fishing;

import org.bukkit.plugin.java.JavaPlugin;

public final class Fishing extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        amorstand Amorstand = new amorstand(this);
        getServer().getPluginManager().registerEvents(Amorstand, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
