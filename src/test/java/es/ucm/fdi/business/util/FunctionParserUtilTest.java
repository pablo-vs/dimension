package es.ucm.fdi.business.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.ucm.fdi.business.workspace.function.AbstractFunction;
import es.ucm.fdi.business.workspace.function.types.VariablesList;

/**
 * @author Eloy Mósig
 * 
 */
public class FunctionParserUtilTest {
	
	 @Test
	    public void basicParserTest() {
	        String[] fs = {"5", "(3*x) + 5", "3*x + 5", "x", "3*(x^2) + x/5",
	            "3*x + 5 + x", "3*x^2+4", "log_(3)(81)", "cos(2*PI)", "sin(3*PI/2)",
	            "ln(e^10)", "ln(e^((x*1)+ln(e^5)))", "ln(0+e)+1"};
	        double[] res = {5, 20, 20, 5, 76, 25, 79, 4, 1, 0.08215400811873169, 10, 10, 2};
	        String[] fs_vv = {"x+y+z", "x^(3*y-3*z+4)", "cos(PI*(tan(x+y+z*y-x*z+y)+y^(1)))"};
	        double[] res_vv = {10, 5, 1.0};
	        String[] varNames = {"x"};
	        VariablesList vars = new VariablesList(varNames);
	        String[] varNames2 = {"x", "y", "z"};
	        VariablesList vars2 = new VariablesList(varNames2);
	        int value = 5, value2 = 2, value3 = 3;
	        System.out.println("First test, evaluating functions at x = " + value);
	        for (int i = 0; i < fs.length; ++i) {
	            String aux = fs[i];
	            AbstractFunction f = FunctionParserUtils.parse(aux, vars);
	            vars.setVariable("x", value);
	            System.out.println("Input: " + fs[i] + " | Parsed function: " + f
	                    + " | Result = " + f.evaluate(vars));
	            assertEquals(res[i], f.evaluate(vars), 0.01);
	        }
	        System.out.println("Second test, evaluating functions at x = " + value
	                + " , y = " + value2 + ", z = " + value3);
	        for (int i = 0; i < fs_vv.length; ++i) {
	            String aux = fs_vv[i];
	            AbstractFunction f = FunctionParserUtils.parse(aux, vars2);
	            vars2.setVariable("x", value);
	            vars2.setVariable("y", value2);
	            vars2.setVariable("z", value3);
	            System.out.println("Input: " + fs_vv[i] + " | Parsed function: "
	                    + f + " | Result = " + f.evaluate(vars2));
	            assertEquals(res_vv[i], f.evaluate(vars2), 0.01);
	        }
	    }
	 
	 @Test
	 public void otherFuctionsParserTest(){
		 String[] fs = {"3", "5*x", "cosec(x)", "cotan((PI*x)/2)", "secan(x - 1)",
				 "tan(x - 1)", "2^(2*x)"};
		 double[] res = { 3, 5, 1.1883, 0, 1, 0, 4};
		 String[] varNames = {"x"};
	     VariablesList vars = new VariablesList(varNames);
	     int value = 1;
	     StringBuilder sb = new StringBuilder("Evaluating some other functions at x = ");
	     sb.append(String.valueOf(value));
	     System.out.println(sb.toString());
	     for(int i = 0; i < fs.length; ++i){
	    	 String aux = fs[i];
             AbstractFunction f = FunctionParserUtils.parse(aux, vars);
             vars.setVariable("x", value);
             StringBuilder check = new StringBuilder("Input: ");
             check.append(fs[i]).append(" | Parsed function: ").append(f).append(" | Result = ")
             	     .append(f.evaluate(vars));
             System.out.println(check.toString());
             assertEquals(res[i], f.evaluate(vars), 0.01);
	     }
	 }
}
