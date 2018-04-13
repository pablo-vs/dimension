package es.ucm.fdi.workspace;

import es.ucm.fdi.util.FunctionParserUtils;
import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.fdi.workspace.function.types.VariablesList;
import java.util.ArrayList;
import java.util.List;

/**
 * A graph object is used to represent an abstract interpretration of a function.
 * It contains two lists of {@link es.ucm.fdi.workspace VertexBO} with values
 * from the range and the domain of the function. It also provides 
 * @author Brian Leiva
 * @author Eloy Mósig
 */
public class Graph {
   
        /**
         * Integer value representing the dimension of the object depicted by the graph.
         */
        private int dim;
        
        /**
        *  List of {@link es.ucm.fdi.workspace VertexBO} containing the vertex in the domain.
        */
	private List<VertexBO> domain;
        
        /**
        *  List of {@link es.ucm.fdi.workspace VertexBO} containing the vertex in the range.
        */
        private List<VertexBO> range;
        
        
	private MultiTreeMap<Integer, Integer> objeto;
        /**
         * Resolution of the graph, it indicates the factor used to calculate the length of the
         */
	private int res; 

		public Graph(int dimension) {
			this.dim = dimension;
			objeto = new MultiTreeMap<>((a, b) -> a - b);
			domain = new ArrayList<>();
		}
		
		public void getGrid(double[] dom_ini, double[] dom_fin) {
			
			if ( dom_ini.length != dom_fin.length) throw new IllegalArgumentException();
			double lado = 1/(Math.pow(2,res)); 
			int[] tam = new int[dom_ini.length];
			int dim = 1;
			for(int i = 0; i < tam.length; ++i) {
				tam[i] = (int) ((dom_fin[i] - dom_ini[i]) / lado);
				dim *= tam[i];
			}
			for(int j = 0; j < dim; ++j) {
				domain.add(new VertexBO());
			}
			int n = 1;
			for(int i = 0; i < dim; ++i) {
				double suma = dom_ini[i];
				int aux = n;
				n *= tam[i];
				for(int j = 0; j < tam[i]; ++j) {
					suma += lado;
					for (int k = j * aux; k < domain.size(); k += n){
						int cont = 0;
						while (cont < aux){
							domain.get(k).set(i, suma);
							if (j > 0) objeto.putValue(k, k - (j * aux));
							++cont;
						}
					}
				}
			}
		}
		
		public void generate(List<String> s, double[] dom_ini, double[] dom_fin, int res) {
			this.res = res;
			getGrid(dom_ini, dom_fin);
			for(int i = 0; i < domain.size(); ++i) {
				VariablesList varList = new VariablesList(domain.get(i).getDimension());
				for(int j = 0; j < domain.get(i).getDimension(); ++j) {
					varList.setVariable(j, domain.get(i).at(j));
				}
				VertexBO fv = new VertexBO(s.size());
				FunctionBO f;
				for(int j = 0; j < s.size(); ++j) {
					f = FunctionParserUtils.parse(s.get(j), varList);

					fv.set(j, f.evaluate(varList));
				}
				range.add(fv);
			}		
		}
		
		public int getDim(){
			return dim;
		}
		
		public List<VertexBO> getRange(){

			return range;
		}
	}
