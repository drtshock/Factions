	package com.massivecraft.factions.tax.format;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.avaje.ebean.TxType;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.util.TextUtil;

public class TimeDiffUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static Pattern patternFull = Pattern.compile("^(?:([^a-zA-Z]+)([a-zA-Z]*))+$");
	public final static Pattern patternPart = Pattern.compile("([^a-zA-Z]+)([a-zA-Z]*)");
	
	// -------------------------------------------- //
	// MILLIS
	// -------------------------------------------- //
	
	public static long millis(TimeUnit timeUnit, long count)
	{
		return timeUnit.millis*count;
	}
	
	public static long millis(TimeUnit timeUnit)
	{
		return millis(timeUnit, 1);
	}
	
	public static long millis(Map<TimeUnit, Long> unitcounts, long count)
	{
		long ret = 0;
		for (Entry<TimeUnit, Long> entry : unitcounts.entrySet())
		{
			ret += millis(entry.getKey(), entry.getValue()*count);
		}
		return ret;
	}
	
	public static long millis(Map<TimeUnit, Long> unitcounts)
	{
		return millis(unitcounts, 1);
	}
	
	public static long millis(String formated, long count) throws Exception
	{
		Map<TimeUnit, Long> unitcount = unitcounts(formated);
		return millis(unitcount, count);
	}
	
	public static long millis(String formated) throws Exception
	{
		return millis(formated, 1);
	}
	
	// -------------------------------------------- //
	// UNITCOUNT
	// -------------------------------------------- //
	
	public static LinkedHashMap<TimeUnit, Long> unitcounts(String formated) throws Exception
	{
		if (formated == null) throw new NullPointerException("The string can't be null.");
		
		Matcher matcherFull = patternFull.matcher(formated);
		if (!matcherFull.matches()) throw new NullPointerException("Invalid time diff format.");
		
		LinkedHashMap<TimeUnit, Long> ret = new LinkedHashMap<TimeUnit, Long>();
		if (formated.equals("0")) return ret;
		
		Matcher matcherPart = patternPart.matcher(formated);
		while (matcherPart.find())
		{
			// Parse the count
			String countString = matcherPart.group(1);
			String countStringFixed = countString.replaceAll("[\\+\\s]", "");
			long count = 0;
			try
			{
				count = Long.parseLong(countStringFixed);
			}
			catch (Exception e)
			{
				throw new Exception("\""+countString+"\" is not a valid integer.");
			}
			
			// Parse the time unit
			String unitString = matcherPart.group(2);
			TimeUnit unit = TimeUnit.get(unitString);
			if (unit == null)
			{
				throw new Exception("\""+unit+"\" is not a valid time unit.");
			}
			
			// Add to the return map
			if (ret.put(unit, count) != null)
			{
				throw new Exception("Multiple "+unit.singularName+" entries is not allowed.");
			}
		}
		
		return ret;
	}
	
	public static LinkedHashMap<TimeUnit, Long> unitcounts(long millis, TreeSet<TimeUnit> units)
	{
		// Create non-negative millis decumulator
		long millisLeft = Math.abs(millis);
		
		LinkedHashMap<TimeUnit, Long> ret = new LinkedHashMap<TimeUnit, Long>();
		
		for (TimeUnit unit : units)
		{
			long count = (long) Math.floor(millisLeft / unit.millis);
			if (count < 1) continue;
			millisLeft -= unit.millis*count;
			ret.put(unit, count);
		}
		
		return ret;
	}
	
	public static LinkedHashMap<TimeUnit, Long> unitcounts(long millis)
	{
		return unitcounts(millis, TimeUnit.getAll());
	}
	
	public static LinkedHashMap<TimeUnit, Long> limit(LinkedHashMap<TimeUnit, Long> unitcounts, int limit)
	{
		LinkedHashMap<TimeUnit, Long> ret = new LinkedHashMap<TimeUnit, Long>();
		
		Iterator<Entry<TimeUnit, Long>> iter = unitcounts.entrySet().iterator();
		int i = 0;
		while (iter.hasNext() && i < limit)
		{
			Entry<TimeUnit, Long> entry = iter.next();
			ret.put(entry.getKey(), entry.getValue());
			i++;
		}
		return ret;
	}
	
	// -------------------------------------------- //
	// FORMAT
	// -------------------------------------------- //
	
	
	public static final String FORMAT_ENTRY_VERBOOSE = P.p.txt.parse("<v>%1$d<k>%3$s");
	public static final String FORMAT_COMMA_VERBOOSE = "%s, ";
	public static final String FORMAT_AND_VERBOOSE = " %sand ";
	
	public static final String FORMAT_ENTRY_MINIMAL = P.p.txt.parse("<v>%1$d<k>%2$s");
	public static final String FORMAT_COMMA_MINIMAL = "%s";
	public static final String FORMAT_AND_MINIMAL = "%s";
	
	public static String formated(TimeUnit unit, long count, String formatEntry)
	{
		return String.format(formatEntry, count, unit.getUnitString(count), unit.getNameString(count));
	}
	
	public static String formated(Map<TimeUnit, Long> unitcounts, String entryFormat, String commaFormat, String andFormat, String color)
	{
		String comma = String.format(commaFormat, P.p.txt.parse(color));
		String and = String.format(andFormat, P.p.txt.parse(color));
		
		if (unitcounts.isEmpty())
		{
			return formated(TimeUnit.SECOND, 0, entryFormat);
		}
		
		List<String> parts = new ArrayList<String>();
		for (Entry<TimeUnit, Long> entry : unitcounts.entrySet())
		{
			parts.add(formated(entry.getKey(), entry.getValue(), entryFormat));
		}
		return TextUtil.implodeCommaAnd(parts, comma, and);
	}
	
	public static String formatedVerboose(TimeUnit unit, long count)
	{
		return formated(unit, count, FORMAT_ENTRY_VERBOOSE);
	}
	
	public static String formatedVerboose(Map<TimeUnit, Long> unitcounts, String color)
	{
		return formated(unitcounts, FORMAT_ENTRY_VERBOOSE, FORMAT_COMMA_VERBOOSE, FORMAT_AND_VERBOOSE, color);
	}
	
	public static String formatedVerboose(Map<TimeUnit, Long> unitcounts)
	{
		return formatedVerboose(unitcounts, "<i>");
	}
	
	public static String formatedMinimal(TimeUnit unit, long count)
	{
		return formated(unit, count, FORMAT_ENTRY_MINIMAL);
	}
	
	public static String formatedMinimal(Map<TimeUnit, Long> unitcounts, String color)
	{
		return formated(unitcounts, FORMAT_ENTRY_MINIMAL, FORMAT_COMMA_MINIMAL, FORMAT_AND_MINIMAL, color);
	}
	
	public static String formatedMinimal(Map<TimeUnit, Long> unitcounts)
	{
		return formatedMinimal(unitcounts, "");
	}
	
}
