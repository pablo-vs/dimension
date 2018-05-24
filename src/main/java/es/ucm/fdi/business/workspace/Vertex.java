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
package es.ucm.fdi.business.workspace;

import es.ucm.fdi.business.exceptions.NoMatchDimensionException;
import es.ucm.fdi.business.util.Matrix;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Vertex defines a new type which uses an internal array with an specified
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
public class Vertex implements Iterable<Double>, Cloneable {

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
    public Vertex(int dim, double[] cmps) throws NoMatchDimensionException {
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
    public Vertex(double... cmps) {
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
    public Vertex(int dim) throws NoMatchDimensionException {
        if (dim < 0) {
            throw new NoMatchDimensionException(dim);
        }
        this.dim = dim;
        this.cmps = new double[dim];
        Arrays.fill(cmps, 0);
    }

    /**
     * Class constructor specifying no parameters, creates a null vertex
     */
    public Vertex() {
        this.dim = 0;
        this.cmps = null;
    }

    public Vertex(double[] comps, double[] comps2) {
		this.dim = comps.length + comps2.length;
		this.cmps = new double[dim];
		int i = 0;
		for(;i < comps.length; ++i) cmps[i] = comps[i];
		for(;i < dim; ++i) cmps[i] = comps2[i-comps.length];
	}

	/**
     * Accessor method to get the value in the vertex at position n
     *
     * @param n n-position in the vertex N dimensional
     * @throws ArrayIndexOutOfBoundsException the value n does not belong to the
     * interval (0, dim)
     * @throws NullPointerException when calling at on a null vertex (zero
     * dimension vertex)
     * @return this.cmps[n]
     */
    public double at(int n) throws ArrayIndexOutOfBoundsException, NullPointerException {
        if (n < this.dim && n >= 0) {
            return this.cmps[n];
        } else if (this.dim == 0) {
            throw new NullPointerException("You cannot access to any component"
                    + " of a null vertex (a zero dimension vertex)");
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

    @Override
    public Iterator<Double> iterator() {
        return new VertexBOIterator();
    }

    @Override
    public String toString() {
        if (0 == this.dim) {
            return new String();
        }
        StringBuilder s = new StringBuilder("");
        for (double d : this.cmps) {
            s.append(d).append(' ');
        }
        return s.substring(0, s.length() - 1);
    }

    /**
     * Iterator inner class VertexBOIterator for a Double Vertex.
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

        @Override
        public boolean hasNext() {
            return this.cursor < Vertex.this.dim;
        }

        @Override
        public Double next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return Vertex.this.cmps[this.cursor++];
        }
    }

    @Override
    protected Vertex clone() throws CloneNotSupportedException {
        // null vertex creation
        Vertex clone = new Vertex();
        //deep copying
        clone.dim = this.dim;
        clone.cmps = new double[clone.dim];
        System.arraycopy(this.cmps, 0, clone.cmps, 0, clone.dim);
        return clone;
    }

    /**
     * Returns a hash code value for the vertex. This method is supported for
     * the benefit of hash tables containers.
     *
     * @return int code generated
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.dim;
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Vertex)) {
            return false;
        }
        Vertex otherVertex = (Vertex) other;
        if (otherVertex.getDimension() != this.getDimension()) {
            return false;
        }
        int i = 0;
        while (i < otherVertex.getDimension()
                && otherVertex.at(i) == this.at(i)) {
            i++;
        }
        return i == otherVertex.getDimension();
    }

    /**
     * @param other destination vertex used to calculate euclidean distance from
     * the current vertex (source).
     * @return The distance from the vertex to the given destination vertex <i>
     * other </i>.
     * @throws NoMatchDimensionException the distance cannot be calculated if
     * dimension do not match
     */
    public double distanceTo(Vertex other) throws NoMatchDimensionException {
        if (other.getDimension() == dim) {
            double d2 = 0;
            for (int i = 0; i < dim; ++i) {
                d2 += Math.pow((cmps[i] - other.at(i)), 2);
            }
            return Math.sqrt(d2);
        } else {
            throw new NoMatchDimensionException("Distance cannot be calculated"
                    + " when mismatching dimensions.");
        }
    }

    /**
     * @return The components of the Vertex.
     */
    public double[] getComps() {
        return cmps == null ? null : cmps.clone();
    }
    
    /**
     * Computes the substraction of another vertex from this one.
     * @param other The Vertex to substract.
     * @return The resulting Vertex.
     * @throws NoMatchDimensionException 
     */
    public Vertex substract(Vertex other) throws NoMatchDimensionException {
    	if(this.dim == other.dim) {
	    	Vertex result = new Vertex(dim);
	    	for(int i = 0; i < dim; ++i) {
	    		result.cmps[i] = cmps[i]-other.cmps[i];
	    	}
	    	return result;
    	} else {
    		throw new NoMatchDimensionException("Cannot substract vertices of different dimension");
    	}
    }
    
    /**
     * Computes the dot product of another vertex from this one.
     * @param other The Vertex to multiply.
     * @return The resulting product.
     * @throws NoMatchDimensionException 
     */
    public double dotProduct(Vertex other) throws NoMatchDimensionException {
    	if(other.dim == dim) {
	    	double prod = 0;
	    	for(int i = 0; i < dim; ++i) {
	    		prod += cmps[i]*other.cmps[i];
	    	}
	    	return prod;
    	} else {
    		throw new NoMatchDimensionException("Cannot multiply vertices of different dimension");
    	}
    }
    
    /**
     * Computes the cross product of a set of vertices.
     * @param other The Vertices to multiply.
     * @return The resulting product.
     * @throws NoMatchDimensionException 
     */
    public static Vertex cross(Vertex ... others) throws NoMatchDimensionException {
		int dim = others[0].dim;
		if(dim != others.length+1) {
			throw new NoMatchDimensionException("Dim-1 vertices are needed for the product");
		}
		Vertex result = new Vertex(dim);
		Matrix determ = new Matrix(dim);
		for(int i = 1; i < dim; ++i) {
			determ.getValues()[i] = others[i-1].cmps.clone();
		}
		for(int i = 0; i < dim; ++i) {
			result.cmps[i] = Math.pow(-1, i)*Matrix.determinant(new Matrix(dim-1, determ.getValues(), 0, i));
		}
		return result;
	}
    
    /**
     * Checks if this Vertex is orthogonal to the given one (their dot
     * product is zero with the given precision).
     * @param other The Vertex to check.
     * @param precision The precision of the operation.
     * @return True if both are orthogonal up to the given precision.
     * @throws NoMatchDimensionException 
     */
    public boolean isOrthogonalTo(Vertex other, double precision) throws NoMatchDimensionException {
    	if(precision <= 0) {
    		throw new IllegalArgumentException("Precision must be positive!");
    	}
    	double prod = dotProduct(other);
    	return prod < precision && prod > -precision;
    }
    
    public void normalize() {
    	double mod;
		try {
			mod = distanceFromOrigin();
			for(int i = 0; i < dim; ++i) {
	    		cmps[i] /= mod;
	    	}
		} catch (NoMatchDimensionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    /**
     * @return The distance from the vertex origin.
     * @throws NoMatchDimensionException if the distance is calculated to an
     * object of different dimension
     */
    public double distanceFromOrigin() throws NoMatchDimensionException {
        return distanceTo(new Vertex(dim));
    }

}
