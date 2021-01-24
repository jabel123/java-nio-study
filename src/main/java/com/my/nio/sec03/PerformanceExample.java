package com.my.nio.sec03;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class PerformanceExample {
    public static void main(String[] args) throws IOException {
        Path from = Paths.get("img/ayumi.jpg");
        Path to1 = Paths.get("img/ayumi2.jpg");
        Path to2 = Paths.get("img/ayumi3.jpg");

        long size = Files.size(from);

        FileChannel fileChannelFrom = FileChannel.open(from);
        FileChannel fileChannelTo1 = FileChannel.open(to1, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));
        FileChannel fileChannelTo2 = FileChannel.open(to2, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE));

        ByteBuffer nonDirectBuffer = ByteBuffer.allocate((int) size);
        ByteBuffer directBuffer = ByteBuffer.allocateDirect((int) size);

        long start, end;

        start = System.nanoTime();
        for (int i=0; i<100; i++)
        {
            fileChannelFrom.read(nonDirectBuffer);
            nonDirectBuffer.flip();
            fileChannelTo1.write(nonDirectBuffer);
            nonDirectBuffer.clear();
        }
        end = System.nanoTime();
        System.out.println("넌다이렉트:\t" + (end-start)/10 + " ns");

        fileChannelFrom.position(0);

        start = System.nanoTime();
        for (int i=0; i<100; i++)
        {
            fileChannelFrom.read(directBuffer);
            directBuffer.flip();
            fileChannelTo1.write(directBuffer);
            directBuffer.clear();
        }
        end = System.nanoTime();
        System.out.println("다이렉트:\t" + (end-start)/10 + " ns");

        fileChannelFrom.close();
        fileChannelTo1.close();
        fileChannelTo2.close();
    }
}
