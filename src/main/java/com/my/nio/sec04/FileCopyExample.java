package com.my.nio.sec04;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileCopyExample {
    public static void main(String[] args) throws IOException {
        Path from = Paths.get("img/yumi.jpg");
        Path to = Paths.get("img/yumi1.jpg");

        FileChannel fileChannelFrom = FileChannel.open(from, StandardOpenOption.READ);
        FileChannel fileChannelTo = FileChannel.open(to, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        ByteBuffer buffer = ByteBuffer.allocateDirect(100);
        int byteCount;
        while (true) {
            buffer.clear();
            byteCount = fileChannelFrom.read(buffer);
            if (byteCount == -1) break;
            buffer.flip();
            fileChannelTo.write(buffer);
        }

        fileChannelTo.close();
        fileChannelFrom.close();
    }
}
