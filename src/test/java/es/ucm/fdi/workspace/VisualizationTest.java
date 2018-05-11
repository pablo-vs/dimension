package es.ucm.fdi.workspace;

import es.ucm.fdi.business_tier.workspace.VisualizationBO;
import es.ucm.fdi.business_tier.workspace.Vertex;
import es.ucm.fdi.business_tier.workspace.GraphBO;
import es.ucm.fdi.business_tier.exceptions.NoMatchDimensionException;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.List;

public class VisualizationTest {

    double[] n1 = {1, 3, 2, 4, 6}, n2 = {3, 7, 5, 1, 4}, n3 = {2, 8, 4, 2, 1},
            n4 = {3, 3, 5, 5, 6}, n5 = {3, 8, 5, 3, 0}, n6 = {1, 0, 0, 0, 0},
            n7 = {1, 2, 2, 4, 4}, n8 = {1, 0, 2, 5, 0}, n9 = {1, 1, 2, 1, 2},
            n10 = {3, 0, 5, 0, 0}, n11 = {3, 2, 5, 9, 6}, n12 = {5, 3, 5, 2, 1},
            hp = {3, 5};
    Vertex v1 = new Vertex(n1);
    Vertex v2 = new Vertex(n2), v3 = new Vertex(n3), v4 = new Vertex(n4),
            v5 = new Vertex(n5), v6 = new Vertex(n6), v7 = new Vertex(n7),
            v8 = new Vertex(n8), v9 = new Vertex(n9), v10 = new Vertex(n10),
            v11 = new Vertex(n11), v12 = new Vertex(n12);
    GraphBO graf = new GraphBO(5);

    public void inicio() {

        graf.getRange().add(v1);
        graf.getRange().add(v2);
        graf.getRange().add(v3);
        graf.getRange().add(v4);
        graf.getRange().add(v5);
        graf.getRange().add(v6);
        graf.getRange().add(v7);
        graf.getRange().add(v8);
        graf.getRange().add(v9);
        graf.getRange().add(v10);
        graf.getRange().add(v11);
        graf.getRange().add(v12);
    }

    @Test
    public void proyeccionTest() throws NoMatchDimensionException {
        inicio();
        VisualizationBO vis = new VisualizationBO();
        vis.addGraph(graf, 1, 3, 4, hp);
        List<Vertex> v = vis.getGraph().get(0).getRange();
        assertTrue("La proyección debe tener 5 puntos", v.size() == 5);
        assertTrue("Las coordenadas de los vectores proyección son correctas (se comprueba en un punto)", v.get(0).at(0) == 7 && v.get(0).at(1) == 1 && v.get(0).at(2) == 4);

    }
}
