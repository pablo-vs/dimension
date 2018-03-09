
package es.ucm.fdi;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;
/**
 * No definitivo, no comprobado
 * @author Inmapg
 */
public class ContraerTest {
    
    public ContraerTest(){
        
    }
    
    @Test
    public void ContraeTest(){
        double[] cmps = {2, 4, 6};
        ArrayList<Vertice> vectorVertices = new ArrayList<>();
   
        Politopo politopoTest = new Politopo(3, (new Vertice(3, cmps)));
        Contraer objContraer = new Contraer(1998); // 1998 is not relevant
        objContraer.contraeTodo(politopoTest, 2);
        double[] cmpsResultado= {0, 2, 4};
        Politopo resultado = new Politopo(3, (new Vertice(3, cmpsResultado)))
        assertEquals(resultado, objContraer);
    }
}
