package com.my.nio.sec04;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileChannelWriteExample {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("C:/Temp/File.txt");
        Files.createDirectories(path.getParent());

        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        String data = "안녕하세요 키노시타 아유미!!!!!!!";
        Charset charset = Charset.defaultCharset();
        ByteBuffer byteBuffer = charset.encode(data);

        int intByteCount = fileChannel.write(byteBuffer);

        System.out.println("file.txt : " + intByteCount + " bytes written ");

        fileChannel.close();

    }
}
