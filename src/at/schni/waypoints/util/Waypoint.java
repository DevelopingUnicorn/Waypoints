package at.schni.waypoints.util;

import java.util.ArrayList;
import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Waypoint {
    
    private FileBuilder_WP f;
    private String name;
    
    public Waypoint() {
        this.f = new FileBuilder_WP("plugins//Waypoints//", "waypoints.yml");
    }
    
    public Waypoint(String name) {
        this.name = name;
        this.f = new FileBuilder_WP("plugins//Waypoints//", "waypoints.yml");
    }
    
    public LinkedList<Waypoint> getWaypoints() {
        LinkedList<Waypoint> waypoints = new LinkedList<>();
        for (String name : f.getKeys(false)) {
            waypoints.add(new Waypoint(name));
        }
        
        return waypoints;
    }
    
    public FileBuilder_WP getF() {
        return this.f;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean exist() {
        return f.getString(this.name) != null;
    }
    
    public void delete() {
        f.setValue(this.name, null);
        f.save();
    }
    
    public Waypoint setLocation(Player p) {
        Location loc = p.getLocation();
        
        f.setValue(name + ".owner", p.getUniqueId().toString());
        f.setValue(name + ".world", loc.getWorld().getName());
        f.setValue(name + ".x", loc.getX());
        f.setValue(name + ".y", loc.getY());
        f.setValue(name + ".z", loc.getZ());
        f.setValue(name + ".yaw", loc.getYaw());
        f.setValue(name + ".pitch", loc.getPitch());
        f.save();
        return this;
    }
    
    public Location getLocation() {
        World world = Bukkit.getWorld(f.getString(name + ".world"));
        double x = f.getDouble(name + ".x"),
                y = f.getDouble(name + ".y"),
                z = f.getDouble(name + ".z");
        float yaw = (float) f.getDouble(name + ".yaw"),
                pitch = (float) f.getDouble(name + ".pitch");
        
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    public LinkedList<String> getPermitted() {
        ArrayList<String> t = (ArrayList<String>) f.getObject(name + ".permitted");
        if (t != null) {
            LinkedList<String> permitted = new LinkedList<>();
            
            for (String s : t) {
                permitted.add(s);
            }
            
            return permitted;
        }
        return null;
    }
    
    public void permit(String uid) {
        LinkedList<String> permitted = (this.getPermitted() != null) ? this.getPermitted() : new LinkedList<>();
        permitted.add(uid);
        f.setValue(name + ".permitted", permitted);
        f.save();
    }
    
    public String getOwner() {
        return f.getString(name + ".owner");
    }
    
    public void setOwner(String uid) {
        f.setValue(name + ".owner", uid);
        f.save();
    }
    
    public boolean isPermitted(String uid) {
        return this.getPermitted().contains(uid);
    }
    
    public boolean isOwner(String uid) {
        return this.getOwner().equals(uid);
    }
    
    public int getWpOfUser(Player p) {
        LinkedList<Waypoint> waypoints = this.getWaypoints();
        int rt = 0;
        
        for (Waypoint waypoint : waypoints) {
            if(waypoint.isOwner(p.getUniqueId().toString())){
                rt++;
            }
        }
        
        return rt;
    }
    
}
