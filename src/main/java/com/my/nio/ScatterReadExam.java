package com.my.nio;

import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public class ScatterReadExam {
    public static void main(String[] args) {
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);

        ByteBuffer[] bufferArray = { header, body};

    }
}
