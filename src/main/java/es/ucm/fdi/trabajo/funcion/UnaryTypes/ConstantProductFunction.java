package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;

/**
 * @author Javier Navalon
 * @author Inmapg
 */
public class ConstantProductFunction extends UnaryFunction{
      public ConstantProductFunction(String a, int param) {
          super(a, param, "Constant product");
      }

      @Override
      public String toString() {
          return Integer.toString(param) + function.toString();
      }
        
      @Override
      public double evaluate(VariablesList list) {
		      return list.getVariable(par);
      }
}


