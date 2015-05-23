package com.company;

import java.util.ArrayList;

/**
 * Created by 1 on 21.05.2015.
 */

public class Main {
    public static void main(String[] args) {

        int N = 10000000;

        ArrayList<Integer> A = new ArrayList<Integer>();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < N; ++i)
            A.add(i);

        long timeSpent = System.currentTimeMillis() - startTime;
        System.out.println("программа выполнялась " + timeSpent + " миллисекунд");

        startTime = System.currentTimeMillis();

        HashedArrayTree<Integer> a = new HashedArrayTree<Integer>();
        for (int i = 0; i < N; ++i) {
            a.add(i);
        }

        timeSpent = System.currentTimeMillis() - startTime;

        System.out.println("программа выполнялась " + timeSpent + " миллисекунд");
    }
}
