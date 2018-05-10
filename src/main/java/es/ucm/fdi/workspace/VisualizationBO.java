package es.ucm.fdi.workspace;

import es.ucm.fdi.exceptions.NoMatchDimensionException;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the view of a function.
 *
 * @author Eduardo Amaya, Javier Galiana, Brian Leiva
 */
public class VisualizationBO {

    /**
     * List of graphs
     */
    private ArrayList<GraphBO> graphics = new ArrayList<>();

    /**
     * Empty class constructor.
     */
    public VisualizationBO() {
    }

    /**
     * Class constructor specifying another list of graphs.
     *
     * @param graphics
     */
    public VisualizationBO(List<GraphBO> graphics) {
        this.graphics = new ArrayList<>(graphics);
    }

    /**
     *
     * @return the list of graphs
     */
    public List<GraphBO> getGraph() {
        return graphics;
    }

    /**
     * Adds a new graph to list.
     *
     * @param graph
     * @param dimX
     * @param dimY
     * @param dimZ
     * @param hp
     * @throws NoMatchDimensionException if not found
     */
    public void addGraph(GraphBO graph, int dimX, int dimY, int dimZ, double[] hp) throws NoMatchDimensionException {
        graphics.add(project(graph, dimX, dimY, dimZ, hp));
    }

    /**
     *
     * @param graph
     * @param dimX
     * @param dimY
     * @param dimZ
     * @param hp
     * @return the projected graph
     * @throws NoMatchDimensionException if not found
     */
    private GraphBO project(GraphBO graph, int dimX, int dimY, int dimZ, double[] hp) throws NoMatchDimensionException {
        GraphBO graphAux = new GraphBO(3);

        for (int i = 0; i < graph.getRange().size(); ++i) {
            VertexBO v = graph.getRange().get(i);
            int j = 0, cont = 0;
            boolean b = true;
            while (b && j < hp.length) {
                while (cont == dimX || cont == dimY || cont == dimZ) {
                    ++cont;
                }
                if (cont < graph.getDimension()) {
                    if (Math.abs(v.at(cont) - hp[j]) > 0.1) {
                        b = false;
                    }
                }
                ++j;
                ++cont;
            }
            if (b) {
                VertexBO newV = new VertexBO(3);
                newV.set(0, v.at(dimX));
                newV.set(1, v.at(dimY));
                newV.set(2, v.at(dimZ));
                graphAux.getRange().add(newV);
            }
        }
        return graphAux;
    }
}
