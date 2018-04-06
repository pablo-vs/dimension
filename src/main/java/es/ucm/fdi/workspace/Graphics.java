package es.ucm.fdi.workspace;

import es.ucm.fdi.util.MultiTreeMap;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Leiva
 * @author Eloy Mósig
 */
public class Graphics {
		private int dimension;
		private List<Vertex> dominio, imagen;
		private MultiTreeMap<Integer, Integer> objeto;
		private int resolution; 

		public Graphics(int dimension) {
			this.dimension = dimension;
			objeto = new MultiTreeMap<>((a, b) -> a - b);
			dominio = new ArrayList<>();
			imagen = new ArrayList<>();
		}
		
		public void getGrid(double[] dom_ini, double[] dom_fin) {
			
			if (dom_ini.length != dom_fin.length) throw new IllegalArgumentException();
			double lado = 1/2^resolution;
			int[] tam = new int[dom_ini.length];
			int dim = 1;
			for(int i = 0; i < tam.length; ++i) {
				tam[i] = (int) ((dom_fin[i] - dom_ini[i]) / lado);
				dim *= tam[i];
			}
			for(int j = 0; j < dim; ++j) {
				dominio.add(new Vertex());
			}
			int n = 1;
			for(int i = 0; i < dimension; ++i) {
				double suma = dom_ini[i];
				int aux = n;
				n *= tam[i];
				for(int j = 0; j < tam[i]; ++j) {
					suma += lado;
					for (int k = j * aux; k < dominio.size(); k += n){
						int cont = 0;
						while (cont < aux){
							dominio.get(k).set(i, suma);
							if (j > 0) objeto.putValue(k, k - (j * aux));
							++cont;
						}
					}
				}
			}
		}
		
		public void generate(List<Function> functions, double[] dom_ini, double[] dom_fin, int res) {
			resolution = res;
			getGrid(dom_ini, dom_fin);
			for(int i = 0; i < dominio.size(); ++i) {
				Vertex fv = new Vertex(functions.size());
				for(int j = 0; j < functions.size(); ++j) {
					fv.set(j, functions.get(i).evaluate(dominio.get(i).getComps()));
				}
				imagen.add(fv);
			}		
		}
		
		public int getDim(){
			return dimension;
		}
		
		public List<Vertex> getImagen(){

			return imagen;
		}
	}
