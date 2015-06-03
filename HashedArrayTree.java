package com.company;

import java.util.*;

/**
 * Created by 1 on 21.05.2015.
 */

public class HashedArrayTree<E> extends AbstractList<E>
implements List<E> {
    private int currentSize;
    private int capacity;
    private int sideLength;
    private int sideD2;
    private E[][] data;

    public HashedArrayTree(int cap) {
        sideD2 = 0;
        while ((1 << (2 * sideD2)) < cap)
            sideD2++;
        int n = 1 << sideD2;
        //noinspection unchecked
        data = (E[][]) new Object[n][];
        capacity = n * n;
        sideLength = n;
        currentSize = 0;
    }

    public HashedArrayTree() {
        this(1);
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (E e : this) {
                if (e == null)
                    return true;
            }
            return false;
        } else {
            for (E e : this) {
                if (e.equals(o))
                    return true;
            }
        }
        return false;
    }

    private class Itr implements Iterator<E> {
        int cursor;

        public boolean hasNext() {
            return cursor != currentSize;
        }

        Itr() {
            cursor = 0;
        }

        public E next() {
            E res = data[cursor >> sideD2][cursor & (sideLength - 1)];
            cursor++;
            return res;
        }

        public void remove() {

        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[currentSize];
        int ft = 0;
        for (E e : this) {
            res[ft++] = e;
        }
        return res;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Iterator<E> it = this.iterator();
        int ft = 0;
        for (E e : this) {
            a[ft++] = (T)e;
        }
        return a;
    }

    private void addLeaf() {
        data[currentSize >> sideD2] = (E[])new Object[sideLength];
    }

    public void resize() {
        HashedArrayTree<E> t = new HashedArrayTree<E>(capacity * 4);
        for (int i = 0; i < sideLength; i += 2) {
            t.addLeaf();
            System.arraycopy(data[i], 0, t.data[i >> 1], 0, sideLength);
            t.currentSize += sideLength;
            data[i] = null;
            if ((i + 1) * sideLength < currentSize) {
                System.arraycopy(data[i + 1], 0, t.data[i >> 1], sideLength, sideLength);
                data[i + 1] = null;
                t.currentSize += sideLength;
            }
        }
        this.data = t.data;
        this.capacity = t.capacity;
        this.sideD2 = t.sideD2;
        this.sideLength = t.sideLength;
    }

    @Override
    public boolean add(E e) {
        if ((currentSize & (sideLength - 1)) == 0) {
            if (currentSize != capacity) {
                addLeaf();
            } else {
                resize();
                if ((currentSize & (sideLength - 1)) == 0)
                    addLeaf();
            }
        }
        data[(currentSize) >> sideD2][(currentSize) & (sideLength - 1)] = e;
        currentSize++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int idx = 0;
        if (o == null) {
            for (E e : this) {
                if (e == null) {
                    remove(idx);
                    return true;
                }
                idx++;
            }
        }   else {
            for (E e : this) {
                if (e.equals(o)) {
                    remove(idx);
                    return true;
                }
                idx++;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<E> it = (Iterator<E>) c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next()))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Iterator<E> it = (Iterator<E>) c.iterator();
        while (it.hasNext())
            add(it.next());
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        Iterator<E> it = (Iterator<E>) c.iterator();
        while (it.hasNext())
            res |= remove(it.next());
        return res;
    }

    @Override
    public void clear() {
        sideD2 = 0;
        int n = 1;
        data = (E[][]) new Object[n][n];
        capacity = n * n;
        sideLength = n;
        currentSize = 0;
    }

    @Override
    public E get(int index) {
        return data[index >> sideD2][index & (sideLength - 1)];
    }

    @Override
    public E set(int index, E element) {
        E res = get(index);
        data[index >> sideD2][index & (sideLength - 1)] = element;
        return res;
    }

    @Override
    public void add(int index, E element) {
        return;
    }

    @Override
    public E remove(int index) {
        E oldValue = get(index);

        int numMoved = currentSize - index - 1;
        if (numMoved > 0) {
            int x = index >> sideD2;
            int y = index & (sideLength - 1);
            while(true) {
                numMoved = sideLength - y - 1;
                System.arraycopy(data[x], y + 1, data[x], index, numMoved);
                x++;
                y = 0;
                if ((x << sideD2) < currentSize) {
                    data[x - 1][sideLength - 1] = data[x][0];
                } else {
                    break;
                }
            }
        }
        set(currentSize--, null);

        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        int pos = 0;
        if (o == null) {
            for (E e : this) {
                if (e == null)
                    return pos;
                pos++;
            }
        }   else {
           for (E e : this) {
               if (e.equals(o))
                   return pos;
               pos++;
           }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = currentSize - 1; i >= 0; --i)
            if (get(i).equals(o))
                return i;
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            cursor = index;
        }

        public void remove() {
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public E previous() {
            int i = cursor - 1;
            E res = get(i);
            cursor = i;
            return res;
        }

        public void set(E e) {
            HashedArrayTree.this.set(cursor, e);
        }

        public void add(E e) {
            HashedArrayTree.this.add(cursor, e);
        }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        List<E> res = new HashedArrayTree<E>();
        for (int i = fromIndex; i < toIndex; ++i)
            res.add(get(i));
        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return true;
    }
}
