package com.my.nio.reactor;

import java.util.Arrays;

public class MyTest {
    public static void main(String[] args) {
        char array1[] = {'1', '2' , '3', '4', '5'};
        char array2[] = {'a', 'b' , 'c', 'd', 'e'};

        System.arraycopy(array1, 1, array2, 1, 3);

        System.out.println(Arrays.toString(array2));

    }
}
