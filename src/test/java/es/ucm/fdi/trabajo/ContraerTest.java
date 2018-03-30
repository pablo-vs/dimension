
package es.ucm.fdi.trabajo;
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
	vectorVertices.add(new Vertice(3, cmps));
        Politopo politopoTest = new Politopo(3, vectorVertices);
        Contraer objContraer = new Contraer(1998); // 1998 is not relevant
        objContraer.contraerTodo(politopoTest, 2);
        double[] cmpsResultado= {0, 2, 4};
	vectorVertices.clear();
	vectorVertices.add(new Vertice(3, cmpsResultado));
        Politopo resultado = new Politopo(3, vectorVertices);
        //assertEquals(resultado, politopoTest);
    }
}
