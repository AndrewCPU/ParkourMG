package net.Andrewcpu.Parkour;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by stein on 6/24/2016.
 */
public class Main extends JavaPlugin {
    private GameListener gameListener = new GameListener();
    private static Main instance = null;
    private Arena arena;
    public static Main getInstance(){
        return instance;
    }
    public void onEnable(){
        instance = this;
        getServer().getPluginManager().registerEvents(gameListener,this);
        arena = new Arena(LocationManager.loadLocation("Map.Corner1"),LocationManager.loadLocation("Map.Corner2"),500);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ()->arena.tick(), 20, 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,()->gameListener.tick(),10,10);
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("parkour")){
            if(args[0].equalsIgnoreCase("setlocation")){
                LocationManager.saveLocation(args[1], ((Player)sender).getLocation());
            }
            if(args[0].equalsIgnoreCase("start")){
                arena.startGame();
            }
            if(args[0].equalsIgnoreCase("end")){
                arena.endGame();
            }
        }
        return true;
    }
}
