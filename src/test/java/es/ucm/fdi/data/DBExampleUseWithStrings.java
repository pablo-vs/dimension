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
package es.ucm.fdi.data;

import es.ucm.fdi.business.workspace.project.ProjectDTO;
import es.ucm.fdi.data.MemoryDB;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 *
 * @author escalope
 */
public class DBExampleUseWithStrings
        extends TestCase {

    /**
     * Create the test case.
     *
     * @param testName name of the test case
     */
    public DBExampleUseWithStrings(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DBExampleUseWithStrings.class);
    }

    /**
     * Test example of DB.
     */
    public void testCreacion() {
        MemoryDB<ProjectDTO> dbProjectTOs = new MemoryDB<>();
        ProjectDTO p1 = new ProjectDTO("p1");
        ProjectDTO p2 = new ProjectDTO("p2");
        assertTrue("DB should be empty but it contains:\n" + dbProjectTOs,
                dbProjectTOs.getIds().isEmpty());
        //bdCadenas.insert("hola", "1");	
        dbProjectTOs.insert(p1, "p1");
        //bdProjectTOs.insert(p2, "p2");
        assertTrue("DB should only contain an element. Current content:\n"
                + dbProjectTOs, dbProjectTOs.getIds().size() == 1);
        dbProjectTOs.print(System.out);
        //bdCadenas.insert("hola", "1");
        assertTrue("DB should only contain an element. Current content: \n"
                + dbProjectTOs, dbProjectTOs.getIds().size() == 1);
    }

    /**
     * Another example of testing DB.
     */
    public void testDestruccion() {
        MemoryDB<String> db = new MemoryDB<>();
        assertTrue("DB should only contain an element. Current content: \n"
                + db, db.getIds().isEmpty());
        db.insert("hola", "1");
        assertTrue("DB should only contain an element. Current content: \n"
                + db, db.getIds().size() == 1);
        db.removeId("1");
        assertTrue("DB should be empty but it contains: \n" + db,
                db.getIds().isEmpty());
    }

    /**
     * Ejemplo de búsqueda
     */
    public void testBuscar() {
        MemoryDB<String> db = new MemoryDB<>();
        assertTrue("DB should be empty but it contains: \n" + db,
                db.getIds().isEmpty());
        db.insert("hey", "1");
        assertTrue("DB should only contain an element. Current content: \n"
                + db, db.getIds().size() == 1);
        assertTrue("DB should contain \"hey\" but this is not happenning. "
                + "Current content \n" + db, db.find("1") != null
                && db.find("1").equals("hey"));
    }

}
