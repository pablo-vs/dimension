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
package es.ucm.fdi.business.workspace.transformations;

import es.ucm.fdi.business.workspace.Graph;
import es.ucm.fdi.business.workspace.Vertex;
import java.util.ListIterator;

/**
 * This class provides a way of dealing with rotation in 3D. Given the
 * quantities representing the rotations in each axis it modifies the graph
 * accordingly.
 *
 * @author Brian Leiva, Inmaculada Pérez
 */
public class RotateTransformation extends TransformationStrategy {

    /**
     * Class constructor receiving the number representing the rotation in each
     * axis.
     *
     * @param x
     * @param y
     * @param z
     */
    public RotateTransformation(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Rotates a given graph as it was indicated in the constructor.
     *
     * @param g the graph which is going to be modified
     */
    @Override
    public void apply(Graph g) {
        rotateX(g, x);
        rotateY(g, y);
        rotateZ(g, z);
    }

    /**
     * Rotates in the X axis the given graph
     *
     * @param g the graph which will be rotated
     * @param d
     */
    private static void rotateX(Graph g, double d) {

        double minY = ((Vertex) g.getIteratorRange().next()).at(1), maxY = minY,
                minZ = ((Vertex) g.getIteratorRange().next()).at(2), maxZ = minZ;

        ListIterator iterator = g.getIteratorRange();

        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            double n1 = v.at(1), n2 = v.at(2);
            if (n1 > maxY) {
                maxY = n1;
            }
            if (n1 < minY) {
                minY = n1;
            }
            if (n2 > maxZ) {
                maxZ = n2;
            }
            if (n2 < minZ) {
                minZ = n2;
            }
        }
        iterator = g.getIteratorRange();
        double centerY = (maxY + minY) / 2, centerZ = (maxZ + minZ) / 2;
        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            double y = v.at(1), z = v.at(2), a = Math.atan((z - centerZ) / (y - centerY)), dist;
            dist = Math.sqrt(Math.pow(y - centerY, 2) + Math.pow(z - centerZ, 2));
            if (y < centerY) a += Math.PI;
            v.set(1, Math.round((centerY + dist * Math.cos(a + d)) * Math.pow(10, 2)) / Math.pow(10, 2));
            v.set(2, Math.round((centerZ + dist * Math.sin(a + d)) * Math.pow(10, 2)) / Math.pow(10, 2));
        }
    }

    /**
     * Rotates in the Y axis the given graph
     *
     * @param g the graph which will be rotated
     * @param d
     */
    private static void rotateY(Graph g, double d) {

        double minX = ((Vertex) g.getIteratorRange().next()).at(0), maxX = minX,
                minZ = ((Vertex) g.getIteratorRange().next()).at(2), maxZ = minZ;
        ListIterator iterator = g.getIteratorRange();

        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            double n1 = v.at(0), n2 = v.at(2);
            if (n1 > maxX) {
                maxX = n1;
            }
            if (n1 < minX) {
                minX = n1;
            }
            if (n2 > maxZ) {
                maxZ = n2;
            }
            if (n2 < minZ) {
                minZ = n2;
            }
        }
        iterator = g.getIteratorRange();
        double centerX = (maxX + minX) / 2, centerZ = (maxZ + minZ) / 2;
        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            double x = v.at(0), z = v.at(2), a = Math.atan((x - centerX) / (z - centerZ)), dist;
            dist = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));
            if (z < centerZ) a += Math.PI;
            v.set(0, Math.round((centerX + dist * Math.sin(a + d)) * Math.pow(10, 2)) / Math.pow(10, 2));
            v.set(2, Math.round((centerZ + dist * Math.cos(a + d)) * Math.pow(10, 2)) / Math.pow(10, 2));
        }
    }

    /**
     * Rotates in the Z axis the given graph
     *
     * @param g the graph which will be rotated
     * @param d
     */
    private static void rotateZ(Graph g, double d) {
        double minX = ((Vertex) g.getIteratorRange().next()).at(0), maxX = minX,
                minY = ((Vertex) g.getIteratorRange().next()).at(1), maxY = minY;
        ListIterator iterator = g.getIteratorRange();

        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();

            double n1 = v.at(0), n2 = v.at(1);
            if (n1 > maxX) {
                maxX = n1;
            }
            if (n1 < minX) {
                minX = n1;
            }
            if (n2 > maxY) {
                maxY = n2;
            }
            if (n2 < minY) {
                minY = n2;
            }
        }
        iterator = g.getIteratorRange();
        double centerX = (maxX + minX) / 2, centerY = (maxY + minY) / 2;
        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            double x = v.at(0), y = v.at(1), a = Math.atan((y - centerY) / (x - centerX)), dist;
            dist = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
            if (x < centerX) a += Math.PI;
            v.set(0, Math.round((centerX + dist * Math.cos(a + d)) * Math.pow(10, 2)) / Math.pow(10, 2));
            v.set(1, Math.round((centerY + dist * Math.sin(a + d)) * Math.pow(10, 2)) / Math.pow(10, 2));
        }
    }
}
