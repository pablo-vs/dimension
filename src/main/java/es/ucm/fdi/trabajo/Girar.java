package es.ucm.fdi.trabajo;

/**
 *
 * @author Brian Leiva
 * @author Inmapg
 */
public class Girar extends TransformarFuncion {
    
    public Girar(int newID) {
        super(newID);
    }
    
    public void girarX(Grafico g, double d){
    	double minY = g.getImagen().get(0).at(1), maxY = minY,
    	       minZ = g.getImagen().get(0).at(2), maxZ = minZ;
        for (int i = 1; i < g.getImagen().size(); ++i){
        	double n1 = g.getImagen().get(i).at(1), n2 = g.getImagen().get(i).at(2);
        	if (n1 > maxY) maxY = n1;
        	if (n1 < minY) minY = n1;
        	if (n2 > maxZ) maxZ = n2;
        	if (n2 < minZ) minZ = n2;
        }
        double centerY = (maxY + minY) / 2, centerZ = (maxZ + minZ) / 2;
        for (int i = 0; i < g.getImagen().size(); ++i){
        	double y = g.getImagen().get(i).at(1), z = g.getImagen().get(i).at(2), dist;
        	dist = Math.sqrt(Math.pow(y - centerY, 2) + Math.pow(z - centerZ, 2));
        	g.getImagen().get(i).set(1, y + dist * Math.cos(d));
        	g.getImagen().get(i).set(2, z + dist * Math.sin(d));
        }
    }
    
    public void girarY(Grafico g, double d){
    	double minX = g.getImagen().get(0).at(0), maxX = minX,
     	       minZ = g.getImagen().get(0).at(2), maxZ = minZ;
         for (int i = 1; i < g.getImagen().size(); ++i){
         	double n1 = g.getImagen().get(i).at(0), n2 = g.getImagen().get(i).at(2);
         	if (n1 > maxX) maxX = n1;
         	if (n1 < minX) minX = n1;
         	if (n2 > maxZ) maxZ = n2;
         	if (n2 < minZ) minZ = n2;
         }
         double centerX = (maxX + minX) / 2, centerZ = (maxZ + minZ) / 2;
         for (int i = 0; i < g.getImagen().size(); ++i){
         	double x = g.getImagen().get(i).at(0), z = g.getImagen().get(i).at(2), dist;
         	dist = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));
         	g.getImagen().get(i).set(0, x + dist * Math.sin(d));
         	g.getImagen().get(i).set(2, z + dist * Math.cos(d));
         }
    }
    
    public void girarZ(Grafico g, double d){
    	double minX = g.getImagen().get(0).at(0), maxX = minX,
    		   minY = g.getImagen().get(0).at(1), maxY = minY;
         for (int i = 1; i < g.getImagen().size(); ++i){
         	double n1 = g.getImagen().get(i).at(0), n2 = g.getImagen().get(i).at(1);
         	if (n1 > maxX) maxX = n1;
         	if (n1 < minX) minX = n1;
         	if (n2 > maxY) maxY = n2;
         	if (n2 < minY) minY = n2;
         }
         double centerX = (maxX + minX) / 2, centerY = (maxY + minY) / 2;
         for (int i = 0; i < g.getImagen().size(); ++i){
         	double x = g.getImagen().get(i).at(0), y = g.getImagen().get(i).at(1), dist;
         	dist = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
         	g.getImagen().get(i).set(0, x + dist * Math.cos(d));
         	g.getImagen().get(i).set(1, y + dist * Math.sin(d));
         }
    }
    
    public void girarTodo(Grafico g, double d){
        girarX(g, d);
        girarY(g, d);
        girarZ(g, d);
    }
}
