package es.ucm.fdi.workspace;

import es.ucm.fdi.exceptions.NoMatchDimensionException;
import es.ucm.fdi.util.MultiTreeMap;
import java.util.ArrayList;
import java.util.List;

/**
 * A graph object is used to represent an abstract interpretration of a
 * function. It contains two lists of {@link es.ucm.fdi.workspace VertexBO} with
 * values from the range and the domain of the function. It also provides
 *
 * @author Brian Leiva, Eloy Mósig
 */
public class GraphBO {

    /**
     * Integer value representing the dimension of the object depicted by the
     * graph.
     */
    private int dimension;

    /**
     * List of {@link es.ucm.fdi.workspace VertexBO} containing the vertex in
     * the domain.
     */
    private List<VertexBO> domain = new ArrayList<>();

    /**
     * List of {@link es.ucm.fdi.workspace VertexBO} containing the vertex in
     * the range.
     */
    private List<VertexBO> range = new ArrayList<>();

    private MultiTreeMap<Integer, Integer> object = new MultiTreeMap<>((a, b) -> a - b);
    /**
     * Resolution of the graph, it indicates the factor used to calculate the
     * length of the
     */
    private int resolution;

    /**
     * Class constructor specifying the dimension of the graph.
     * 
     * @param dimension 
     */
    public GraphBO(int dimension) {
        this.dimension = dimension;
    }

    /**
     * 
     * @param dom_ini
     * @param dom_fin 
     */
    public void getGrid(double[] dom_ini, double[] dom_fin) {
        if (dom_ini.length != dom_fin.length) {
            throw new IllegalArgumentException();
        }
        double lado = 1 / (Math.pow(2, resolution));
        int[] tam = new int[dom_ini.length];
        int dim = 1;
        for (int i = 0; i < tam.length; ++i) {
            tam[i] = (int) ((dom_fin[i] - dom_ini[i]) / lado);
            dim *= tam[i];
        }
        for (int j = 0; j < dim; ++j) {
            domain.add(new VertexBO());
        }
        int n = 1;
        for (int i = 0; i < dim; ++i) {
            double suma = dom_ini[i];
            int aux = n;
            n *= tam[i];
            for (int j = 0; j < tam[i]; ++j) {
                suma += lado;
                for (int k = j * aux; k < domain.size(); k += n) {
                    int cont = 0;
                    while (cont < aux) {
                        domain.get(k).set(i, suma);
                        if (j > 0) {
                            object.putValue(k, k - (j * aux));
                        }
                        ++cont;
                    }
                }
            }
        }
    }

    /**
     * Generates the graph. 
     * 
     * @param functions
     * @param dom_ini
     * @param dom_fin
     * @param resolution
     * @throws NoMatchDimensionException 
     */
    public void generate(List<FunctionBO> functions, double[] dom_ini, double[] dom_fin, int resolution) throws NoMatchDimensionException {
        this.resolution = resolution;
        getGrid(dom_ini, dom_fin);
        for (int i = 0; i < domain.size(); ++i) {
            VertexBO fv = new VertexBO(functions.size());
            for (int j = 0; j < functions.size(); ++j) {
                fv.set(j, functions.get(i).evaluate(domain.get(i).getComps()));
            }
            range.add(fv);
        }
    }

    /**
     * 
     * @return the dimension of the graph
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * 
     * @return the list of vertex of the graph
     */
    public List<VertexBO> getRange() {
        return range;
    }
}
