package com.company;

import java.util.Iterator;

/**
 * Created by 1 on 21.05.2015.
 */

public class HashedArrayTree<E> {
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
        int n = 1;
        data = (E[][])new Object[n][n];
        capacity = n * n;
        sideLength = n;
        currentSize = 0;
    }
    public void set(int pos, E x) {
        data[pos >> sideD2][pos & (sideLength - 1)] = x;
    }
    public E get(int pos) {
        return data[pos >> sideD2][pos & (sideLength - 1)];
    }
    public void resize(int nsz) {
        if (nsz > capacity) {
            for (int side = sideD2 + 1; ; ++side) {
                if ((1 << (side + side)) > nsz) {
                    HashedArrayTree _t = new HashedArrayTree(side);
                    for (int i = 0; i < currentSize; ++i)
                        _t.add(this.get(i));
                    this.sideLength = _t.sideLength;
                    this.sideD2 = _t.sideD2;
                    this.capacity = _t.capacity;
                    this.currentSize = _t.currentSize;
                    this.data = (E[][]) _t.data;
                    return;
                }
            }
        }
    }
    public void add(E x) {
        resize(currentSize + 1);
        set(currentSize, x);
        currentSize++;
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
    public Iterator<E> iterator() {
        return new Itr();
    }
}
