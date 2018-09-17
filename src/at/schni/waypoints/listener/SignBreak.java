package at.schni.waypoints.listener;

import at.schni.waypoints.main.Plugin;
import at.schni.waypoints.util.Waypoint;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SignBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Plugin pl = Plugin.getInstance();
        String prefix = pl.getChatprefix();
        
        if (e.getBlock().getState() instanceof Sign) {
            Sign s = (Sign) e.getBlock().getState();
            if (s.getLine(0).equals("§3[§cWaypoint§3]")) {
                Player p = e.getPlayer();
                if (p.hasPermission("waypoints.use")) {
                    Waypoint w = new Waypoint(s.getLine(1));
                    if (w.exist()) {
                        if (w.isOwner(p.getUniqueId().toString()) || p.hasPermission("waypoint.admin")) {
                            w.delete();
                            p.sendMessage(prefix + " Du hast den Waypoint, " + w.getName() + " gelöscht!");
                        } else {
                            e.setCancelled(true);
                            p.sendMessage(prefix + " Dieser Waypoint gehört jemand anderem!");
                        }
                    }
                }else{
                    p.sendMessage(prefix + " Du hast keine Berechtigung dies zu tun!");
                }
            }
        }
    }
}
