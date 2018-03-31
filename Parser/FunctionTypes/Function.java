package FunctionTypes;

import BinaryTypes.DivideFunction;
import BinaryTypes.ExponentialFunction;
import BinaryTypes.ProductFunction;
import BinaryTypes.SumFunction;
import UnaryTypes.ConstantFunction;
import UnaryTypes.IdentityFunction;

public class Function {
	protected String type;
	public Function parser(String string) {
		String[] chars= {string};
		if (string.length()>1) {
			chars=string.split("");
		} 
		Function dev = null;
		if (chars[0].equals("x")) {
			if (chars.length>1) {
				if (chars[1].equals("+")) {
					dev = new SumFunction("x", string.substring(2));
				} else if (chars[1].equals("*")) {
					dev = new ProductFunction("x", string.substring(2));
				} else if (chars[1].equals("^")) {
					dev = new ExponentialFunction("x", string.substring(2));
				} else if (chars[1].equals("/")) {
					dev = new DivideFunction("x", string.substring(2));
				}
			} else {
				dev= new IdentityFunction("x",1);
				return dev;
			}	
		}	
		else if (chars[0].equals("(")){
			int i=0;
			while (!chars[i].equals(")")) i++;
			String aux=null, aux2=null;
			aux=string.substring(0,i+1);
			if (chars.length>i+1) {
				aux2=string.substring(i+2, string.length());
				if (chars[i+1].equals("*")) {
					dev = new ProductFunction(aux, aux2);
				} else if (chars[i+1].equals("+")) {
					dev = new SumFunction(aux, aux2);
				} else if(chars[i+1].equals("^")) {
					dev = new ExponentialFunction(aux, aux2);
				}
			} else {
				dev = parser(string.substring(1, string.length()-1));
			}	
		} else {
			dev = new ConstantFunction(null, Integer.parseInt(string));
		}
		return dev;
	}
	public static boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	public double evaluate(int x) {
		return 0;
	}
}
