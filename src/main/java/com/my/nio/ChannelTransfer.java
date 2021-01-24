package com.my.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class ChannelTransfer {
    public static void main(String[] args) throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long positoin = 0;
        long count = fromChannel.size();
        toChannel.transferFrom(fromChannel, positoin, count);

    }
}
