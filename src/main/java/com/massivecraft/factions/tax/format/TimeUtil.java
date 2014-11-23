package com.massivecraft.factions.tax.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Help With Time Mostly Stolen from MassiveCore
 * 
 * @author oloflarsson, Techacble
 *
 */
public class TimeUtil {
	public static final long millisPerSecond = 1000;
	public static final long millisPerMinute = 60 * millisPerSecond;
	public static final long millisPerHour = 60 * millisPerMinute;
	public static final long millisPerDay = 24 * millisPerHour;
	public static final long millisPerWeek = 7 * millisPerDay;
	public static final long millisPerMonth = 31 * millisPerDay;
	public static final long millisPerYear = 365 * millisPerDay;
	public static final Map<String, Long> unitMillis = /* I moved the method to this file */map("years", millisPerYear, "months", millisPerMonth, "weeks", millisPerWeek, "days", millisPerDay, "hours", millisPerHour, "minutes", millisPerMinute, "seconds", millisPerSecond);

	public static String getTimeDeltaDescriptionRelNow(long millis) {
		String ret = "";
		double millisLeft = (double) Math.abs(millis);
		List<String> unitCountParts = new ArrayList<String>();
		for (Entry<String, Long> entry : unitMillis.entrySet()) {
			if (unitCountParts.size() == 3) break;
			String unitName = entry.getKey();
			long unitSize = entry.getValue();
			long unitCount = (long) Math.floor(millisLeft / unitSize);
			if (unitCount < 1) continue;
			millisLeft -= unitSize * unitCount;
			unitCountParts.add(unitCount + " " + unitName);
		}
		if (unitCountParts.size() == 0) return "just now";
		ret += TextUtil.implodeCommaAnd(unitCountParts);
		ret += " ";
		if (millis <= 0) {
			ret += "ago";
		} else {
			ret += "from now";
		}
		return ret;
	}
	
	//Map stuff coppied from MUtil
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> map(K key1, V value1, Object... objects) {
		Map<K, V> ret = new LinkedHashMap<K, V>();
		ret.put(key1, value1);
		Iterator<Object> iter = Arrays.asList(objects).iterator();
		while (iter.hasNext()) {
			K key = (K) iter.next();
			V value = (V) iter.next();
			ret.put(key, value);
		}
		return ret;
	}
	

}
