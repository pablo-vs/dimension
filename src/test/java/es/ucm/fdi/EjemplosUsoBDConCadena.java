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


import es.ucm.fdi.workspace.project.ProjectTO;
import es.ucm.fdi.data.MemoryDB;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for simple App.
 */
public class EjemplosUsoBDConCadena 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EjemplosUsoBDConCadena( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( EjemplosUsoBDConCadena.class );
    }
    

    /**
     * Ejemplo de prueba con la BD
     */
    public void testCreacion()
    {    	
    	MemoryDB<ProjectTO> bdProjectTOs=new MemoryDB<ProjectTO>();
    	ProjectTO p1 = new ProjectTO("p1");
    	ProjectTO p2 = new ProjectTO("p2");
    	assertTrue("La BD debía estar vacía y tiene elementos.Contenido actual \n"+bdProjectTOs, bdProjectTOs.getIds().isEmpty());
    	//bdCadenas.insert("hola", "1");	
    	bdProjectTOs.insert(p1, "p1");
    	//bdProjectTOs.insert(p2, "p2");
    	assertTrue("La BD debe tener sólo un elemento.Contenido actual \n"+bdProjectTOs, bdProjectTOs.getIds().size()==1);
    	bdProjectTOs.print(System.out);
    	//bdCadenas.insert("hola", "1");
    	assertTrue("La BD debe tener sólo un elemento.Contenido actual \n"+bdProjectTOs, bdProjectTOs.getIds().size()==1);
    }
    
    /**
     * Otro ejemplo de prueba con la BD
     */
    public void testDestruccion()
    {
    	MemoryDB<String> bdCadenas=new MemoryDB<String>();
    	assertTrue("La BD debía estar vacía y tiene elementos.Contenido actual \n"+bdCadenas, bdCadenas.getIds().isEmpty());
    	bdCadenas.insert("hola", "1");		
    	assertTrue("La BD debe tener sólo un elemento.Contenido actual \n"+bdCadenas, bdCadenas.getIds().size()==1);
    	bdCadenas.removeId("1");
    	assertTrue("La BD debía estar vacía y tiene elementos.Contenido actual \n"+bdCadenas, bdCadenas.getIds().isEmpty());    	
    }
    
    /**
     * Ejemplo de búsqueda
     */
    public void testBuscar()
    {
    	MemoryDB<String> bdCadenas=new MemoryDB<String>();
    	assertTrue("La BD debía estar vacía y tiene elementos.Contenido actual \n"+bdCadenas, bdCadenas.getIds().isEmpty());
    	bdCadenas.insert("hola", "1");		
    	assertTrue("La BD debe tener sólo un elemento. Contenido actual \n"+bdCadenas, bdCadenas.getIds().size()==1);    	
    	assertTrue("La BD debía contener la cadena \"hola\" y no está. Contenido actual \n"+bdCadenas, bdCadenas.find("1")!=null && bdCadenas.find("1").equals("hola"));    	
    }
    
    
    
    
}
