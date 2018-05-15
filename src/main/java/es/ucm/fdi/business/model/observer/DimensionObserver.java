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
package es.ucm.fdi.business.model.observer;

/**
 * A class can implement the <code>DimensionObserver</code> interface when it
 * wants to be informed of changes in DimensionObservable. It copies the code
 * from Observer and adds some new methods to manage new notifications added to
 * a Dimension Observable object.
 *
 * @see java.util.Observable
 */
public interface DimensionObserver {

    /**
     * This method is called whenever a Dimension Observable is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's observers
     * notified of the change.
     *
     * @param o the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code> method.
     */
    void update(DimensionObservable o, Object arg);

    /**
     * This method is called when a login is done
     *
     * @param o the observable object.
     * @param username
     */
    void updateLogin(DimensionObservable o, String username);

    /**
     * This method is called when a logout is done
     *
     * @param o the observable object.
     * @param username
     */
    void updateLogout(DimensionObservable o, String username);

    /**
     * This method is called whenever an exception is thrown and cannot be
     * caught by the controller.
     *
     * @param o the observable object.
     * @param e
     */
    void updateException(DimensionObservable o, Exception e);

}
