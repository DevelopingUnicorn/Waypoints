package at.schni.waypoints.main;

import net.milkbowl.vault.economy.Economy;

import at.schni.waypoints.command.CMD_Waypoint;
import at.schni.waypoints.listener.SignBreak;
import at.schni.waypoints.listener.PlayerInteract;
import at.schni.waypoints.listener.SignChange;
import at.schni.waypoints.util.WaypointsConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    private static PluginManager pm;
    private Economy econ;
    private WaypointsConfig wc;
    private CMD_Waypoint cmd = new CMD_Waypoint();
    

    private String chatprefix = "§3[§cWaypoints§3]";
    
    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;

        setupWaypointsConfig();

        if (!setupEconomy()) {
            this.getLogger().severe("[Waypoints & Minecash] Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        pm = Bukkit.getPluginManager();

        getCommand("waypoint").setExecutor(cmd);
        pm.registerEvents(new SignChange(), this);
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new SignBreak(), this);
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            } else {
                econ = rsp.getProvider();
                return econ != null;
            }
        }
    }

    private void setupWaypointsConfig() {
        WaypointsConfig temp = new WaypointsConfig();

        if (!temp.exist()) {
            temp.setConfig();
        }

        wc = temp.getConfig();
    }

    public Economy getEconomy() {
        return econ;
    }

    public static Plugin getInstance() {
        return instance;
    }

    public WaypointsConfig getWcConf() {
        return wc;
    }

    public String getChatprefix() {
        return chatprefix;
    }

    public CMD_Waypoint getCmd() {
        return cmd;
    }
}
