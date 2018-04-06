package es.ucm.fdi.workspace;

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
	
	public Visualization(){
		graphics = new ArrayList<Grafico>();
	}
	
	public List<Grafico> getGraphics(){
		return graphics;
	}
	
	/**
	 * Muestra el gráfico por pantalla
	 * 
	 */
	public void dibujar(){
		
	}
	/**
	 * Añade un nuevo gráfico
	 * 
	 */
	public void addGraphic(Grafico g, int dim1, int dim2, int dim3, double[] hp){
		graphics.add(proyectar(g, dim1, dim2, dim3, hp));
	}
		
	private Grafico proyectar(Grafico g, int dim1, int dim2, int dim3, double[] hp){
		Grafico graf = new Grafico(3);
		for (int i = 0; i < g.getDim(); ++i) {
			Vertex v = g.getImagen().get(i);
			int j = 0, cont = 0;
			boolean b = true;
			while (b && j < hp.length) {
				while (v.at(dim1) == dim1 || v.at(dim2) == dim2 || v.at(dim3) == dim3) 
					++cont;
				if (v.at(cont) != hp[j]) 
					b = false;
				++j;
			}
			if (b) {
				Vertex newV = new Vertex();
				newV.set(0, v.at(dim1));
				newV.set(0, v.at(dim2));
				newV.set(0, v.at(dim3));
				graf.getImagen().add(newV);
			}
		}
		return graf;
	}
}
