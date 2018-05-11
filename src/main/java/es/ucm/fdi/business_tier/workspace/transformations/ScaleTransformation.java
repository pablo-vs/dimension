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
package es.ucm.fdi.business_tier.workspace.transformations;

import es.ucm.fdi.business_tier.workspace.GraphBO;

/**
 * This class provides a way of dealing with scale transformations in 3D. Given
 * a certain number it will shrink the graph proportionally.
 *
 * @author Brian Leiva, Inmaculada Pérez
 */
public class ScaleTransformation implements GraphTransformationBO {

    /**
     * Amount to shrink the X axis
     */
    private final double x;
    /**
     * Amount to shrink the Y axis
     */
    private final double y;
    /**
     * Amount to shrink the Z axis
     */
    private final double z;

    /**
     * Class constructor specifying the amount to shrink in each axis.
     *
     * @param x X axis
     * @param y Y axis
     * @param z Z axis
     */
    public ScaleTransformation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Receives a certain graph and applies the scale transformation
     *
     * @param g the graph which is going to modified
     */
    @Override
    public void apply(GraphBO g) {
        shrinkX(g, x);
        shrinkY(g, y);
        shrinkZ(g, z);
    }

    /**
     * Shrink the X axis.
     *
     * @param g the graph which is going to modified
     * @param d quantify indicating the amount which will be shrunk
     */
    private static void shrinkX(GraphBO g, double d) {
        double min = g.getRange().get(0).at(0), max = min;
        for (int i = 1; i < g.getRange().size(); ++i) {
            double n = g.getRange().get(i).at(0);
            if (n > max) {
                max = n;
            }
            if (n < min) {
                min = n;
            }
        }
        double newMin = (max * (d - 1) + min * (d + 1) / 2 * d);
        for (int i = 0; i < g.getRange().size(); ++i) {
            g.getRange().get(i).set(0,
                    ((g.getRange().get(i).at(0) - min) / d) + newMin);
        }
    }

    /**
     * Shrink the Y axis.
     *
     * @param g the graph which is going to modified
     * @param d quantify indicating the amount which will be shrunk
     */
    private static void shrinkY(GraphBO g, double d) {
        double min = g.getRange().get(0).at(1), max = min;
        for (int i = 1; i < g.getRange().size(); ++i) {
            double n = g.getRange().get(i).at(1);
            if (n > max) {
                max = n;
            }
            if (n < min) {
                min = n;
            }
        }
        double newMin = (max * (d - 1) + min * (d + 1) / 2 * d);
        for (int i = 0; i < g.getRange().size(); ++i) {
            g.getRange().get(i).set(1,
                    ((g.getRange().get(i).at(1) - min) / d) + newMin);
        }
    }

    /**
     * Shrink the Z axis.
     *
     * @param g the graph which is going to modified
     * @param d quantify indicating the amount which will be shrunk
     */
    private static void shrinkZ(GraphBO g, double d) {
        double min = g.getRange().get(0).at(2), max = min;
        for (int i = 1; i < g.getRange().size(); ++i) {
            double n = g.getRange().get(i).at(2);
            if (n > max) {
                max = n;
            }
            if (n < min) {
                min = n;
            }
        }
        double newMin = (max * (d - 1) + min * (d + 1) / 2 * d);
        for (int i = 0; i < g.getRange().size(); ++i) {
            g.getRange().get(i).set(2,
                    ((g.getRange().get(i).at(2) - min) / d) + newMin);
        }
    }
}
