package es.ucm.fdi.util;

/**
 * A class to store useful methods for parsing functions.
 *
 * @author Pablo Villalobos
 * @version 01.04.2018
 */
public class ParserUtils {

	/**
	 * Eliminates unnecessary parenthesis at the ends of the string.
	 */
	public static String stripExtraParenthesis(String str1) {
		String str = str1.trim();
		int ini = 0, end = 0, min = str.length(), current;
		int i = 0;
		while(str.charAt(i) == '(') {
			ini++;
			i++;
		}
		i = 0;
		while(str.charAt(str.length()-i-1) == ')') {
			end++;
			i++;
		}
		if(ini > end) {
			min = end;
		} else {
			min = ini;
		}
		current = ini;
		i = ini;
		while(i < str.length()-end) {
			char c = str.charAt(i);
			if(c == '(') {
				current++;
			} else if (c == ')' && current > 0) {
				current--;
				if(current < min) {
					min = current;
				}
			}
			++i;
		}
		return str.substring(min, str.length()-min);
	}

	/**
	 * Given the position of an opening parenthesis, return the position
	 * next to the corresponding closing one.
	  */
	public static int getEndOfParenthesis(String str, int ini) {
		if(str.charAt(ini) == '(') {
			int current = 1, i = ini+1;
			while(i < str.length() && current > 0) {
				if(str.charAt(i) == '(') {
					current++;
				} else if (str.charAt(i) == ')') {
					current--;
				}
				++i;
			}
			if(current > 0) {
				throw new IllegalArgumentException("Expected )");
			} else {
				return i;
			}
		} else {
			throw new IllegalArgumentException("Character at position " + ini + " is not '('");
		}
	}
}
