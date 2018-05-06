/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.datos;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a database with a single table and a primary key. It
 * could be overwritten to storage different tables. A certain type must be
 * given. The object is not stored in local memory. Each object type used in the
 * table must override toString method.
 *
 * @author Arturo Acuaviva Huertos 
 * @author escalope, @jjgomezs
 *
 * @param <T> The given type for the table
 */
public class MemoryDB<T> {

    /**
     * Container collection to store the data in the DB.
     */
    private final HashMap<String, T> _table = new HashMap<>();

    /**
     * Insert an element in the table. If the element id currently exists it is
     * overwritten.
     *
     * @param newElement the new object to be inserted
     * @param id identification parameter
     */
    public void insert(T newElement, String id) {
        _table.put(id, newElement);
    }

    /**
     * Find a certain object given an id. It returns null when the value cannot
     * be found
     *
     * @param id identification parameter
     * @return the object found or null when not found
     */
    public T find(String id) {
        return _table.get(id);
    }

    /**
     * Obtains a list of the identifiers
     *
     * @return the list of identifiers
     */
    public List<String> getIds() {
        return new ArrayList<>(_table.keySet());
    }

    /**
     * Remove the object with the indicated id.
     *
     * @param id object identifier
     * @return true when the object has been correctly remove, otherwise it
     * returns false
     */
    public boolean removeId(String id) {
        if (_table.containsKey(id)) {
            _table.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Prints the table through an specified output stream. Normally the
     * outputstream will be a file or the default system output service.
     *
     * @param os the output stream
     */
    public void print(OutputStream os) {
        PrintStream ps = new java.io.PrintStream(os);
        _table.keySet().forEach((key) -> {
            ps.println(key + ":" + _table.get(key));
        });
    }

    /**
     * Returns a string representation of the data base.
     *
     * @return a string with all the values stored in the database
     */
    @Override
    public String toString() {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        print(baos);
        return baos.toString();
    }

}
