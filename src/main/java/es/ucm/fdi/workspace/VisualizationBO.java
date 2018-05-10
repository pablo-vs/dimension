package es.ucm.fdi.workspace;

import es.ucm.fdi.exceptions.NoMatchDimensionException;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Eduardo Amaya
 * @author Javier Galiana
 * @author Brian Leiva
 *
 */
public class VisualizationBO {

    private ArrayList<GraphBO> graphics;

    public VisualizationBO() {
        graphics = new ArrayList<>();
    }

    public VisualizationBO(List<GraphBO> g) {
        graphics = new ArrayList<>(g);
    }

    public List<GraphBO> getGraph() {
        return graphics;
    }

    /**
     * Añade un nuevo gráfico
     *
     */
    public void addGraph(GraphBO g, int dim1, int dim2, int dim3, double[] hp) throws NoMatchDimensionException {
        graphics.add(proyectar(g, dim1, dim2, dim3, hp));
    }

    private GraphBO proyectar(GraphBO g, int dim1, int dim2, int dim3, double[] hp) throws NoMatchDimensionException {
        GraphBO graf = new GraphBO(3);

        for (int i = 0; i < g.getRange().size(); ++i) {
            VertexBO v = g.getRange().get(i);
            int j = 0, cont = 0;
            boolean b = true;
            while (b && j < hp.length) {
                while (cont == dim1 || cont == dim2 || cont == dim3) {
                    ++cont;
                }
                if (cont < g.getDim()) {
                    if (Math.abs(v.at(cont) - hp[j]) > 0.1) {
                        b = false;
                    }
                }
                ++j;
                ++cont;
            }
            if (b) {
                VertexBO newV = new VertexBO(3);
                newV.set(0, v.at(dim1));
                newV.set(1, v.at(dim2));
                newV.set(2, v.at(dim3));
                graf.getRange().add(newV);
            }
        }
        return graf;
    }
}
