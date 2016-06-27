package net.Andrewcpu.Parkour;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by stein on 6/18/2016.
 */
public class LocationManager {
    public static void teleportPlayerToLocation(Player player, String location){
        player.teleport(loadLocation(location));
    }
    public static Location loadLocation(String configLocation){
        FileConfiguration config = Main.getInstance().getConfig();
        int x = config.getInt("Server.Location." + configLocation + ".X");
        int y = config.getInt("Server.Location." + configLocation + ".Y");
        int z = config.getInt("Server.Location." + configLocation + ".Z");
        World world = Bukkit.getWorld(config.getString("Server.Location." + configLocation + ".World"));
        return new Location(world,x,y,z);
    }
    public static void saveLocation(String configLocation, Location location){
        FileConfiguration config = Main.getInstance().getConfig();
        String fC = "Server.Location." + configLocation + ".";
        config.set(fC + "X", location.getBlockX());
        config.set(fC + "Y", location.getBlockY());
        config.set(fC + "Z", location.getBlockZ());
        config.set(fC + "World", location.getWorld().getName());
        Main.getInstance().saveConfig();
        Main.getInstance().reloadConfig();
    }
}
