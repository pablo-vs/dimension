package es.ucm.fdi.trabajo;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Eduardo Amaya
 * @author Javier Galiana
 * @author Brian Leiva
 *
 */
public class Visualization {
	private List<Grafico> graphics;
	
	public Visualization(ArrayList<Grafico> g){
		graphics = new ArrayList<Grafico>();
	}
	
	/**
	 * Muestra el gráfico por pantalla
	 * 
	 */
	public void dibujar(int n){
		
	}
	/**
	 * Añade un nuevo gráfico
	 * 
	 */
	public void addGraphic(Grafico g, int dim1, int dim2, int dim3, int hp1, int hp2, int hp3){
		graphics.add(proyectar(g, dim1, dim2, dim3, hp1, hp2, hp3));
	}
	
	private Grafico proyectar(Grafico g, int dim1, int dim2, int dim3, int hp1, int hp2, int hp3){
		Grafico graf = new Grafico(3);
		for (int i = 0; i < g.getDim(); ++i) {
			Vertice v = g.getImagen().get(i);
			if (v.at(dim1) == hp1 && v.at(dim2) == hp2 && v.at(dim3) == hp3) {
				graf.getImagen().add(v);
			}
		}
		return graf;
	}
}
