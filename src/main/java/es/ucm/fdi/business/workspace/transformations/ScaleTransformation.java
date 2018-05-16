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
 * Provides a way of dealing with scale transformations in 3D. Given a certain
 * number it will shrink the graph proportionally.
 *
 * @author Brian Leiva, Inmaculada Pérez
 */
public class ScaleTransformation extends TransformationStrategy {

    /**
     * Class constructor specifying the amount to shrink in each axis.
     *
     * @param x X axis
     * @param y Y axis
     * @param z Z axis
     */
    public ScaleTransformation(double x, double y, double z) {
        super(x, y, z);
    }


    /**
     * Modifies the given graph shrinking it proportionally to 
     * the value specified by x,y and z.
     * @param g 
     */
    @Override
    public void apply(Graph g) {
        shrinkX(g, x);
        shrinkY(g, y);
        shrinkZ(g, z);
    }

    /**
     * Shrinks the X axis.
     *
     * @param g the graph which is going to modified
     * @param d quantify indicating the amount which will be shrunk
     */
    private static void shrinkX(Graph g, double d) {
        double min = ((Vertex) g.getIteratorRange().next()).at(0), max = min;

        ListIterator iterator = g.getIteratorRange();

        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            double n = v.at(0);
            if (n > max) {
                max = n;
            }
            if (n < min) {
                min = n;
            }
        }
        double newMin = (max * (d - 1) + min * (d + 1) / 2 * d);
        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            v.set(0,
                    ((v.at(0) - min) / d) + newMin);
        }
    }

    /**
     * Shrinks the Y axis.
     *
     * @param g the graph which is going to modified
     * @param d quantify indicating the amount which will be shrunk
     */
    private static void shrinkY(Graph g, double d) {
        double min = ((Vertex) g.getIteratorRange().next()).at(1), max = min;

        ListIterator iterator = g.getIteratorRange();
        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            double n = v.at(1);
            if (n > max) {
                max = n;
            }
            if (n < min) {
                min = n;
            }
        }
        double newMin = (max * (d - 1) + min * (d + 1) / 2 * d);
        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            v.set(1,
                    ((v.at(1) - min) / d) + newMin);
        }
    }

    /**
     * Shrinks the Z axis.
     *
     * @param g the graph which is going to modified
     * @param d quantify indicating the amount which will be shrunk
     */
    private static void shrinkZ(Graph g, double d) {

        double min = ((Vertex) g.getIteratorRange().next()).at(2), max = min;

        ListIterator iterator = g.getIteratorRange();

        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            double n = v.at(2);
            if (n > max) {
                max = n;
            }
            if (n < min) {
                min = n;
            }
        }
        double newMin = (max * (d - 1) + min * (d + 1) / 2 * d);
        while (iterator.hasNext()) {
            Vertex v = (Vertex) iterator.next();
            v.set(2,
                    ((v.at(2) - min) / d) + newMin);
        }
    }
}
