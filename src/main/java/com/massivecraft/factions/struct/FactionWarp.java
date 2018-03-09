package com.massivecraft.factions.struct;

import com.massivecraft.factions.P;
import com.massivecraft.factions.util.LazyLocation;
import org.bukkit.Material;

import java.util.logging.Level;

public class FactionWarp {

    private String name;
    private String password = "";
    private Material material;
    private LazyLocation location;

    public FactionWarp(String name, LazyLocation location) {
        this.name = name;
        this.location = location;

        // Get default material from config
        Material material = Material.matchMaterial(P.p.getConfig().getString("fwarp-gui.material"));
        if (material == null) {
            P.p.log(Level.WARNING, "Invalid default Warp GUI material");
            material = Material.STONE;
        }
        this.material = material;
    }

    // Standard Warp stuff
    public String getName() {
        return name;
    }

    public LazyLocation getLocation() {
        return location;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
