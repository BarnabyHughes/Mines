package me.barnaby.mines.mine;

import lombok.Getter;
import me.barnaby.mines.Mines;
import me.barnaby.mines.item.ItemDrop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.*;

public class MineManager {

    private final Mines plugin;

    @Getter
    private final List<Mine> mines = new ArrayList<>();

    public MineManager(Mines mines) {
        this.plugin = mines;
    }

    public void initMines() {
        FileConfiguration config = plugin.getConfigManager().getConfig();

        if (config.contains("mines")) {
            ConfigurationSection minesConfig = config.getConfigurationSection("mines");

            for (String mineName : minesConfig.getKeys(false)) {
                ConfigurationSection mineConfig = minesConfig.getConfigurationSection(mineName);

                ConfigurationSection corner1Config = mineConfig.getConfigurationSection("corner-1");
                ConfigurationSection corner2Config = mineConfig.getConfigurationSection("corner-2");

                if (corner1Config == null || corner2Config == null) {
                    Bukkit.getLogger().warning("Invalid mine configuration for mine: " + mineName);
                    continue;
                }

                Location corner1 = getLocationFromConfig(corner1Config);
                Location corner2 = getLocationFromConfig(corner2Config);

                List<MineBlock> mineBlocks = getMineBlocks(mineConfig.getConfigurationSection("blocks"));

                if (corner1 != null && corner2 != null) {
                    Mine mine = new Mine(corner1, corner2, mineBlocks, plugin);
                    mines.add(mine);
                }
            }
        }
    }


    public void onBlockBreak(BlockBreakEvent event) {
        Optional<Mine> mineOptional = mines.stream().filter(mine ->
                mine.isInRegion(event.getBlock().getLocation())).findFirst();

        mineOptional.ifPresent(mine -> {
            mine.onBlockBreak(event);
        });
    }





    public List<MineBlock> getMineBlocks(ConfigurationSection section) {
        List<MineBlock> mineBlocks = new ArrayList<>();

        if (section == null) {
            return mineBlocks;
        }

        for (String blockName : section.getKeys(false)) {
            ConfigurationSection blockConfig = section.getConfigurationSection(blockName);
            if (blockConfig != null) {
                Material material = Material.matchMaterial(blockName.toUpperCase());
                if (material != null) {
                    int respawnTime = blockConfig.getInt("respawn-time", 0); // Default respawn time is 0 if not specified
                    Map<ItemDrop, Float> itemDrops = getItemDrops(blockConfig.getConfigurationSection("drops"));
                    MineBlock mineBlock = new MineBlock(material, respawnTime, itemDrops);
                    mineBlocks.add(mineBlock);
                }
            }
        }
        return mineBlocks;
    }

    private Map<ItemDrop, Float> getItemDrops(ConfigurationSection section) {
        Map<ItemDrop, Float> itemDrops = new HashMap<>();

        if (section == null) {
            return itemDrops;
        }

        for (String dropName : section.getKeys(false)) {
            Optional<ItemDrop> drop = plugin.getItemDropManager().getItemDrop(dropName);
            drop.ifPresent(d -> {
                itemDrops.put(d, (float) section.getDouble(dropName));
            });
        }

        return itemDrops;
    }

    private Location getLocationFromConfig(ConfigurationSection config) {
        String worldName = config.getString("world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            Bukkit.getLogger().warning("World " + worldName + " not found.");
            return null;
        }

        double x = config.getDouble("x");
        double y = config.getDouble("y");
        double z = config.getDouble("z");

        return new Location(world, x, y, z);
    }
}
