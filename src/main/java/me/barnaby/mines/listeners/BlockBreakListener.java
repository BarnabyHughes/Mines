package me.barnaby.mines.listeners;

import me.barnaby.mines.Mines;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final Mines mines;
    public BlockBreakListener(Mines mines) {
        this.mines = mines;
    }

   @EventHandler
   public void onBreak(BlockBreakEvent event) {
        mines.getMineManager().onBlockBreak(event);
   }



}
