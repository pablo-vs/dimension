package es.ucm.fdi.trabajo;

import java.util.ArrayList;
/**
 * @author Inmapg
 */

public class Politopo {
	private int dimension;
	private ArrayList<Vertice> vertices; 
	
        public Politopo(){
            // Not implemented yet
        }
 /* Deberíamos cambiar aquí ArrayList por List para hacerlo más genérico porque a las demás clases les da igual el tipo de
lista que usemos aquí. Sólo deberíamos indicar que se usa una lista.
----Lo cambiaremos----
*/
	public Politopo(int dim, ArrayList<Vertice> v){ 
            dimension = dim;
            vertices = v;
        }
	/**
	 * Cambia la lista de vertices
	 *
	 * @param v Lista de vertices
	 * @param dim Dimension
	 */
	public void modificar(ArrayList<Vertice> v, int dim){
		
	}
}
