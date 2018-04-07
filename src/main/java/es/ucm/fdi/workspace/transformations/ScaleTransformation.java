package es.ucm.fdi.workspace.transformations;

import es.ucm.fdi.workspace.GraphBO;

/**
 *  @author Brian Leiva
 *  @author Inmapg
 */
public class ScaleTransformation implements GraphTransformationBO {

	private double x,y,z;

	public ScaleTransformation(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void apply(GraphBO g) {
		contraerX(g, x);
		contraerY(g, y);
		contraerZ(g, z);
	}
	
	private static void contraerX(GraphBO g, double d){
		double min = g.getImagen().get(0).at(0), max = min;
		for (int i = 1; i < g.getImagen().size(); ++i){
			double n = g.getImagen().get(i).at(0);
			if (n > max) max = n;
			if (n < min) min = n;
		}
		double newMin = (max * (d - 1) + min * (d + 1) / 2 * d);
		for (int i = 0; i < g.getImagen().size(); ++i){
			g.getImagen().get(i).set(0, ((g.getImagen().get(i).at(0) - min) / d) + newMin);
		}
	}
    
	private static void contraerY(GraphBO g, double d){
		double min = g.getImagen().get(0).at(1), max = min;
		for (int i = 1; i < g.getImagen().size(); ++i){
			double n = g.getImagen().get(i).at(1);
			if (n > max) max = n;
			if (n < min) min = n;
		}
		double newMin = (max * (d - 1) + min * (d + 1) / 2 * d);
		for (int i = 0; i < g.getImagen().size(); ++i){
			g.getImagen().get(i).set(1, ((g.getImagen().get(i).at(1) - min) / d) + newMin);
		}
	}
    
	private static void contraerZ(GraphBO g, double d){
		double min = g.getImagen().get(0).at(2), max = min;
		for (int i = 1; i < g.getImagen().size(); ++i){
			double n = g.getImagen().get(i).at(2);
			if (n > max) max = n;
			if (n < min) min = n;
		}
		double newMin = (max * (d - 1) + min * (d + 1) / 2 * d);
		for (int i = 0; i < g.getImagen().size(); ++i){
			g.getImagen().get(i).set(2, ((g.getImagen().get(i).at(2) - min) / d) + newMin);
		}
	}
}
