package com.massivecraft.factions.tax.format;

import com.massivecraft.factions.P;
import com.massivecraft.factions.integration.Econ;

/**
 * This is not a massive craft format class
 * @author Techcable
 *
 */
public class FormatUtil {
	private FormatUtil() {}
	
	public static final String YES = parse("<g>YES");
	public static final String NO = parse("<b>NO");
	
	public static String parse(String str, Object... args) {
		return P.p.txt.parse(str, args);
	}
	
	public static String upperCaseFirst(String str) {
		return P.p.txt.upperCaseFirst(str);
	}
	
	public static String formatMoney(double money) {
		return Econ.moneyString(money);
	}
}
