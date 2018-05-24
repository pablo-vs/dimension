package es.ucm.fdi.business.util;

import es.ucm.fdi.business.exceptions.NoMatchDimensionException;
import es.ucm.fdi.business.workspace.Vertex;

public class Matrix {
	private final int dim;
	private double[][] values;

	public Matrix(int dim, double[][] values) throws NoMatchDimensionException {
		if(dim <= 0) {
			throw new NoMatchDimensionException("Matrix dimension must be positive");
		}
		this.dim = dim;
		this.setValues(values.clone());
	}

	public Matrix(int dim) throws NoMatchDimensionException {
		this(dim, new double[dim][dim]);
	}

	//Matriz con las coordenadas de los vectores dados
	public Matrix(Vertex ... vecs) {
		this.dim = vecs[0].getDimension();
		setValues(new double[dim][dim]);
		for(int i = 0; i < dim; ++i) {
			getValues()[i] = vecs[i].getComps().clone();
		}
	}

	//Matriz resultante de excluir la fila exclI y la columna exclJ de los valores dados
	public Matrix(int dim, double[][] values, int exclI, int exclJ) {
		int auxi = 0, auxj;
		this.dim = dim;
		this.setValues(new double[dim][dim]);
		for(int i = 0; i < dim; ++i) {
			auxj = 0;
			if(i == exclI) auxi = 1;
			for(int j = 0; j < dim; ++j) {
				if(j == exclJ) auxj = 1;
				this.getValues()[i][j] = values[i+auxi][j+auxj];
			}
		}
	}

	//Producto por un vector (por la derecha)
	public Vertex multiply(Vertex vec) throws NoMatchDimensionException {
		Vertex result = new Vertex(dim);
		for(int i = 0; i < dim; ++i) {
			result.set(i, 0);
			for(int j = 0; j < dim; ++j) {
				result.set(i, result.at(i) + getValues()[i][j]*vec.at(j));
			}
		}
		return result;
	}

	//Producto por una matriz (por la derecha)
	public Matrix multiply(Matrix other) throws NoMatchDimensionException {
		double[][] result = new double[dim][dim];
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				for (int i = 0; i < dim; i++) {
					result[row][col] +=
						this.getValues()[row][i] * other.getValues()[i][col];
				}
			}
		}
		return new Matrix(dim, result);
	}

	//Determinante
	public static double determinant(Matrix m) {
		double result = 0;
		if(m.dim == 1) result = m.getValues()[0][0];
		else {
			for(int i = 0; i < m.dim; ++i) {
				if(m.getValues()[0][i] != 0) result += Math.pow(-1, i)*m.getValues()[0][i]*determinant(new Matrix(m.dim-1, m.getValues(), 0, i));
				
			}
		}
		return result;
	}

	//Transponer
	public Matrix transpose() throws NoMatchDimensionException {
		Matrix result = new Matrix(dim);
		for(int i = 0; i < dim; ++i) {
			for(int j = 0; j < dim; ++j) {
				result.getValues()[i][j] = this.getValues()[j][i];
			}
		}
		return result;
	}
	
	public Matrix inverse() throws NoMatchDimensionException {
		Matrix result = new Matrix(dim);
		double det = determinant(this);
		if(Math.abs(det) < 0.000001) {
			throw new IllegalArgumentException("The matrix has no inverse");
		} else if (Math.abs(det) - 1 < 0.000001) {
			return transpose();
		} else {
			for(int i = 0; i < dim; ++i) {
				for(int j = 0; j < dim; ++j) {
					result.values[i][j] = Math.pow(-1, i+j)*determinant(new Matrix(dim-1, this.values, i, j))/det;
				}
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		String res = "";
		for(int i = 0; i < dim; ++i) {
			for(int j = 0; j < dim; ++j) {
				res += getValues()[i][j] + " ";
			}
			res += "\n";
		}
		return res;
	}

	@Override
	public Matrix clone() {
		Matrix res;
		try {
			res = new Matrix(dim, getValues());
			return res;
		} catch (NoMatchDimensionException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public double[][] getValues() {
		return values;
	}
	
	public double at(int row, int col) {
		if(0 > row || row >= values.length || 0 > col || col >= values[0].length) {
			throw new IllegalArgumentException("Out of bounds");
		} else {
			return values[row][col];
		}
	}

	public void setValues(double[][] values) {
		this.values = values;
	}
}
