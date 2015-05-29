package com.company;

import java.util.ArrayList;

/**
 * Created by 1 on 21.05.2015.
 */

public class Main {
    public static void main(String[] args) {

        Benchmark check = new Benchmark(10000000);

        check.run(new HashedArrayTree<Integer>());
        check.run(new ArrayList<Integer>());
    }
}
