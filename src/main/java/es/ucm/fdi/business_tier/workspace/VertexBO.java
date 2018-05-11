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

import es.ucm.fdi.exceptions.NoMatchDimensionException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * VertexBO defines a new type which uses an internal array with an specified
 * number of components. The vertex could be initalized using an array of double
 * values, or using directly a list of double values as parameters. It could
 * also be declared by indicating just the dimension, creating a vertex at the
 * origin. The null vertex could be instantiated by calling the constructor
 * without parameters, then the dimension is taken to be zero and the array
 * takes the null value.
 * <b>Changes in the new version still not commented</b>.
 *
 * @author Arturo Acuaviva Huertos
 */
public class VertexBO implements Iterable<Double>, Cloneable {

    /**
     * The value of the dimension of the space in which the vertex is
     * instantiated.
     */
    private int dim;

    /**
     * The array with the components of the vertex.
     */
    private double[] cmps;

    /**
     * Class constructor specifying number of objects to create.
     *
     * @param dim number of componentes of the vertex
     * @param cmps array with the value of the components
     * @throws NoMatchDimensionException dim does not match cmps.length.
     */
    public VertexBO(int dim, double[] cmps) throws NoMatchDimensionException {
        if (dim != cmps.length) {
            throw new NoMatchDimensionException(dim, cmps.length);
        }
        this.dim = dim;
        this.cmps = cmps.clone();
    }

    /**
     * Class constructor specifying a number of points for the vertex
     *
     * @param cmps array with the values of the points
     */
    public VertexBO(double... cmps) {
        this.dim = cmps.length;
        this.cmps = cmps.clone();
    }

    /**
     * Class constructor specifying the dimension of the vertex and setting the
     * vertex at the origin.
     *
     * @param dim number of componentes of the vertex
     * @throws NoMatchDimensionException the dimension is not valid
     */
    public VertexBO(int dim) throws NoMatchDimensionException {
        if (dim < 0) {
            throw new NoMatchDimensionException(dim);
        }
        this.dim = dim;
        this.cmps = new double[dim];
        for (double d : this.cmps) {
            d = 0;
        }
    }

    /**
     * Class constructor specifying no parameters, creates a null vertex
     */
    public VertexBO() {
        this.dim = 0;
        this.cmps = null;
    }

    /**
     * Accessor method to get the value in the vertex at position n
     *
     * @param n n-position in the vertex N dimensional
     * @throws ArrayIndexOutOfBoundsException the value n does not belong to the
     * interval (0, dim)
     * @return this.cmps[n]
     */
    public double at(int n) throws ArrayIndexOutOfBoundsException {
        if (n < this.dim && n >= 0) {
            return this.cmps[n];
        } else {
            throw new ArrayIndexOutOfBoundsException("n value must belong to"
                    + " the interval (0, " + this.dim + ")");
        }
    }

    /**
     * Accessor method to get the dimension numeric value
     *
     * @return this.dim
     */
    public int getDimension() {
        return this.dim;
    }

    /**
     * Mutator method to set the value in the vertex at position n
     *
     * @param n n-position in the vertex N dimensional
     * @param v double value to be set
     * @throws ArrayIndexOutOfBoundsException the value n does not belong to the
     * interval (0, dim)
     */
    public void set(int n, double v) {
        if (n < this.dim && n >= 0) {
            this.cmps[n] = v;
        } else {
            throw new ArrayIndexOutOfBoundsException("n value must belong to the"
                    + " interval (0, " + this.dim + ")");
        }
    }

    /**
     * Generates a VertexBOIterator in order to provide a way of iterate the
     * VertexBO
     */
    @Override
    public Iterator<Double> iterator() {
        return new VertexBOIterator();
    }

    /**
     * Returns a string with all the values of the VertexBO concatenated
     *
     * @return s coordinates concatenated in a string
     */
    public String toString() {
        String s = "";
        for (double d : this.cmps) {
            s += d + ' ';
        }
        return s;
    }

    /**
     * Iterator inner class VertexBOIterator for a Double VertexBO.
     */
    private final class VertexBOIterator implements Iterator<Double> {

        /**
         * Index for the iterator
         */
        private int cursor;

        /**
         * Class constructor which sets the cursor of the iterator to zero.
         */
        public VertexBOIterator() {
            this.cursor = 0;
        }

        /**
         * It returns if there are more elements available in the vertex.
         *
         * @return this.cursor < VertexBO.this.dim
         */
        public boolean hasNext() {
            return this.cursor < VertexBO.this.dim;
        }

        /**
         * It returns the next value in the VertexBO, if there are no more
         * elements it throws an exception NoSuchElementException.
         *
         * @throws NoSuchElementException iterator out of range.
         * @return VertexBO.this.cmps[this.cursor]
         */
        public Double next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return VertexBO.this.cmps[this.cursor++];
        }
    }

    /**
     * Returns a VertexBO with the same values of this VertexBO
     *
     * @return clone a copy of VertexBO
     */
    protected VertexBO clone() {
        VertexBO clone = null;
        try {
            clone = (VertexBO) super.clone();
            clone.cmps = new double[clone.dim]; //deep copying 
        } catch (CloneNotSupportedException cns) {
            cns.printStackTrace();
        }
        return clone;
    }

    /**
     * Returns whether the object of the argument is the same as this VertexBO.
     *
     * @param other right hand object for the comparision
     * @return (other == this)
     */
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof VertexBO)) {
            return false;
        }
        VertexBO otherVertexBO = (VertexBO) other;
        if (otherVertexBO.getDimension() != this.getDimension()) {
            return false;
        }
        int i = 0;
        while (i < otherVertexBO.getDimension()
                && otherVertexBO.at(i) == this.at(i)) {
        }
        return i == otherVertexBO.getDimension();
    }

    /**
     * @param other destination vertex used to calculate distance from the
     * current vertex (source).
     * @return The distance from the vertex to the given destination vertex <i>
     * other </i>.
     * @throws NoMatchDimensionException the distance cannot be calculated if
     * dimension do not match
     */
    public double dist(VertexBO other) throws NoMatchDimensionException {
        if (other.getDimension() == dim) {
            double d2 = 0;
            for (int i = 0; i < dim; ++i) {
                d2 += cmps[i] * other.at(i);
            }
            return Math.sqrt(d2);
        } else {
            throw new NoMatchDimensionException("Distance cannot be calculated"
                    + " when mismatching dimensions.");
        }
    }

    /**
     * @return The components of the VertexBO.
     */
    public double[] getComps() {
        return cmps;
    }

    /**
     * @return The distance from the vertex origin.
     * @throws NoMatchDimensionException if the distance is calculated to an
     * object of different dimension
     */
    public double disto() throws NoMatchDimensionException {
        return dist(new VertexBO(dim));
    }
}
