/*     
	Ejemplo de desarrollo para ingeniería del software
    Copyright (C) 2017  Jorge J. Gómez Sanz (escalope)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    	
*/

package es.ucm.fdi;

/**
 * Ejemplo de desarrollo para Ingeniería del Software
 * @author escalope
 */
public class EjemploApp 
{
    public static void main( String[] args )
 
    {
    	
    	System.out.println("Ejemplo de desarrollo para ingeniería del software\n"+
    			"Copyright (C) 2017  Jorge J. Gómez Sanz (escalope) \n"+
    "This program comes with ABSOLUTELY NO WARRANTY; for details type `show w'.\n"+
    "This is free software, and you are welcome to redistribute it\n"+
    "under certain conditions; type `show c' for details.\n");
    	    	
        // Inicializar datos
    	
    	// Esto es un ejemplo
    	es.ucm.fdi.datos.BDMemoria<String> tablaCadena=new es.ucm.fdi.datos.BDMemoria<String>();
    	tablaCadena.insert("dato1","1");
    	tablaCadena.insert("dato2", "2");    	
    	System.out.println(tablaCadena);
    	
    	// Inicializar integración
    	
        // TODO
    	
    	// Inicializar negocio
    	
        // TODO
    	
    	// Inicializar presentacion
    	
        // TODO    	
    	
        
    }
}
