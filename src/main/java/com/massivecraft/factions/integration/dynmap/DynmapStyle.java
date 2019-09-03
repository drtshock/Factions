package com.massivecraft.factions.integration.dynmap;

import com.massivecraft.factions.FactionsPlugin;

public class DynmapStyle {
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public String lineColor = null;

    public int getLineColor() {
        return getColor(coalesce(this.lineColor,
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getLineColor(),
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getDefaultStyle().lineColor));
    }

    public DynmapStyle setStrokeColor(String strokeColor) {
        this.lineColor = strokeColor;
        return this;
    }

    public Double lineOpacity = null;

    public double getLineOpacity() {
        return coalesce(this.lineOpacity,
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getLineOpacity(),
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getDefaultStyle().lineOpacity);
    }

    public DynmapStyle setLineOpacity(Double strokeOpacity) {
        this.lineOpacity = strokeOpacity;
        return this;
    }

    public Integer lineWeight = null;

    public int getLineWeight() {
        return coalesce(this.lineWeight,
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getLineWeight(),
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getDefaultStyle().lineWeight);
    }

    public DynmapStyle setLineWeight(Integer strokeWeight) {
        this.lineWeight = strokeWeight;
        return this;
    }

    public String fillColor = null;

    public int getFillColor() {
        return getColor(coalesce(this.fillColor,
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getFillColor(),
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getDefaultStyle().fillColor));
    }

    public DynmapStyle setFillColor(String fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public Double fillOpacity = null;

    public double getFillOpacity() {
        return coalesce(this.fillOpacity,
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getFillOpacity(),
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getDefaultStyle().fillOpacity);
    }

    public DynmapStyle setFillOpacity(Double fillOpacity) {
        this.fillOpacity = fillOpacity;
        return this;
    }

    // NOTE: We just return the string here. We do not return the resolved Dynmap MarkerIcon object.
    // The reason is we use this class in the MConf. For serialization to work Dynmap would have to be loaded and we can't require that.
    // Using dynmap is optional.
    public String homeMarker = null;

    public String getHomeMarker() {
        return coalesce(this.homeMarker,
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getHomeMarker(),
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getDefaultStyle().homeMarker);
    }

    public DynmapStyle setHomeMarker(String homeMarker) {
        this.homeMarker = homeMarker;
        return this;
    }

    public Boolean boost = null;

    public boolean getBoost() {
        return coalesce(this.boost,
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().isStyleBoost(),
                FactionsPlugin.getInstance().getConfigManager().getDynmapConfig().style().getDefaultStyle().boost);
    }

    public DynmapStyle setBoost(Boolean boost) {
        this.boost = boost;
        return this;
    }

    // -------------------------------------------- //
    // UTIL
    // -------------------------------------------- //

    @SafeVarargs
    public static <T> T coalesce(T... items) {
        for (T item : items) {
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    public static int getColor(String string) {
        int ret = 0x00FF00;
        try {
            ret = Integer.parseInt(string.substring(1), 16);
        } catch (NumberFormatException ignored) {
        }
        return ret;
    }
}
