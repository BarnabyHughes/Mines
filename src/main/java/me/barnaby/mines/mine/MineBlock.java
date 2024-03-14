package me.barnaby.mines.mine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.barnaby.mines.item.ItemDrop;
import org.bukkit.Material;

import java.util.Map;

@Getter
@AllArgsConstructor
public class MineBlock {

    private Material material;
    private int respawnSeconds;
    private Map<ItemDrop, Float> itemDrops;

}
