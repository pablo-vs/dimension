package es.ucm.fdi.business_tier.workspace;

import es.ucm.fdi.business_tier.workspace.Visualization;
import es.ucm.fdi.business_tier.workspace.Vertex;
import es.ucm.fdi.business_tier.workspace.Graph;
import es.ucm.fdi.business_tier.exceptions.NoMatchDimensionException;
import java.util.Iterator;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

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
    Graph graf = new Graph(5);

    public void initialize() {

        graf.add(v1);
        graf.add(v2);
        graf.add(v3);
        graf.add(v4);
        graf.add(v5);
        graf.add(v6);
        graf.add(v7);
        graf.add(v8);
        graf.add(v9);
        graf.add(v10);
        graf.add(v11);
        graf.add(v12);
    }

    @Test
    public void projectGraphTest() throws NoMatchDimensionException {
        
        initialize();
        
        double results[] =
        {
            7, 1, 4,
            3, 5, 6,
            8, 3, 0,
            0, 0, 0,
            2, 9, 6
        };
        int counter = 0;
        
        Graph newGraph = Visualization.projectGraph(graf, 1, 3, 4, hp);
        Iterator newIt = newGraph.getCompositeIterator();
        
        while(newIt.hasNext()){
            Vertex v =  (Vertex) newIt.next();
            assertTrue("The new graph has correct list of new vertexes",
                    v.at(0) == results[counter] && 
                    v.at(1) == results[1 +counter] &&
                    v.at(2) == results[2 + counter]);
            counter += 3; 
        }
    }
}
