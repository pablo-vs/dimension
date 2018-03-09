package es.ucm.fdi.trabajo;
/**
 *  @author Inmapg
 */
public class Contraer extends TransformarFuncion {
    
    public Contraer(int newID){
        super(newID);
    }
    
    public void contraerX(Politopo p, double d){
        
    }
    
    public void contraerY(Politopo p, double d){
        
    }
    
    public void contraerZ(Politopo p, double d){
        
    }
    
    public void contraerTodo(Politopo p, double d){
        contraerX(p, d);
        contraerY(p, d);
        contraerZ(p, d);
    }
}
