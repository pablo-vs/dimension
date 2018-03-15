import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Vector;
import java.lang.Integer;

public class Prueba3D {

	public static VertexN [] generateHypercube(int dim, double length) {
		//Genera un hipercubo de la dimensión y lado dadas centrado en el origen.
		//Devuelve la lista de puntos que lo forman (hc)
		//Cada punto tiene una lista de coordenadas que se van almacenando en comps
		int numV = (int) Math.pow(2, dim), j;
		VertexN [] hc = new VertexN[numV];
		double [] comps = new double[dim];
		double semiSide = length/2;
		boolean found;
		
		for(int i = 0; i < dim; ++i) comps[i] = -semiSide;
		//Empieza con el punto cuyas coordenadas son todas -1*lado/2
		
		for(int i = 0; i < numV; ++i) {
			hc[i] = new VertexN(dim, comps);
			j = dim-1;

			//Esto transforma las coordenadas que hay en comps al "siguiente" punto
			//Como sumar 1 a un número binario
			found = false;
			while(!found && j >= 0) {
				if(comps[j] == -semiSide) {
					found = true;
					comps[j] = semiSide;
					for(int k = j+1; k < dim; ++k) comps[k] = -semiSide;
				}
				--j;
			}
		}
		return hc;
	}

	
	static VertexN [][] generateParams(int dim, double d, double s) {
		//Genera parámetros de visualización:
		//Una proyección de N a N-1 dim requiere un punto de vista,
		//un punto hacia el que mirar, y puntos adicionales para formar una base
		//del espacio N-1 sobre el que se proyecta. Un array de puntos
		
		//Esto genera esos parámetros para cada proyección desde dim a 3, d es la distancia
		//del punto de vista al origen en cada una de ellas, s es
		//el módulo de los vectores de la base.
		
		VertexN [][] res = new VertexN[dim-2][];
		for(int i = dim-3; i >= 0; --i) {
			res[i] = new VertexN[i+4];
			res[i][0] = new VertexN(i+3);
			res[i][0].components[0] = d;
			res[i][1] = new VertexN(i+3);
			res[i][1].components[0] = d-s;
			for(int j = 1; j < i+3; ++j) {
				res[i][j+1] = new VertexN(i+3);
				res[i][j+1].components[0] = d;
				res[i][j+1].components[j] = s;
			}
		}
		return res;
	}

	static VertexN[] projection3(int dim, VertexN[] obj, double dist, double viewScale) {
		//Realiza todas las proyecciones desde dimension dim hasta dimension 3
		//del conjunto de puntos obj, dist es la distancia desde la que se observa, viewScale
		//la escala del "observador" (módulo de los vectores de la base sobre la que se proyecta)

		//Básicamente llama a VertexN.project() muchas veces
		
		VertexN[][] params = generateParams(dim,dist,viewScale);
		VertexN[] projections = obj.clone();
		for(int i = dim; i > 3; --i) {
			for(int j = 0; j < obj.length; ++j) {
				projections[j] = projections[j].project(params[i-3]);
				//System.out.println(projections[j].tostring());
			}
		}
		return projections;
	}

	static long comb(int n, int r) {
		//Números combinatorios, hacen falta para calcular el número de rotaciones posibles en
		//dimensiones arbitrarias
		long res = 1;
		for(int i = n-r+1; i <= n; ++i) {
			res *= i;
		}
		for(int i = 1; i <= n-r; ++i) {
			res /= i;
		}
		return res;
	}
	
	public static void main(String[] args) {
		//Genera un hipercubo de dimensión dim y lado 1 y lo proyecta a 3D, luego a 2D
		//para visualizarlo. Cada proyección disminuye el tamaño aparente del objeto, por
		//eso hace falta un factor SCALE para la proyección final a 2D que aumente el tamaño
		
		//params son los parámetros para la proyección final a 2D, desde el punto (2,0,0) mirando hacia (1,0,0)
		int DIM = 10, SCALE = 64000;
		VertexN[][] params = generateParams(3,2,1);

		//Projections serán los puntos que se muestren por pantalla,
		//rotate lo uso para almacenar resultados intermedios de rotaciones
		
		VertexN[] hc = generateHypercube(DIM, 1), rotate = new VertexN[hc.length],
			projections = new VertexN[hc.length];
	       		
		//Magia gráfica
		
		JFrame frame = new JFrame();
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());

		// Slider para la rotacion horizontal de la proyeccion 3D
		JSlider headingSlider = new JSlider(0, 360, 180);
		pane.add(headingSlider, BorderLayout.SOUTH);

		// Rotacion vertical
		JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
		pane.add(pitchSlider, BorderLayout.EAST);

		//Aquí genero todos los pares posibles de coordenadas que puedo rotar en N dimensiones
		//representadas por un número de dos dígitos (cada uno una coordenada)
		//A partir de dimension 10 se rompe porque harían falta más dígitos
	        Vector<String> rotations = new Vector<String>();

		for(int i = 0; i < DIM; ++i) {
			for(int j = i; j < DIM; ++j) {
				rotations.add(new Integer(i).toString() + new Integer(j).toString());
			}
		}

		//Uso un slider para rotar en estas dimensiones, el ComboBox permite seleccionar
		//que coordenadas quieres rotar
		JComboBox rotList = new JComboBox(rotations.toArray());
		rotList.setSelectedIndex(0);
		pane.add(rotList, BorderLayout.NORTH);

		//El slider genérico
		JSlider genericSlider = new JSlider(SwingConstants.VERTICAL, 0, 360, 90);
		pane.add(genericSlider, BorderLayout.WEST);

		//Estas matrices representarán las rotaciones en cada par de coordenadas
		MatrixN [] rots = new MatrixN[rotations.size()];
		//La identidad, para inicializar
		MatrixN ident = new MatrixN(DIM);
		for(int i = 0; i < DIM; ++i) ident.values[i][i] = 1;

		//Inicialmente todas las rotaciones son identidades
		for(int i = 0; i < rots.length; ++i) rots[i] = ident.clone();

		
		//Dentro de la función paintComponent meto el código para dibujar, se ejecuta
		//cada vez que se repinta la interfaz
		JPanel renderPanel = new JPanel() {
				public void paintComponent(Graphics g) {

					//Fondo negro
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.BLACK);
					g2.fillRect(0, 0, getWidth(), getHeight());

					
					//Estas dos matrices manejan las rotaciones de la proyeccion 3D
					//Se combinan en una sola matriz transform
					double heading = Math.toRadians(headingSlider.getValue());
				        MatrixN headingTransform = new MatrixN(3, new double[][] {
							{Math.cos(heading), 0, Math.sin(heading)},
							{0, 1, 0},
							{-Math.sin(heading), 0, Math.cos(heading)}
						});
					double pitch = Math.toRadians(pitchSlider.getValue());
					MatrixN pitchTransform = new MatrixN(3, new double[][] {
							{1, 0, 0},
							{0, Math.cos(pitch), Math.sin(pitch)},
							 {0, -Math.sin(pitch), Math.cos(pitch)}
						});
					MatrixN transform = headingTransform.multiply(pitchTransform);

					//Construye una matriz para representar la rotacion en N dimensiones
					//que hay especificada, currentRot
					double angle = Math.toRadians(genericSlider.getValue());
				        MatrixN currentRot = new MatrixN(DIM);
					int x = ((String)rotList.getSelectedItem()).charAt(0)-'0', y =((String) rotList.getSelectedItem()).charAt(1)-'0';
					int index = 0;
					for(int i = 0; i < x; ++i) index += DIM-1-i;
					index += y;
					
					for(int i = 0; i < DIM; ++i) {
						for(int j = 0; j < DIM; ++j) {
							if((i == x && j == x) || (i == y && j == y)) {
								currentRot.values[i][j] = Math.cos(angle);
							}
							else if (i == x && j == y) {
								currentRot.values[i][j] = Math.sin(angle);
							}
							else if (i == y && j == x) {
								currentRot.values[i][j] = -Math.sin(angle);
							}
							else if (i == j) currentRot.values[i][j] = 1;
						}
					}

					//Una vez tengo currentRot, la meto en rots[] y combino todo el vector
					//de matrices para obtener una sola matriz que representa la rotacion total, allRot

					rots[index] = currentRot.clone();
					MatrixN allRot = ident.clone();
					for(int i = 0; i < rots.length; ++i) {
						allRot = allRot.multiply(rots[i]);
					}

					//hcTransformed almacena los puntos en N dim. rotados por allRot
					VertexN [] hcTransformed = new VertexN[hc.length];
					for(int i = 0; i < hc.length; ++i) {
						hcTransformed[i] = allRot.multiply(hc[i]);
					}

					//Proyecto hcTransformed a 3D en proj
					VertexN[] proj = projection3(DIM, hcTransformed, 2, 1);

					//Ahora roto proj con la rotacion 3D especificada
					//y proyecto a 2D sobre projections
					for(int i = 0; i < projections.length; ++i) {
						rotate[i] = transform.multiply(proj[i]);
						projections[i] = rotate[i].project(params[0]).multiply(SCALE);
					}

					//Color a blanco para el objeto
					g2.translate(getWidth() / 2, getHeight() / 2);
					g2.setColor(Color.WHITE);

					
					//Dibuja el hipercubo
					for(int i = 0; i < projections.length; ++i) {
						Path2D path = new Path2D.Double();

						//El path empieza en el i-esimo punto del cubo, y se conecta con todos
						//los puntos posteriores cuya distancia a él sea el lado del hipercubo
						//original (está puesto a 1, hay que cambiarlo si cambias el lado del HC)
						//Es super ineficiente hacerlo así
						path.moveTo(projections[i].components[0], projections[i].components[1]);
						for(int j = i+1; j < projections.length; ++j) {
							path.moveTo(projections[i].components[0], projections[i].components[1]);
							if(hc[j].substract(hc[i]).modulus() == 1) {
								
								path.lineTo(projections[j].components[0], projections[j].components[1]);
							}
						}

						//Dibuja el camino trazado
						path.closePath();
						g2.draw(path);
						
					}
				
					
				}
			};

		pane.add(renderPanel, BorderLayout.CENTER);

		//Con esto se vuelve a pintar cuando mueves un slider
		headingSlider.addChangeListener(e -> renderPanel.repaint());
		pitchSlider.addChangeListener(e -> renderPanel.repaint());
		genericSlider.addChangeListener(e -> renderPanel.repaint());
		
		//Con esto cuando cambias la rotación en N dim. se pone el slider en el medio
		rotList.addActionListener(e -> {genericSlider.setValue(180);});
		
		//Tamaño de la ventana
		frame.setSize(500, 500);
		frame.setVisible(true);
	}

}


//Clases auxiliares sin encapsulación, todo public y sin comprobar salidas de arrays, etc
//Hay que tirarlo y hacerlo de cero

class VertexN {
	int dim;
	double [] components;

	VertexN(int dim, double [] comps) {
		this.dim = dim;
		this.components = comps.clone();
	}

	VertexN(int dim) {
		this.dim = dim;
		this.components = new double[dim];
	}

	//Resta de vectores
	VertexN substract(VertexN other) {
		VertexN result = new VertexN(dim);
		for(int i = 0; i < dim; ++i) {
			result.components[i] = components[i]-other.components[i];
		}
		return result;
	}

	//Producto de vector por número
	VertexN multiply(double d) {
		VertexN res = new VertexN(dim);
		for(int i = 0; i < dim; ++i) res.components[i] = d*components[i];
		return res;
	}

	//Producto vectorial en N dim. Requiere N-1 vectores y produce uno ortogonal a ellos
	//Esta implementacion es super ineficiente y calcula un monton de determinantes varias veces
	static VertexN cross(VertexN [] others) {
		int dim = others[0].dim;
		VertexN result = new VertexN(dim);
		MatrixN determ = new MatrixN(dim);
		for(int i = 1; i < dim; ++i) {
			determ.values[i] = others[i].components.clone();
		}
		for(int i = 0; i < dim; ++i) {
			determ.values[0][i] = 1;
			
			result.components[i] = MatrixN.determinant(determ);
			determ.values[0][i] = 0;
		}
		return result;
	}

	//Proyecta a una dimension menos con los parametros dados
	VertexN project(VertexN [] params) {
		/*Suponemos que todos los vectores parámetro son unitarios
		quedando un ángulo de visión de 90º. Lo primero es obtener
		la matriz de cambio de base. Como ambas bases son ortonormales
		basta con trasponer las coordenadas de params.*/

		//Obtener la base a partir de los parametros
		VertexN [] vectorBase = new VertexN[dim];
		for(int i = 0; i < dim; ++i) {
			vectorBase[i] = params[i+1].substract(params[0]);
		}

		//Obtener la matriz de cambio de base, las coordenadas del punto en
		//el nuevo sistema de referencia y proyectar
		MatrixN baseChange = new MatrixN(vectorBase).transpose();
		VertexN newPoint = baseChange.multiply(this.substract(params[0]));
		VertexN projection = new VertexN(dim-1);
		for(int i = 0; i < projection.dim; ++i) {
			projection.components[i] = newPoint.components[i+1]/newPoint.components[0];
		}
		return projection;
	}

	//Modulo del vector
	double modulus() {
		double res = 0;
		for(int i = 0; i < dim; ++i) {
			res += Math.pow(components[i], 2);
		}
		return Math.sqrt(res);
	}

	//No me dejaba redefinir toString() ?
	String tostring() {
		String res = "";
		for(int i = 0; i < dim; ++i) {
			res += components[i] + " ";
		}
		res += "\n";
		return res;
	}
}

class MatrixN {
	int dim;
	double[][] values;

	MatrixN(int dim, double[][] values) {
		this.dim = dim;
		this.values = values.clone();
	}

	MatrixN(int dim) {
		this.dim = dim;
		this.values = new double[dim][dim];
	}

	//Matriz con las coordenadas de los vectores dados
	MatrixN(VertexN [] vecs) {
		this.dim = vecs[0].dim;
		values = new double[dim][dim];
		for(int i = 0; i < dim; ++i) {
			values[i] = vecs[i].components.clone();
		}
	}

	//Matriz resultante de excluir la fila exclI y la columna exclJ de los valores dados
	MatrixN(int dim, double[][] values, int exclI, int exclJ) {
		int auxi = 0, auxj;
		this.dim = dim;
		this.values = new double[dim][dim];
		for(int i = 0; i < dim; ++i) {
			auxj = 0;
			if(i == exclI) auxi = 1;
			for(int j = 0; j < dim; ++j) {
				if(j == exclJ) auxj = 1;
				this.values[i][j] = values[i+auxi][j+auxj];
			}
		}
	}

	//Producto por un vector (por la derecha)
	VertexN multiply(VertexN vec) {
		VertexN result = new VertexN(dim);
		for(int i = 0; i < dim; ++i) {
			result.components[i] = 0;
			for(int j = 0; j < dim; ++j) {
				result.components[i] += values[i][j]*vec.components[j];
			}
		}
		return result;
	}

	//Producto por una matriz (por la derecha)
	MatrixN multiply(MatrixN other) {
		double[][] result = new double[dim][dim];
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				for (int i = 0; i < dim; i++) {
					result[row][col] +=
						this.values[row][i] * other.values[i][col];
				}
			}
		}
		return new MatrixN(dim, result);
	}

	//Determinante
	static double determinant(MatrixN m) {
		double result = 0;
		if(m.dim == 1) result = m.values[0][0];
		else {
			for(int i = 0; i < m.dim; ++i) {
				if(m.values[0][i] != 0) result += Math.pow(-1, i)*m.values[0][i]*determinant(new MatrixN(m.dim-1, m.values, 0, i));
				
			}
		}
		return result;
	}

	//Transponer
	MatrixN transpose() {
		MatrixN result = new MatrixN(dim);
		for(int i = 0; i < dim; ++i) {
			for(int j = 0; j < dim; ++j) {
				result.values[i][j] = this.values[j][i];
			}
		}
		return result;
	}
	
	String tostring() {
		String res = "";
		for(int i = 0; i < dim; ++i) {
			for(int j = 0; j < dim; ++j) {
				res += values[i][j] + " ";
			}
			res += "\n";
		}
		return res;
	}

	public MatrixN clone() {
		MatrixN res;
		res = new MatrixN(dim, values);
		return res;
	}
}
