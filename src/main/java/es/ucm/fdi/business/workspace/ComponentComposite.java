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
package es.ucm.fdi.business.workspace;

import java.util.Iterator;

/**
 * Interface used to apply composite pattern in workspace design. Each component
 * behaves differently. Leaf components won't contain other components and they
 * should throw an UnsupportedOperationException when its methods are called.
 *
 * @author Arturo Acuaviva Huertos
 */
public interface ComponentComposite {

    /**
     * Adds a new ComponentComposite to the inner list of the object.
     *
     * @param component to be added
     */
    public void add(ComponentComposite component);

    /**
     * Removes a ComponentComposite of the inner list of the object.
     *
     * @param component to be removed
     */
    public void delete(ComponentComposite component);

    /**
     * Removes all ComponentComposites of the inner list.
     */
    public void deleteAll();

    /**
     * Returns an iterator over the inner list of ComponentComposite of the
     * object.
     *
     * @return iterator over the inner component composite list
     */
    public Iterator getCompositeIterator();

    /**
     * Returns a CompositeComponent at a given position by the index.
     * @param index
     * @return 
     */
    public ComponentComposite elementAt(int index);
    
}
