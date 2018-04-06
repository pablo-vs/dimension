package es.ucm.fdi.util;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.workspace.Vertex;
import es.ucm.fdi.workspace.Function;
import es.ucm.fdi.workspace.function.types.VariablesList;

/**
 * 
 * 
 * @author Brian Leiva
 * @author Eloy Mósig
 */

public class FunctionToGraphic {

	public class Grafico {
		private int dimension;
		private List<Vertex> dominio, imagen;
		private MultiTreeMap<Integer, Integer> objeto;
		private int resolucion; 

		public Grafico(int dimension) {
			this.dimension = dimension;
			objeto = new MultiTreeMap<>((a, b) -> a - b);
			dominio = new ArrayList<>();
		}
		
		public void getGrid(double[] dom_ini, double[] dom_fin) {
			
			if (dom_ini.length != dom_fin.length) throw new IllegalArgumentException();
			double lado = 1/2^resolucion;
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
		
		public void generate(List<String> s, double[] dom_ini, double[] dom_fin, int res) {
			resolucion = res;
			getGrid(dom_ini, dom_fin);
			for(int i = 0; i < dominio.size(); ++i) {
				VariablesList varList = new VariablesList(dominio.get(i).getDimension());
				for(int j = 0; j < dominio.get(i).getDimension(); ++j) {
					varList.setVariable(j, dominio.get(i).at(j));
				}
				Vertex fv = new Vertex(s.size());
				Function f;
				for(int j = 0; j < s.size(); ++j) {
					f = FunctionParserUtils.parse(s.get(j), varList);

					fv.set(j, f.evaluate(varList));
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
}
