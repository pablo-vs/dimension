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

import java.io.Serializable;

import es.ucm.fdi.datos.BDMemoria;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Clase de ejemplo de Transfer Object
 * @author escalope
 *
 */
class EjemploTO implements Serializable{
	private String campo1="";
	private String campo2="";
	
	public EjemploTO(String campo1,String campo2){
		this.campo1=campo1;
		this.campo2=campo2;
	}
	
	public String toString(){
		return "campo1:"+campo1+"; campo2:"+campo2;
	}
	
	public String getCampo1() {
		return campo1;
	}
	public void setCampo1(String campo1) {
		this.campo1 = campo1;
	}
	public String getCampo2() {
		return campo2;
	}
	public void setCampo2(String campo2) {
		this.campo2 = campo2;
	}
	
	
}

/** 
 * Ejemplo de pruebas para TOs
 * 
 * @author escalope
 *
 */
public class EjemplosUsoBDConPOJO 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EjemplosUsoBDConPOJO( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( EjemplosUsoBDConPOJO.class );
    }
    

    /**
     * Ejemplo de prueba con la BD
     */
    public void testCreacion()
    {    	
    	BDMemoria<EjemploTO> bdCadenas=new BDMemoria<EjemploTO>();
    	assertTrue("La BD debía estar vacía y tiene elementos.Contenido actual \n"+bdCadenas, bdCadenas.getIds().isEmpty());
    	bdCadenas.insert(new EjemploTO("1","2"), "1");				
    	assertTrue("La BD debe tener sólo un elemento.Contenido actual \n"+bdCadenas, bdCadenas.getIds().size()==1);    	
    	bdCadenas.insert(new EjemploTO("3","4"), "1");		
    	assertTrue("La BD debe tener sólo un elemento.Contenido actual \n"+bdCadenas, bdCadenas.getIds().size()==1);
    	assertTrue("La BD debe tener el último objeto. Contenido actual \n"+bdCadenas, bdCadenas.find("1").getCampo1().equals("3"));
    }
    
    /**
     * Otro ejemplo de prueba con la BD
     */
    public void testDestruccion()
    {
    	BDMemoria<EjemploTO> bdCadenas=new BDMemoria<EjemploTO>();
    	assertTrue("La BD debía estar vacía y tiene elementos.Contenido actual \n"+bdCadenas, bdCadenas.getIds().isEmpty());
    	bdCadenas.insert(new EjemploTO("1","2"), "1");		
    	assertTrue("La BD debe tener sólo un elemento.Contenido actual \n"+bdCadenas, bdCadenas.getIds().size()==1);
    	bdCadenas.removeId("1");
    	assertTrue("La BD debía estar vacía y tiene elementos.Contenido actual \n"+bdCadenas, bdCadenas.getIds().isEmpty());    	
    }
    
    /**
     * Ejemplo de búsqueda
     */
    public void testBuscar()
    {
    	BDMemoria<EjemploTO> bdCadenas=new BDMemoria<EjemploTO>();
    	assertTrue("La BD debía estar vacía y tiene elementos.Contenido actual \n"+bdCadenas, bdCadenas.getIds().isEmpty());
    	bdCadenas.insert(new EjemploTO("1","2"), "1");				
    	assertTrue("La BD debe tener sólo un elemento. Contenido actual \n"+bdCadenas, bdCadenas.getIds().size()==1);
    	assertTrue("La BD debía contener el TO \"1\" y no está. Contenido actual \n"+bdCadenas, bdCadenas.find("1")!=null && bdCadenas.find("1").getCampo1().equals("1"));    	    
    }
    
    
    
    
}
