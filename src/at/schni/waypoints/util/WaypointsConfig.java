package at.schni.waypoints.util;

public class WaypointsConfig {

    private FileBuilder_WP f;
    private String name = "config";

    private double cost, wtcost, costmp;

    public WaypointsConfig() {
        this.f = new FileBuilder_WP("plugins//Waypoints//", "config.yml");
    }

    public WaypointsConfig(double cost, double wtcost, double costmp) {
        this.cost = cost;
        this.wtcost = wtcost;
        this.costmp = costmp;
        this.f = new FileBuilder_WP("plugins//Waypoints//", "config.yml");
    }

    public FileBuilder_WP getF() {
        return this.f;
    }

    public boolean exist() {
        return f.getString(this.name) != null;
    }

    public WaypointsConfig setConfig() {
        f.setValue(name + ".cost", 250.00d);
        f.setValue(name + ".costmultiplier", 1.5d);
        f.setValue(name + ".worldtravelcost", 100.00d);
        f.save();
        return this;
    }

    public WaypointsConfig getConfig() {
        double cost = f.getDouble(name + ".cost");
        double wtcost = f.getDouble(name + ".worldtravelcost");
        double costmp = f.getDouble(name + ".costmultiplier");
        return new WaypointsConfig(cost, wtcost, costmp);
    }

    public double getCost() {
        return cost;
    }

    public double getWtcost() {
        return wtcost;
    }

    public double getCostmp() {
        return costmp;
    }
}
