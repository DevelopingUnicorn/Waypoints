package at.schni.waypoints.listener;

import at.schni.waypoints.util.Waypoint;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getState() instanceof Sign) {
                if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    Sign s = (Sign) e.getClickedBlock().getState();
                    Player p = e.getPlayer();

                    if (s.getLine(0).equals("§3[§cWaypoint§3]")) {
                        if (p.hasPermission("waypoints.use")) {
                            Waypoint w = new Waypoint(s.getLine(1));
                            if (w.exist()) {
                                if (!w.isPermitted(p.getUniqueId().toString())) {
                                    w.permit(p.getUniqueId().toString());
                                    p.sendMessage("[Waypoints] Du hast den Waypoint, " + w.getName() + " aktiviert!");
                                }
                            }
                        } else {
                            p.sendMessage("[Waypoints] Du hast keine Berechtigung dies zu tun!");
                        }
                    }
                }
            }
        }
    }
}
