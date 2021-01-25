package com.my.nio.sec04;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileChannelReadExample {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("C:/Temp/file.txt");
        FileChannel readChannel = FileChannel.open(path, StandardOpenOption.READ);

        ByteBuffer byteBuffer = ByteBuffer.allocate(100);

        Charset charset = Charset.defaultCharset();
        String data = "";
        int byteCount;

        while (true) {
            byteCount = readChannel.read(byteBuffer);
            if (byteCount == -1) break;
            byteBuffer.flip();
            data += charset.decode(byteBuffer).toString();
            byteBuffer.clear();
        }
        System.out.println(data);
        readChannel.close();

    }
}
