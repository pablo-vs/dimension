package es.ucm.fdi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.trabajo.Vertice;
import es.ucm.fdi.trabajo.funcion.functiontypes.Function;
import es.ucm.fdi.trabajo.funcion.functiontypes.VariablesList;

/**
 * 
 * @author Eloy Mósig
 *
 */

public class FunctionToGraphic {

	public class Grafico {
		private int dimension;
		//cual es el mejor TAD para implementar esta mierda???
		private List<List<Vertice>> objeto;
		private List<List<Vertice>> dominio; //inicialmente dominio contiene (dimension) vértices con todas las coordenadas nulas excepto una
		private int resolucion; 
		//la cuadrícula va a estar formada por cubos de lado 1/2^resolucion


		public void Grafico(int dimension) {
			this.dimension = dimension; //n+1 si la función es de R^n a R
			objeto = new ArrayList<>();
			//inicializo aquí objeto con un vértice en el origen?
			List<Vertice> f1 = new ArrayList<>();
			f1.add(new Vertice(dimension));
			objeto.add(f1);
		}
		
		public void Cuadricula(List<Vertice>dom) {
			
			double lado = 1/2^resolucion;
			//aristas
			for(Vertice v: dom) {
				for(int i = 0; i < dimension; ++i) {
					dominio.add(new ArrayList<Vertice>());
					dominio.get(i).add(dom.get((i)));
					for(double j = 0; j < v.at(i); j+=lado) {
						Vertice a = v;
						a.set(i, v.at(i) + j);
						dominio.get(i).add(a);
					}
				}
			}
			//cojo la primera arista y creo hiperplanos en cada nuevo vértice
			
			
		}
		
		public void Generar(Function f, List<Vertice>dom, int res) {
			resolucion = res;
			Cuadricula(dom); //si esto es A, voy a dibujar f(A) 
			//!!!Por ahora A es un rectángulo en el primer cuadrante con un vértice en el origen
			VariablesList x = f.getVars();
			for(int i = 0; i < dimension; ++i) {
				objeto.add(new ArrayList<Vertice>());
				for(Vertice v: dominio.get(i)) {
					objeto.get(i).add(new Vertice(f.evaluate(x)));
				}
			}		
		}
	}
}
