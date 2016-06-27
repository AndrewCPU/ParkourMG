package net.Andrewcpu.Parkour;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Created by stein on 6/24/2016.
 */
public class GameListener implements Listener {
    public void tick(){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(Main.getInstance().getArena().getState()== ArenaState.PLAYING){
                if(player.getLocation().getBlock().getType().toString().contains("WATER")){
                    player.damage(1);
                }
            }
        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            if(((Player) event.getEntity()).getHealth() - event.getDamage() <= 0){
                ((Player) event.getEntity()).setHealth(20);
                ((Player) event.getEntity()).setGameMode(GameMode.SPECTATOR);
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        ((Player) event.getEntity()).setGameMode(GameMode.SPECTATOR);
        event.getEntity().setHealth(20);
    }
}
