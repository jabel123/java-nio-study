package com.my.nio;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.DoubleStream;

public class MyPath {
    public static void main(String[] args) {
        Path dir = Paths.get("dir", "file.txt");
        Path dir2 = Paths.get("dir/file.txt");

        Iterator<Path> iterator = dir.iterator();
        while(iterator.hasNext())
        {
            Path next = iterator.next();
            System.out.println(next.getFileName());
        }

    }
}
