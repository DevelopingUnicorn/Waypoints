package at.schni.waypoints.listener;

import at.schni.waypoints.main.Plugin;
import at.schni.waypoints.util.WaypointsConfig;
import at.schni.waypoints.util.Waypoint;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Plugin pl = Plugin.getInstance();
        Economy economy = pl.getEconomy();
        WaypointsConfig c = pl.getWcConf();

        String prefix = pl.getChatprefix();

        if (e.getLine(0).equals("[Waypoint]")) {
            Player p = e.getPlayer();
            double b = economy.getBalance(p);
            double cost = c.getCost();
            int mp = new Waypoint().getWpOfUser(p);

            for (int i = 0; i < mp; i++) {
                cost *= c.getCostmp();
            }

            if (cost > 2500.00) {
                cost = (double)2500;
            }

            if (p.hasPermission("waypoints.set")) {
                if (b >= cost) {
                    e.setLine(0, "§3[§cWaypoint§3]");

                    Waypoint wp = new Waypoint(e.getLine(1).trim());
                    if (!wp.exist()) {
                        wp.setLocation(p);
                        wp.setOwner(p.getUniqueId().toString());
                        wp.permit(p.getUniqueId().toString());
                        economy.withdrawPlayer(p, cost);
                        p.sendMessage(prefix + " Dir wurden " + cost + " " + economy.currencyNamePlural() + " für das erstellen eines Waypoints abgezogen.");
                    } else {
                        p.sendMessage(prefix + " Ein Waypoint mit diesem Namen existiert bereits!");
                    }

                } else {
                    p.sendMessage(prefix + " Du brauchst " + cost + " " + economy.currencyNamePlural() + " um einen Waypoint zu erstellen!");
                }
            } else {
                p.sendMessage(prefix + " Du hast keine Berechtigung dies zu tun!");
            }
        }
    }

}
