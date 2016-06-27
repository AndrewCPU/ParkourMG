package net.Andrewcpu.Parkour;

import net.Andrewcpu.MinigameAPI.scoreboard.GameBoard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by stein on 6/24/2016.
 */
public class Arena {
    private Location corner1;
    private Location corner2;
    private int time = 0, maxTime = 500;
    private double waterHeight = 0;
    private double waterRate = 0;
    private ArenaState state = ArenaState.OFFLINE;
    private GameBoard gameBoard = new GameBoard(ChatColor.RED + "Flooded", true);

    public Arena(Location corner1, Location corner2, int maxTime) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.maxTime = maxTime;
        Location lower = corner1.getBlockY() > corner2.getBlockY() ? corner2 : corner1;
        Location higher = corner1.getBlockY() > corner2.getBlockY() ? corner1 : corner2;
        this.waterRate = (higher.getY() - lower.getY()) / maxTime;
//        this.waterRate*=10;
    }

    public double getWaterHeight() {
        return waterHeight;
    }

    public void setWaterHeight(double waterHeight) {
        this.waterHeight = waterHeight;
    }

    public double getWaterRate() {
        return waterRate;
    }

    public void setWaterRate(double waterRate) {
        this.waterRate = waterRate;
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState state) {
        this.state = state;
    }

    public void raiseWater(){
        raiseWater(1);
    }
    public void raiseWater(double i){
        waterHeight+=i;
        updateWater(false);
    }
    public void lowerWater(){
        lowerWater(1);
    }
    public void lowerWater(double i){
        waterHeight-=i;
        updateWater(true);
    }
    
    private void updateWater(boolean complete) {
//        Bukkit.broadcastMessage("Water level: " + getWaterHeight() + " / " + getWaterRate());
        Location lower = corner1.getBlockY() > corner2.getBlockY() ? corner2 : corner1;
        Location higher = corner1.getBlockY() > corner2.getBlockY() ? corner1 : corner2;
        int top = higher.getBlockY();
//        if(complete) {
//            for (Block b : blocksFromTwoPoints(lower, higher)) {
//                if (b.getLocation().getBlockY() > lower.getBlockY() + waterHeight) {
//                    if (b.getType().toString().contains("WATER")) {
//                        b.setType(Material.AIR);
//                    }
//                }
//            }
//        }
//        higher.setY(lower.getY() + waterHeight);
//        for(Block b : blocksFromTwoPoints(lower,higher)) {
//            if (b.getType() == Material.AIR && b.getLocation().getBlockY()<top) {
//                b.setType(Material.STATIONARY_WATER);
//            }
//        }

        Random random = new Random();
        higher.setY(lower.getY() + waterHeight);
        for(Block b : blocksFromTwoPoints(lower,higher)) {
            if (b.getType()!=Material.AIR && b.getLocation().getBlockY()<top) {
                if(random.nextInt(10) == 2) {
                    FallingBlock fallingBlock = b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
                    fallingBlock.setTicksLived(20);
                    fallingBlock.setVelocity(new Vector(0, 0, 0));
                }
                b.setType(Material.AIR);
            }
        }
    }

    public void tick(){
        if(getState()==ArenaState.PLAYING) {
            gameBoard.setEntries(new HashMap<>());
            Location lower = corner1.getBlockY() > corner2.getBlockY() ? corner2 : corner1;
            int floor = lower.getBlockY();
            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getLocation().getBlockY()>floor + waterHeight)
                    gameBoard.addEntry(player.getLocation().getBlockY() - floor, player.getDisplayName());
                else{
                    String name = (ChatColor.RED + player.getDisplayName());
                    gameBoard.addEntry(player.getLocation().getBlockY() - floor,name.length() > 16 ? name.substring(0,16) : name );
                }
            }
            setTime(getTime() + 1);
            if (getTime() > getMaxTime())
                endGame();
            else
                raiseWater(waterRate);
            for(Player player : Bukkit.getOnlinePlayers()){
                player.setScoreboard(gameBoard.toScoreboard());
            }
        }
    }

    public void endGame(){
        setState(ArenaState.OFFLINE);
        Bukkit.broadcastMessage("Game over!");
        setWaterHeight(0);
        setTime(0);
        updateWater(true);
        Bukkit.getOnlinePlayers().forEach((player)->player.teleport(player.getWorld().getSpawnLocation()));
    }
    public void startGame(){
        Bukkit.broadcastMessage("Starting game!");
        setState(ArenaState.PLAYING);
    }
    public Location getCorner1() {
        return corner1;
    }

    public void setCorner1(Location corner1) {
        this.corner1 = corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public void setCorner2(Location corner2) {
        this.corner2 = corner2;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2)
    {
        List<Block> blocks = new ArrayList<Block>();

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }
}
