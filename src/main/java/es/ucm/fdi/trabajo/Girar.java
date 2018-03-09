package es.ucm.fdi.trabajo;

/**
 *
 * @author Inmapg
 */
public class Girar extends TransformarFuncion {
    
    public Girar(int newID) {
        super(newID);
    }
    
    public void girarX(Politopo p, double d){
        
    }
    
    public void girarY(Politopo p, double d){
        
    }
    
    public void girarZ(Politopo p, double d){
        
    }
    
    public void girarTodo(Politopo p, double d){
        girarX(p, d);
        girarY(p, d);
        girarZ(p, d);
    }
}
