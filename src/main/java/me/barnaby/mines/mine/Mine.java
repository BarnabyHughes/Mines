package me.barnaby.mines.mine;

import me.barnaby.mines.Mines;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Mine {

    private final Location corner1;
    private final Location corner2;
    private final Mines mines;
    private final List<MineBlock> blocks;
    private final Random random = new Random();

    public Mine(Location corner1, Location corner2, List<MineBlock> mineBlocks, Mines mines) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.blocks = mineBlocks;
        this.mines = mines;
    }

    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockState blockState = event.getBlock().getState();

        Optional<MineBlock> mineBlockOptional = blocks.stream().filter(
                mineBlock -> mineBlock.getMaterial() == block.getType()).findFirst();

        mineBlockOptional.ifPresent(mineBlock -> {
            float randomNumber = (float) random.nextInt(1000) /10;
            mineBlock.getItemDrops().forEach((itemDrop, aFloat) -> {
                if (randomNumber <= aFloat) {
                    itemDrop.give(event.getPlayer());
                }
            });
            new BukkitRunnable() {
                @Override
                public void run() {
                    blockState.update(true,false);
                }
            }.runTaskLater(mines, mineBlock.getRespawnSeconds() * 20L);
        });
    }

    public boolean isInRegion(Location location) {
        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        return location.getWorld() == corner1.getWorld() &&
                location.getX() >= minX && location.getX() <= maxX &&
                location.getY() >= minY && location.getY() <= maxY &&
                location.getZ() >= minZ && location.getZ() <= maxZ;
    }



}
