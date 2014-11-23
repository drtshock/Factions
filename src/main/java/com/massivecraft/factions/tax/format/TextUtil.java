package com.massivecraft.factions.tax.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TextUtil {
	//Text Utils

	public static String implode(final Object[] list, final String glue, final String format) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			Object item = list[i];
			String str = (item == null ? "NULL" : item.toString());
			if (i != 0) {
				ret.append(glue);
			}
			if (format != null) {
				ret.append(String.format(format, str));
			} else {
				ret.append(str);
			}
		}
		return ret.toString();
	}

	public static String implode(final Object[] list, final String glue) {
		return implode(list, glue, null);
	}

	public static String implode(final Collection<? extends Object> coll, final String glue, final String format) {
		return implode(coll.toArray(new Object[0]), glue, format);
	}

	public static String implode(final Collection<? extends Object> coll, final String glue) {
		return implode(coll, glue, null);
	}

	public static String implodeCommaAndDot(final Collection<? extends Object> objects, final String format, final String comma, final String and, final String dot) {
		if (objects.size() == 0) return "";
		if (objects.size() == 1) {
			return implode(objects, comma, format);
		}
		List<Object> ourObjects = new ArrayList<Object>(objects);
		String lastItem = ourObjects.get(ourObjects.size() - 1).toString();
		String nextToLastItem = ourObjects.get(ourObjects.size() - 2).toString();
		if (format != null) {
			lastItem = String.format(format, lastItem);
			nextToLastItem = String.format(format, nextToLastItem);
		}
		String merge = nextToLastItem + and + lastItem;
		ourObjects.set(ourObjects.size() - 2, merge);
		ourObjects.remove(ourObjects.size() - 1);
		return implode(ourObjects, comma, format) + dot;
	}

	public static String implodeCommaAndDot(final Collection<? extends Object> objects, final String comma, final String and, final String dot) {
		return implodeCommaAndDot(objects, null, comma, and, dot);
	}

	public static String implodeCommaAnd(final Collection<? extends Object> objects, final String comma, final String and) {
		return implodeCommaAndDot(objects, comma, and, "");
	}

	public static String implodeCommaAndDot(final Collection<? extends Object> objects, final String color) {
		return implodeCommaAndDot(objects, color + ", ", color + " and ", color + ".");
	}

	public static String implodeCommaAnd(final Collection<? extends Object> objects, final String color) {
		return implodeCommaAndDot(objects, color + ", ", color + " and ", "");
	}

	public static String implodeCommaAndDot(final Collection<? extends Object> objects) {
		return implodeCommaAndDot(objects, "");
	}

	public static String implodeCommaAnd(final Collection<? extends Object> objects) {
		return implodeCommaAnd(objects, "");
	}
}
