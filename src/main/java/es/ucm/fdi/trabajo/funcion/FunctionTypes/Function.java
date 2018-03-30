package main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes;

import main.java.es.ucm.fdi.trabajo.funcion.BinaryTypes.*;
import main.java.es.ucm.fdi.trabajo.funcion.UnaryTypes.*;

public class Function {
	protected String type;
        
        public Function(String type){
            this.type = type;
        }
	public Function parser(String string) {
		String[] chars= {string};
		if (string.length()>1) {
			chars=string.split("");
		}
		Function dev = null;
		if (chars[0].equals("x")) {
			String var = string.substring(0,3);
			if (chars.length>3) {
				if (chars[3].equals("+")) {
					dev = new SumFunction(var, string.substring(4));
				} else if (chars[3].equals("*")) {
					dev = new ProductFunction(var, string.substring(4));
				} else if (chars[3].equals("^")) {
					dev = new ExponentialFunction(var, string.substring(4));
				} else if (chars[3].equals("/")) {
					dev = new DivideFunction(var, string.substring(4));
				} else if (chars[3].equals("-")) {
					dev = new SubstractFunction(var, string.substring(4));
				}
			} else {
				dev= new IdentityFunction(chars[2],1);
				return dev;
			}	
		}	
		else if (chars[0].equals("(")){
			int i=1, nP=1;
			while (nP!=0) {
				if (chars[i].equals("(")) nP++;
				if (chars[i].equals(")")) nP--;
				i++;
			}
			if (i>0) i--;
			String aux=null, aux2=null;
			if ((chars.length-1)!=i) {
				aux=string.substring(0,i+1);
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
	public double evaluate(VariablesList variables) {
		return 0;
	}
}
