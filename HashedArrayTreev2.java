package com.company;

import sun.util.PreHashedMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by 1 on 21.05.2015.
 */

public class HashedArrayTree<E> implements List<E> {
    public int currentSize;
    private int capacity;
    private int sideLength;
    private int sideD2;
    private E[][] data;

    public HashedArrayTree(int n) {
        sideD2 = n;
        n = 1 << n;
        data = (E[][])new Object[n][n];
        capacity = n * n;
        sideLength = n;
        currentSize = 0;
    }

    public HashedArrayTree() {
        sideD2 = 0;
        int n = 1;
        data = (E[][])new Object[n][n];
        capacity = n * n;
        sideLength = n;
        currentSize = 0;
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
        Iterator<E> it = this.iterator();
        while(it.hasNext()) {
            if (it.next().equals(o))
                return true;
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
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[currentSize];
        Iterator<E> it = this.iterator();
        int ft = 0;
        while(it.hasNext()) {
            res[ft++] = it.next();
        }
        return res;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Iterator<E> it = this.iterator();
        int ft = 0;
        while(it.hasNext()) {
            a[ft++] = (T)it.next();
        }
        return a;
    }

    public void resize(int newSize) {
        if (newSize > capacity) {
            for (int side = sideD2 + 1; ; ++side) {
                if ((1 << (side + side)) > newSize) {
                    HashedArrayTree _t = new HashedArrayTree(side);
                    for (int i = 0; i < currentSize; ++i)
                        _t.add(this.get(i));
                    this.sideLength = _t.sideLength;
                    this.sideD2 = _t.sideD2;
                    this.capacity = _t.capacity;
                    this.currentSize = newSize;
                    this.data = (E[][]) _t.data;
                    return;
                }
            }
        } else {
            currentSize = newSize;
        }
    }

    @Override
    public boolean add(E e) {
        resize(currentSize + 1);
        set(currentSize, e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < currentSize; ++i)
            if (get(i).equals(o)) {
                for (int j = i + 1; j < currentSize; ++j)
                    set(j - 1, get(j));
                currentSize--;
                return true;
            }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<E> it = (Iterator<E>)c.iterator();
        while(it.hasNext()) {
            if (!contains(it.next()))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Iterator<E> it = (Iterator<E>) c.iterator();
        while(it.hasNext())
            add(it.next());
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        int r = currentSize;
        resize(currentSize + c.size());
        for (int i = r; i >= index; --i) {
            set(i + c.size(), get(i));
        }

        Iterator<E> it = (Iterator<E>) c.iterator();
        while(it.hasNext())
            set(index++, it.next());

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean res = false;
        Iterator<E> it = (Iterator<E>) c.iterator();
        while(it.hasNext())
            res |= remove(it.next());
        return res;
    }

    @Override
    public void clear() {
        sideD2 = 0;
        int n = 1;
        data = (E[][])new Object[n][n];
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
        resize(currentSize + 1);
        for (int i = currentSize - 1; i >= index; --i) {
            set(i + 1, get(i));
        }
        set(index, element);
    }

    @Override
    public E remove(int index) {
        E res = get(index);
        for (int i = index; i < currentSize - 1; ++i)
            set(i, get(i + 1));
        currentSize--;
        return res;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < currentSize; ++i)
            if (get(i).equals(o))
                return i;
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
