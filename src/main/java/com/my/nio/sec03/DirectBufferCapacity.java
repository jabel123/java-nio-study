package com.my.nio.sec03;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

public class DirectBufferCapacity
{
    public static void main(String[] args)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
        System.out.println(byteBuffer.capacity() + " 바이트");

        CharBuffer charBuffer = ByteBuffer.allocateDirect(100).asCharBuffer();
        System.out.println(charBuffer.capacity() + " 바이트");

        IntBuffer intBuffer = ByteBuffer.allocateDirect(100).asIntBuffer();
        System.out.println(intBuffer.capacity() + " 정수");
    }
}
