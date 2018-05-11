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
 * This class provides a way of dealing with rotation in 3D. Given the
 * quantities representing the rotations in each axis it modifies the graph
 * accordingly.
 *
 * @author Brian Leiva, Inmaculada Pérez
 */
public class RotateTransformation implements GraphTransformationBO {

    /**
     * Amount to rotate in the X axis
     */
    private final double x;
    /**
     * Amount to rotate in the Y axis
     */
    private final double y;
    /**
     * Amount to rotate in the Z axis
     */
    private final double z;

    /**
     * Class constructor receiving the number representing the rotation in each
     * axis.
     *
     * @param x
     * @param y
     * @param z
     */
    public RotateTransformation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * It rotates a given graph as it was indicated in the constructor.
     *
     * @param g the graph which is going to be modified
     */
    @Override
    public void apply(GraphBO g) {
        rotateX(g, x);
        rotateY(g, y);
        rotatZ(g, z);
    }

    /**
     * Rotates in the X axis the given graph
     *
     * @param g the graph which will be rotated
     * @param d
     */
    private static void rotateX(GraphBO g, double d) {
        double minY = g.getRange().get(0).at(1), maxY = minY,
                minZ = g.getRange().get(0).at(2), maxZ = minZ;
        for (int i = 1; i < g.getRange().size(); ++i) {
            double n1 = g.getRange().get(i).at(1), n2 = g.getRange().get(i).at(2);
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
        double centerY = (maxY + minY) / 2, centerZ = (maxZ + minZ) / 2;
        for (int i = 0; i < g.getRange().size(); ++i) {
            double y = g.getRange().get(i).at(1), z = g.getRange().get(i).at(2), dist;
            dist = Math.sqrt(Math.pow(y - centerY, 2) + Math.pow(z - centerZ, 2));
            g.getRange().get(i).set(1, y + dist * Math.cos(d));
            g.getRange().get(i).set(2, z + dist * Math.sin(d));
        }
    }

    /**
     * Rotates in the Y axis the given graph
     *
     * @param g the graph which will be rotated
     * @param d
     */
    private static void rotateY(GraphBO g, double d) {
        double minX = g.getRange().get(0).at(0), maxX = minX,
                minZ = g.getRange().get(0).at(2), maxZ = minZ;
        for (int i = 1; i < g.getRange().size(); ++i) {
            double n1 = g.getRange().get(i).at(0), n2 = g.getRange().get(i).at(2);
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
        double centerX = (maxX + minX) / 2, centerZ = (maxZ + minZ) / 2;
        for (int i = 0; i < g.getRange().size(); ++i) {
            double x = g.getRange().get(i).at(0), z = g.getRange().get(i).at(2), dist;
            dist = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));
            g.getRange().get(i).set(0, x + dist * Math.sin(d));
            g.getRange().get(i).set(2, z + dist * Math.cos(d));
        }
    }

    /**
     * Rotates in the Z axis the given graph
     *
     * @param g the graph which will be rotated
     * @param d
     */
    private static void rotatZ(GraphBO g, double d) {
        double minX = g.getRange().get(0).at(0), maxX = minX,
                minY = g.getRange().get(0).at(1), maxY = minY;
        for (int i = 1; i < g.getRange().size(); ++i) {
            double n1 = g.getRange().get(i).at(0), n2 = g.getRange().get(i).at(1);
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
        double centerX = (maxX + minX) / 2, centerY = (maxY + minY) / 2;
        for (int i = 0; i < g.getRange().size(); ++i) {
            double x = g.getRange().get(i).at(0), y = g.getRange().get(i).at(1), dist;
            dist = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
            g.getRange().get(i).set(0, x + dist * Math.cos(d));
            g.getRange().get(i).set(1, y + dist * Math.sin(d));
        }
    }
}
