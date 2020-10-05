package javaxt.utils;
import java.util.Iterator;
import java.util.NoSuchElementException;

//******************************************************************************
//**  Generator
//******************************************************************************
/**
 *   A custom iterator that yields its values one at a time. This is a great
 *   alternative to arrays or lists when dealing with large data. By yielding
 *   one entry at a time, this iterator can help avoid out of memory exceptions.
 *
 *   Subclasses must define a method called {@link #run()} and may call
 *   {@link yield(T)} to return values one at a time.
 *
 *   @author Adrian Kuhn &lt;akuhn(at)iam.unibe.ch&gt;
 *
 ******************************************************************************/

public abstract class Generator<T> implements Iterable<T> {

    public abstract void run();

    public Iterator<T> iterator() {
        return new Iter();
    }

    private static final Object DONE = new Object();
    private static final Object EMPTY = new Object();
    private Object drop = EMPTY;
    private Thread th = null;

    private synchronized Object take() {
        while (drop == EMPTY) {
            try {
                    wait();
            } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
            }
        }
        Object temp = drop;
        if (drop != DONE)
                drop = EMPTY;
        notifyAll();
        return temp;
    }

    private synchronized void put(Object value) {
        if (drop == DONE)
                throw new IllegalStateException();
        if (drop != EMPTY)
                throw new IllegalStateException();
        drop = value;
        notifyAll();
        while (drop != EMPTY) {
            try {
                    wait();
            } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
            }
        }
    }

    protected void yield(T value) {
            put(value);
    }

    public synchronized void done() {
        if (drop == DONE)
                throw new IllegalStateException();
        if (drop != EMPTY)
                throw new IllegalStateException();
        drop = DONE;
        notifyAll();
    }

    private class Iter implements Iterator<T>, Runnable {

        private Object next = EMPTY;

        public Iter() {
            if (th != null)
                    throw new IllegalStateException("Can not run coroutine twice");
            th = new Thread(this);
            th.setDaemon(true);
            th.start();
        }

        public void run() {
            Generator.this.run();
            done();
        }

        public boolean hasNext() {
                if (next == EMPTY)
                        next = take();
                return next != DONE;
        }

        @SuppressWarnings("unchecked")
        public T next() {
                if (next == EMPTY)
                        next = take();
                if (next == DONE)
                        throw new NoSuchElementException();
                Object temp = next;
                next = EMPTY;
                return (T) temp;
        }

        public void remove() {
                throw new UnsupportedOperationException();

        }

        @SuppressWarnings("deprecation")
        @Override
        protected void finalize() throws Throwable {
                th.stop(); // let's commit suicide
        }

    }

}