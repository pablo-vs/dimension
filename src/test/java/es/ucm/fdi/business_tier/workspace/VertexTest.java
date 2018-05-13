/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business_tier.workspace;

import es.ucm.fdi.business_tier.exceptions.NoMatchDimensionException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test for Vertex class.
 *
 * @see Vertex
 * @author Arturo Acuaviva
 */
public class VertexTest {
    /**
     * Null vertex with dimension 0 and no cmps
     */
    Vertex vertexNull = new Vertex();
    /**
     * Empty 3D Vertex, it will be set at the origin (0,0,0)
     */
    Vertex vertex3Dempty;
    /**
     * Non-empty 3D Vertex
     */
    Vertex vertex3D;
    /**
     * Non-empty 3D Vertex equals to the first 3D vertex created
     */
    Vertex vertex3Dequal;
    
    /**
     * Empty 4D vertex
     */
    Vertex vertex4Dempty;
    
    /**
     * List of points for a 3D vertex
     */
    double [] cmps = { 2.0, 3.0, 5.0};
    
    public VertexTest() { }
   
    /**
     * Creates all the object that will be used for the tests.
     * @throws NoMatchDimensionException 
     */
    @Before
    public void setUp() throws NoMatchDimensionException {
        vertex3Dempty = new Vertex(3);
        vertex4Dempty = new Vertex(4);
        vertex3D = new Vertex(cmps);
        vertex3Dequal = new Vertex(2.0, 3.0, 5.0);
    }

    /**
     * Test of at method, of class Vertex.
     */
    @Test
    public void testAt() {
        System.out.println("at");
       // At for a non-empty 3D vertex
       assertEquals(2.0, vertex3D.at(0), 0);
        assertEquals(3.0, vertex3D.at(1), 0);
         assertEquals(5.0, vertex3D.at(2), 0);
         // At out of range test
       try{
           vertex3D.at(3);
           fail("You should not be able to access a non-existing dimension");
       } catch (ArrayIndexOutOfBoundsException e){  }
       // At used over a null vertex
       try{
           vertexNull.at(0);
           fail("You should no be able to access to a null vertex");
       }catch(NullPointerException e){
       }
       
    }

    /**
     * Test of getDimension method, of class Vertex.
     */
    @Test
    public void testGetDimension() {
        System.out.println("getDimension");
        // Dimension getters test for all types of vertexes
        assertEquals(0, vertexNull.getDimension(), 0);
        assertEquals(3, vertex3Dempty.getDimension(), 0);
        assertEquals(3, vertex3D.getDimension(), 0);
        assertEquals(3, vertex3Dequal.getDimension(), 0);
        assertEquals(4, vertex4Dempty.getDimension(), 0);
    }

    /**
     * Test of set method, of class Vertex.
     */
    @Test
    public void testSet() {
        System.out.println("set");
        // simple set test
        vertex3Dempty.set(0, 2.0);
        assertEquals(2.0, vertex3Dempty.at(0), 0);
        vertex3Dempty.set(0, 0.0);
        assertEquals(0.0, vertex3Dempty.at(0), 0);
        // set test out of range
        try{
            vertex3Dempty.set(4, 2.3);
            fail("Setting a value out of range");
        } catch(ArrayIndexOutOfBoundsException e){
        }
    }

    /**
     * Test of iterator method, of class Vertex.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        // Iteration over a 3D vertex
       Iterator it = vertex3D.iterator();
       int counter = 0;
       double [] matriz;
       matriz = new double[3];
       while(it.hasNext()){
           try{
               if(counter >= 3){
                   fail("A 3D vertex has 3 elements");
               }
                matriz[counter] = (double) it.next();
                counter++;
           } catch (NoSuchElementException e){
               fail("All elements are available");
           }
       }
       assertEquals(matriz[0], vertex3D.at(0), 0);
       assertEquals(matriz[1], vertex3D.at(1), 0);
       assertEquals(matriz[2], vertex3D.at(2), 0);
       // Null vertex iteration test
       it = vertexNull.iterator();
       while(it.hasNext()){
           fail("A null vertex should have no elements");
       }
       try{
           it.next();
           fail("There are no elements in the vertex");
       }catch(NoSuchElementException e){}
    }

    /**
     * Test of toString method, of class Vertex.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        // Check all the different outputs for all vertexes types
        assertEquals("2.0 3.0 5.0", vertex3D.toString());
        assertEquals("", vertexNull.toString());
        assertEquals("0.0 0.0 0.0", vertex3Dempty.toString());
        assertEquals("0.0 0.0 0.0 0.0", vertex4Dempty.toString());
    }

    /**
     * Test of clone method, of class Vertex.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        Vertex newCopiedVertex = vertex3D.clone();
        // check if it was correctly cloned
       assertEquals(vertex3D, newCopiedVertex);
       newCopiedVertex.set(0, 1.0);
       // we check that the value was deep copied
       assertEquals(1.0, newCopiedVertex.at(0), 0);
       assertEquals(2.0, vertex3D.at(0), 0);
       // we check if dimensions match
       assertEquals(3, newCopiedVertex.getDimension());
       assertEquals(3, vertex3D.getDimension());
    }

    /**
     * Test of equals method, of class Vertex.
     * @throws es.ucm.fdi.business_tier.exceptions.NoMatchDimensionException
     */
    @Test
    public void testEquals() throws NoMatchDimensionException {
        System.out.println("equals");
        // Checking equals correction for all the vertexes types
        assertEquals(vertex3Dempty, new Vertex(3));
        assertEquals(vertex4Dempty, new Vertex(4));
        assertEquals(vertexNull, new Vertex());
        assertEquals(vertex3D, new Vertex(cmps));
        // Checking equals when not equal for different types of vertexes
        assertFalse(vertex3Dempty.equals(vertex3D));
        assertFalse(vertex3D.equals(vertex3Dempty));
        assertFalse(vertexNull.equals(vertex3D));
        assertFalse(vertex3D.equals(vertexNull));
        assertFalse(vertex4Dempty.equals(vertex3D));
        assertFalse(vertex3D.equals(vertex4Dempty));
        assertFalse(vertex4Dempty.equals(vertex3Dempty));
        assertFalse(vertex3Dempty.equals(vertex4Dempty));
    }

    /**
     * Test of distanceTo method, of class Vertex.
     */
    @Test
    public void testDistanceTo() throws Exception {
        // 6.164414002968976
        System.out.println("distanceTo");
        // Vertex from an origin and a 3D vertex not in the origin distance calculation
        assertEquals(6.164, vertex3D.distanceTo(vertex3Dempty), 0.001);
        assertEquals(6.164, vertex3Dempty.distanceTo(vertex3D), 0.001);
        assertEquals(0.0, vertex3D.distanceTo(vertex3D), 0);
        assertEquals(0.0, vertex3Dempty.distanceTo(vertex3Dempty), 0);
        // Different dimension and null vertex distance calculation
        try{
            vertex3D.distanceTo(vertex4Dempty);
            vertex4Dempty.distanceTo(vertex3D);
            vertex4Dempty.distanceTo(vertex3Dempty);
            vertex3Dempty.distanceTo(vertex4Dempty);
            vertexNull.distanceTo(vertex3D);
            vertex3D.distanceTo(vertexNull);
            fail("Distance between different dimension vertexes cannot be"
                    + " calculated");
        } catch(NoMatchDimensionException e){   }
    }

    /**
     * Test of getComps method, of class Vertex.
     */
    @Test
    public void testGetComps() {
        System.out.println("getComps");
       // Obtaining non-empty vertex components and modifying them
       double [] components = vertex3D.getComps();
       assertEquals(vertex3D.at(0), components[0], 0);
       assertEquals(vertex3D.at(1), components[1], 0);
       assertEquals(vertex3D.at(2), components[2], 0);
       double previousVertex3D = vertex3D.at(1);
       components[1] = previousVertex3D+1;
       assertEquals(vertex3D.at(1), previousVertex3D, 0);
       assertEquals(components[1], previousVertex3D+1, 0);
       
       //Obtaining components from a null vertex
       assertTrue(Arrays.equals(null, vertexNull.getComps()));
    }

    /**
     * Test of distanceFromOrigin method, of class Vertex.
     */
    @Test
    public void testDistanceFromOrigin() throws Exception {
        System.out.println("distanceFromOrigin");
        // vertexes at the origin 
        assertEquals(0.0, vertex4Dempty.distanceFromOrigin(), 0);
       assertEquals(0.0, vertex3Dempty.distanceFromOrigin(), 0);
       // null vertex distance
       assertEquals(0.0, vertexNull.distanceFromOrigin(), 0);
       // vertex not at the origin
       assertEquals(6.1644, vertex3D.distanceFromOrigin(), 0.0001);
    }

}
