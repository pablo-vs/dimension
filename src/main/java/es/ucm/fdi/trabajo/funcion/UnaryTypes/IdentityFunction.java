package es.ucm.fdi.trabajo.funcion.UnaryTypes;

import es.ucm.fdi.trabajo.funcion.FunctionTypes.UnaryFunction;
import main.java.es.ucm.fdi.trabajo.funcion.FunctionTypes.VariablesList;

/**
 * @author Javier Navalon
 * 
 */
public class IdentityFunction extends UnaryFunction{
      public IdentityFunction(String a) {
                super(a, 1, "Identity");
                this.function = null;
      }

      @Override
      public String toString() {
                return "x";
      }

            @Override
      public double evaluate(VariablesList list) {
        return list.getVariable(par);
      }
}



