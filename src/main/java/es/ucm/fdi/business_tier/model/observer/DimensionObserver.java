package es.ucm.fdi.business_tier.model.observer;

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
