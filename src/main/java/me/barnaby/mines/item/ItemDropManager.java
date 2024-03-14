package me.barnaby.mines.item;

import lombok.Getter;
import me.barnaby.mines.Mines;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDropManager {


    private final Mines mines;
    public ItemDropManager(Mines mines) {
        this.mines = mines;
    }

    private final List<ItemDrop> itemDrops = new ArrayList<>();

    public Optional<ItemDrop> getItemDrop(String name) {
        return itemDrops.stream().filter(itemDrop -> itemDrop.getId().equalsIgnoreCase(name)).findFirst();
    }

    public void initItems() {
        FileConfiguration config = mines.getConfigManager().getConfig();

        if (config.contains("items")) {
            ConfigurationSection itemsConfig = config.getConfigurationSection("items");

            for (String itemName : itemsConfig.getKeys(false)) {
                ConfigurationSection itemConfig = itemsConfig.getConfigurationSection(itemName);

                String id = itemName.toUpperCase(); // Assuming the item ID is the same as the key name
                String name = ChatColor.translateAlternateColorCodes('&', itemConfig.getString("name", ""));
                List<String> lore = itemConfig.getStringList("lore");
                List<String> translatedLore = new ArrayList<>();
                for (String loreLine : lore) {
                    translatedLore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
                }
                String message = ChatColor.translateAlternateColorCodes('&', itemConfig.getString("message", ""));
                Material material = Material.matchMaterial(itemConfig.getString("material", ""));

                if (material != null) {
                    ItemDrop itemDrop = new ItemDrop(id, name, translatedLore, message, material);
                    itemDrops.add(itemDrop);
                }
            }
        }
    }
}
