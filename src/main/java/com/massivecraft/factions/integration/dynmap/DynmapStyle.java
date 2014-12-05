package com.massivecraft.factions.integration.dynmap;

import com.massivecraft.factions.entity.MConf;

public class DynmapStyle
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public String lineColor = null;
	public int getLineColor() { return getColor(coalesce(this.lineColor, MConf.get().dynmapDefaultStyle.lineColor, MConf.DYNMAP_STYLE_LINE_COLOR)); }
	public DynmapStyle setStrokeColor(String strokeColor) { this.lineColor = strokeColor; return this; }
	
	public Double lineOpacity = null;
	public double getLineOpacity() { return coalesce(this.lineOpacity, MConf.get().dynmapDefaultStyle.lineOpacity, MConf.DYNMAP_STYLE_LINE_OPACITY); }
	public DynmapStyle setLineOpacity(Double strokeOpacity) { this.lineOpacity = strokeOpacity; return this; }
	
	public Integer lineWeight = null;
	public int getLineWeight() { return coalesce(this.lineWeight, MConf.get().dynmapDefaultStyle.lineWeight, MConf.DYNMAP_STYLE_LINE_WEIGHT); }
	public DynmapStyle setLineWeight(Integer strokeWeight) { this.lineWeight = strokeWeight; return this; }
	
	public String fillColor = null;
	public int getFillColor() { return getColor(coalesce(this.fillColor, MConf.get().dynmapDefaultStyle.fillColor, MConf.DYNMAP_STYLE_FILL_COLOR)); }
	public DynmapStyle setFillColor(String fillColor) { this.fillColor = fillColor; return this; }
	
	public Double fillOpacity = null;
	public double getFillOpacity() { return coalesce(this.fillOpacity, MConf.get().dynmapDefaultStyle.fillOpacity, MConf.DYNMAP_STYLE_FILL_OPACITY); }
	public DynmapStyle setFillOpacity(Double fillOpacity) { this.fillOpacity = fillOpacity; return this; }
	
	// NOTE: We just return the string here. We do not return the resolved Dynmap MarkerIcon object.
	// The reason is we use this class in the MConf. For serialization to work Dynmap would have to be loaded and we can't require that.
	// Using dynmap is optional.
	public String homeMarker = null;
	public String getHomeMarker() { return coalesce(this.homeMarker, MConf.get().dynmapDefaultStyle.homeMarker, MConf.DYNMAP_STYLE_HOME_MARKER); }
	public DynmapStyle setHomeMarker(String homeMarker) { this.homeMarker = homeMarker; return this; }
	
	public Boolean boost = null;
	public boolean getBoost() { return coalesce(this.boost, MConf.get().dynmapDefaultStyle.boost, MConf.DYNMAP_STYLE_BOOST); }
	public DynmapStyle setBoost(Boolean boost) { this.boost = boost; return this; }
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	@SafeVarargs
	public static <T> T coalesce(T... items)
	{
		for (T item : items)
		{
			if (item != null) return item;
		}
		return null;
	}
	
	public static int getColor(String string)
	{
		int ret = 0x00FF00;
		try
		{
			ret = Integer.parseInt(string.substring(1), 16);
		}
		catch (NumberFormatException nfx)
		{
			
		}
		return ret;
	}
	
}
