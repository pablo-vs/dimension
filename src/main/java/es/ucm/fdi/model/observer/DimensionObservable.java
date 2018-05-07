package es.ucm.fdi.model.observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observer;

/**
 * This class represents an observable object, or "data" in the model-view
 * paradigm. It can be subclassed to represent an object that the application
 * wants to have observed. It copies the methods from Observable and adds its
 * own one to manage Dimension. New methods are added to notify specific errors.
 *
 * @see java.util.Observable
 */
public class DimensionObservable {

    private boolean changed = false;
    private final List<Observer> obs;

    /**
     * Construct an Observable with zero Observers.
     */
    public DimensionObservable() {
        obs = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Adds an observer to the set of observers for this object, provided that
     * it is not the same as some observer already in the set. The order in
     * which notifications will be delivered to multiple observers is not
     * specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    public synchronized void addObserver(Observer o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!obs.contains(o)) {
            obs.add(o);
        }
    }

    /**
     * Deletes an observer from the set of observers of this object. Passing
     * <CODE>null</CODE> to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    public synchronized void deleteObserver(Observer o) {
        obs.remove(o);
    }

    /**
     * If this object has changed, as indicated by the <code>hasChanged</code>
     * method, then notify all of its observers and then call the
     * <code>clearChanged</code> method to indicate that this object has no
     * longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other words,
     * this method is equivalent to:
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     *
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#hasChanged()
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void notifyObservers() {
        notifyObservers(null);
    }

    /**
     * If this object has changed, as indicated by the <code>hasChanged</code>
     * method, then notify all of its observers and then call the
     * <code>clearChanged</code> method to indicate that this object has no
     * longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and the <code>arg</code> argument.
     *
     * @param arg any object.
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#hasChanged()
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void notifyObservers(Object arg) {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Vector and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!changed) {
                return;
            }
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--) {
            ((DimensionObserver) arrLocal[i]).update(this, arg);
        }
    }

    /**
     * If a login occurred in Dimension all observers will be notified by this
     * method.
     * <p>
     * Each observer has its <code>updateLogin</code> method called with two
     * arguments: this observable object and the <code>username</code> argument.
     *
     * @param username
     */
    public void notifyLogin(String username) {
        Object[] arrLocal;

        synchronized (this) {
            if (!changed) {
                return;
            }
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--) {
            ((DimensionObserver) arrLocal[i]).updateLogin(this, username);
        }
    }

    /**
     * If a logout occurred in Dimension all observers will be notified by this
     * method.
     * <p>
     * Each observer has its <code>updateLogout</code> method called with two
     * arguments: this observable object and the <code>username</code> argument.
     *
     * @param username
     */
    public void notifyLogout(String username) {
        Object[] arrLocal;

        synchronized (this) {
            if (!changed) {
                return;
            }
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--) {
            ((DimensionObserver) arrLocal[i]).updateLogout(this, username);
        }
    }

    /**
     * If an exception is thrown in Dimension all observers will be notified by
     * this method.
     * <p>
     * Each observer has its <code>updateException</code> method called with two
     * arguments: this observable object and the <code>exception</code>
     * argument.
     *
     * @param e the exception thrown
     */
    public void notifyException(Exception e) {
        Object[] arrLocal;

        synchronized (this) {
            if (!changed) {
                return;
            }
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--) {
            ((DimensionObserver) arrLocal[i]).updateException(this, e);
        }
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {
        obs.removeAll(obs);
    }

    /**
     * Marks this <tt>Observable</tt> object as having been changed; the
     * <tt>hasChanged</tt> method will now return <tt>true</tt>.
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has already
     * notified all of its observers of its most recent change, so that the
     * <tt>hasChanged</tt> method will now return <tt>false</tt>. This method is
     * called automatically by the <code>notifyObservers</code> methods.
     *
     * @see java.util.Observable#notifyObservers()
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    protected synchronized void clearChanged() {
        changed = false;
    }

    /**
     * Tests if this object has changed.
     *
     * @return  <code>true</code> if and only if the <code>setChanged</code>
     * method has been called more recently than the <code>clearChanged</code>
     * method on this object; <code>false</code> otherwise.
     * @see java.util.Observable#clearChanged()
     * @see java.util.Observable#setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Returns the number of observers of this <tt>Observable</tt> object.
     *
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return obs.size();
    }

}
