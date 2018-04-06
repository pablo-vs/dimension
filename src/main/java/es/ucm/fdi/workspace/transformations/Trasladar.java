package es.ucm.fdi.workspace.transformations;

import es.ucm.fdi.workspace.Politopo;

/**
 *
 * @author Inmapg
 */
public class Trasladar extends TransformarFuncion{
    
    public Trasladar(int newID){
        super(newID);
    }
    
     public void trasladarX(Politopo p, double d){
        
    }
    
    public void trasladarY(Politopo p, double d){
        
    }
    
    public void trasladarZ(Politopo p, double d){
        
    }
    
    public void trasladarTodo(Politopo p, double d){
        trasladarX(p, d);
        trasladarY(p, d);
        trasladarZ(p, d);
    }
}
