package es.ucm.fdi.workspace.transformations;

import es.ucm.fdi.workspace.Graphics;

/**
 *  @author Brian Leiva
 *  @author Inmapg
 */
public class Contraer extends TransformarFuncion {
    
    public Contraer(int newID){
        super(newID);
    }
    
    public void contraerX(Graphics g, double d){
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
    
    public void contraerY(Graphics g, double d){
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
    
    public void contraerZ(Graphics g, double d){
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
    
    public void contraerTodo(Graphics g, double d){
        contraerX(g, d);
        contraerY(g, d);
        contraerZ(g, d);
    }
}
