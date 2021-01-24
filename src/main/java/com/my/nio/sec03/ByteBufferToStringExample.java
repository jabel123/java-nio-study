package com.my.nio.sec03;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ByteBufferToStringExample {
    public static void main(String[] args) {
        Charset charset = Charset.forName("UTF-8");

        String data = "안녕하세요";
        ByteBuffer buffer = charset.encode(data);

        data = charset.decode(buffer).toString();

        System.out.println(data);
    }
}
