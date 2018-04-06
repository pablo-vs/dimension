package es.ucm.fdi.trabajo;

/**
 *
 * @author Inmapg
 */
public class Girar extends TransformarFuncion {
    
    public Girar(int newID) {
        super(newID);
    }
    
    public void girarX(Visualization p, double d){
    	
        p.dibujar();
    }
    
    public void girarY(Visualization p, double d){
        
    }
    
    public void girarZ(Visualization p, double d){
        
    }
    
    public void girarTodo(Visualization p, double d){
        girarX(p, d);
        girarY(p, d);
        girarZ(p, d);
    }
}
