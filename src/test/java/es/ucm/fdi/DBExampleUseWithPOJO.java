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

import es.ucm.fdi.integration.data.MemoryDB;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Example class of a Transfer Object.
 *
 * @author escalope
 */
class ExampleTO implements Serializable {

    private String field1 = "";
    private String field2 = "";

    public ExampleTO(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public String toString() {
        return "field1:" + field1 + "; field2:" + field2;
    }

    public String getFirstField() {
        return field1;
    }

    public void setFirstField(String field1) {
        this.field1 = field1;
    }

    public String getSecondField() {
        return field2;
    }

    public void setSecondField(String field2) {
        this.field2 = field2;
    }

}

/**
 * Test example for TOs.
 *
 * @author escalope
 */
public class DBExampleUseWithPOJO extends TestCase {

    /**
     * Creates the test case.
     *
     * @param testName name of the test case
     */
    public DBExampleUseWithPOJO(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested.
     */
    public static Test suite() {
        return new TestSuite(DBExampleUseWithPOJO.class);
    }

    /**
     * Test example with DB.
     */
    public void testCreacion() {
        MemoryDB<ExampleTO> db = new MemoryDB<>();
        assertTrue("DB should be empty but it contains: \n" + db, db.getIds().isEmpty());
        db.insert(new ExampleTO("1", "2"), "1");
        assertTrue("DB should only contain an element. Current content: \n"
                + db, db.getIds().size() == 1);
        db.insert(new ExampleTO("3", "4"), "1");
        assertTrue("DB should only contain an element. Current content: \n"
                + db, db.getIds().size() == 1);
        assertTrue("DB should contains the last object. Contenido actual \n"
                + db, db.find("1").getFirstField().equals("3"));
    }

    /**
     * Another test examples with DB.
     */
    public void destructionTest() {
        MemoryDB<ExampleTO> db = new MemoryDB<>();
        assertTrue("DB should be empty but it contains: \n"
                + db, db.getIds().isEmpty());
        db.insert(new ExampleTO("1", "2"), "1");
        assertTrue("DB should only contain an element. Current content: \n"
                + db, db.getIds().size() == 1);
        db.removeId("1");
        assertTrue("DB should be empty but it contains: \n"
                + db, db.getIds().isEmpty());
    }

    /**
     * Search example.
     */
    public void searchTest() {
        MemoryDB<ExampleTO> db = new MemoryDB<>();
        assertTrue("DB should be empty but it contains: \n" + db, db.getIds().isEmpty());
        db.insert(new ExampleTO("1", "2"), "1");
        assertTrue("DB should only contain an element. Current content: \n"
                + db, db.getIds().size() == 1);
        assertTrue("DB should contain the TO \"1\" but it does not. Current content: \n"
                + db, db.find("1") != null && db.find("1").getFirstField().equals("1"));
    }

}
