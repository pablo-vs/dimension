package es.ucm.fdi.trabajo;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Eduardo Amaya
 * @author Javier Galiana
 *
 */
public class Visualization {
	private int partida;
	private int imagen;
	private List<Grafico> graphics;
	private int tam;
	
	public Visualization(ArrayList<Grafico> g){
		graphics = new ArrayList<Grafico>();
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
	public void addGraphic(Grafico g){
		graphics.add(proyectar(g));
	}
	
	private Grafico proyectar(Grafico g){
		
	}
}
