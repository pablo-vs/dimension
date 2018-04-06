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
	private List<Graphics> graphics;
	
	public Visualization(){
		graphics = new ArrayList<Graphics>();
	}
	
	public Visualization(ArrayList<Graphics> g){
		graphics = g;
	}
	
	public List<Graphics> getGraphics(){
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
	public void addGraphic(Graphics g, int dim1, int dim2, int dim3, double[] hp){
		graphics.add(proyectar(g, dim1, dim2, dim3, hp));
	}
		
	private Graphics proyectar(Graphics g, int dim1, int dim2, int dim3, double[] hp){
		Graphics graf = new Graphics(3);
		for (int i = 0; i < g.getImagen().size(); ++i) {
			Vertex v = g.getImagen().get(i);
			int j = 0, cont = 0;
			boolean b = true;
			while (b && j < hp.length) {
				while (cont == dim1 || cont == dim2 || cont == dim3) 
					++cont;
				if (cont < g.getDim()) {
					if (Math.abs(v.at(cont) - hp[j]) > 0.1) 
					b = false;
				}
				++j;
				++cont;
			}
			if (b) {
				Vertex newV = new Vertex(3);
				newV.set(0, v.at(dim1));
				newV.set(1, v.at(dim2));
				newV.set(2, v.at(dim3));
				graf.getImagen().add(newV);
			}
		}
		return graf;
	}
}
