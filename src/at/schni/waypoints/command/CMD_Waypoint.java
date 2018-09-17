package at.schni.waypoints.command;

import at.schni.waypoints.main.Plugin;
import at.schni.waypoints.util.Waypoint;
import at.schni.waypoints.util.WaypointsConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class CMD_Waypoint implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Plugin pl = Plugin.getInstance();
            String prefix = pl.getChatprefix();
            Economy economy = pl.getEconomy();
            WaypointsConfig c = pl.getWcConf();

            if (args.length == 1) {
                if (args[0].equals("list")) {
                    if (p.hasPermission("waypoints.list")) {
                        String out = "§a";
                        for (Waypoint w : new Waypoint().getWaypoints()) {
                            if (w.isPermitted(p.getUniqueId().toString())) {
                                out = out + "\n" + w.getName();
                            }
                        }

                        if (out.isEmpty()) {
                            p.sendMessage(prefix + " Keine Waypoints!");
                        } else {
                            p.sendMessage(prefix + " Verfügbare Waypoints: " + out);
                        }
                        return true;
                    } else {
                        p.sendMessage(prefix + " Du hast keine Berechtigung dies zu tun!");
                    }
                } else {
                    if (p.hasPermission("waypoints.use")) {
                        Waypoint w = new Waypoint(args[0]);

                        if (!w.exist()) {
                            p.sendMessage(prefix + " Dieser Waypoint existiert nicht!");
                            return true;
                        } else {
                            if (w.isPermitted(p.getUniqueId().toString())) {
                                Location wpl = w.getLocation();
                                Location pll = p.getLocation();

                                if (pll.getWorld() != wpl.getWorld()) {

                                    double b = economy.getBalance(p);
                                    if (b > c.getWtcost()) {
                                        economy.withdrawPlayer(p, c.getWtcost());
                                        p.teleport(w.getLocation());
                                        p.sendMessage(prefix + " Du wurdest zu " + w.getName() + " teleportiert!");
                                        p.sendMessage(prefix + " Dir wurden " + c.getWtcost() + " " + economy.currencyNamePlural() + " für das benutzen des Waypoints abgezogen.");
                                    } else {
                                        p.sendMessage(prefix + " Du brauchst " + c.getWtcost() + " " + economy.currencyNamePlural() + " um diesen Waypoint zu nutzen!");
                                    }

                                } else {
                                    p.teleport(w.getLocation());
                                    p.sendMessage(prefix + " Du wurdest zu " + w.getName() + " teleportiert!");
                                }
                                return true;
                            } else {
                                p.sendMessage(prefix + " Du hast den Waypoint noch nicht aktiviert!");
                                return true;
                            }
                        }
                    } else {
                        p.sendMessage(prefix + " Du hast keine Berechtigung dies zu tun!");
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equals("list") && args[1].equals("all")) {
                    if (p.hasPermission("waypoints.listall")) {
                        String out = "";
                        for (Waypoint w : new Waypoint().getWaypoints()) {
                            if (w.isPermitted(p.getUniqueId().toString())) {
                                out = out + "\n§a" + w.getName();
                            } else {
                                out = out + "\n§c" + w.getName();
                            }
                        }

                        if (out.isEmpty()) {
                            p.sendMessage(prefix + " Keine Waypoints!");
                        } else {
                            p.sendMessage(prefix + " Verfügbare Waypoints:\n" + out);
                        }
                        return true;
                    } else {
                        p.sendMessage(prefix + " Du hast keine Berechtigung dies zu tun!");
                    }
                } else if (args[1].equals("delete")) {
                    if (p.hasPermission("waypoints.use")) {
                        Waypoint w = new Waypoint(args[0]);
                        if (w.exist()) {
                            if (w.isOwner(p.getUniqueId().toString()) || p.hasPermission("waypoint.admin")) {
                                w.delete();
                                p.sendMessage(prefix + " Waypoint gelöscht!");
                            } else {
                                p.sendMessage(prefix + " Dieser Waypoint gehört nicht dir!");
                            }
                        }
                    } else {
                        p.sendMessage(prefix + " Du hast keine Berechtigung dies zu tun!");
                    }
                }
            } else if (args.length == 3) {
                if (args[1].equals("permit")) {
                    if (p.hasPermission("waypoints.admin")) {
                        Waypoint w = new Waypoint(args[0]);
                        if (w.exist()) {
                            Player toPermit = Bukkit.getPlayer(args[2]);
                            if (toPermit != null) {
                                w.permit(toPermit.getUniqueId().toString());
                                p.sendMessage(prefix + args[0] + "für" + toPermit.getDisplayName() + " aktiviert!");
                            } else {
                                p.sendMessage(prefix + " Der Spieler ist gerade nicht online!");
                            }
                        } else {
                            p.sendMessage(prefix + " Dieser Waypoint existiert nicht!");
                        }
                    } else {
                        p.sendMessage(prefix + " Du hast keine Berechtigung dies zu tun!");
                    }
                }
            }
        }
        return false;
    }
}
