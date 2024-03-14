package me.barnaby.mines;

import lombok.Getter;
import me.barnaby.mines.config.ConfigManager;
import me.barnaby.mines.item.ItemDropManager;
import me.barnaby.mines.listeners.BlockBreakListener;
import me.barnaby.mines.mine.MineManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Mines extends JavaPlugin {

    @Getter
    private final ConfigManager configManager = new ConfigManager(this);

    @Getter
    private final MineManager mineManager = new MineManager(this);

    @Getter
    private final ItemDropManager itemDropManager = new ItemDropManager(this);
    @Override
    public void onEnable() {
        registerListeners();
        configManager.init();
        itemDropManager.initItems();
        mineManager.initMines();
    }



    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BlockBreakListener(this), this);
        //pm.registerEvents();
    }

    private void registerCommands() {
        //this.getCommand("").setExecutor();


    }
}