package com.company;

import java.util.List;

/**
 * Created by 1 on 29.05.2015.
 */
public class Benchmark<E> {
    public int N;

    public Benchmark(int n) {
        N = n;
    }

    public void run(List<E> x) {

        System.out.println(x.getClass());
        System.out.println("Добавление " + N + " элементов");


        long startTime = System.currentTimeMillis();

        for (int i = 0; i < N; ++i)
            x.add(null);

        long timeSpent = System.currentTimeMillis() - startTime;

        System.out.println("программа выполнялась " + timeSpent + " миллисекунд");

        System.out.println("Доступ к " + N + " элементам");

        startTime = System.currentTimeMillis();

        for (int i = 0; i < N; ++i)
            x.get(i);

        timeSpent = System.currentTimeMillis() - startTime;

        System.out.println("программа выполнялась " + timeSpent + " миллисекунд");

    }
}
