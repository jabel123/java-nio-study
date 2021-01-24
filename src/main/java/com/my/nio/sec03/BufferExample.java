package com.my.nio.sec03;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferExample {
    public static void main(String[] args) {
        System.out.println("[7바이트 크기로 버퍼 생성]");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(7);
        printState(byteBuffer);

        byteBuffer.put((byte) 10);
        byteBuffer.put((byte) 11);
        System.out.println("[2바이트 저장후]");
        printState(byteBuffer);

        byteBuffer.put((byte) 12);
        byteBuffer.put((byte) 13);
        byteBuffer.put((byte) 14);
        System.out.println("[3바이트 저장후]");
        printState(byteBuffer);

        byteBuffer.flip();

        System.out.println("[read 모드로 바뀐 후]");
        printState(byteBuffer);

        System.out.println("[1바이트 읽은후] -- 이 위치에 마크함");
        byteBuffer.get();
        byteBuffer.mark();
        printState(byteBuffer);

        System.out.println("[3바이트 읽은후]");
        byteBuffer.get(new byte[3]);
        printState(byteBuffer);

        System.out.println("reset 후 ");
        byteBuffer.reset();
        printState(byteBuffer);

        byteBuffer.clear();
        System.out.println("[clear 실행후]");
        printState(byteBuffer);
    }

    public static void printState(Buffer buffer)
    {
        System.out.print("\tposition : " + buffer.position() + ", ");
        System.out.print("\tlimit : " + buffer.limit() + ", ");
        System.out.println("\tcapacity : " + buffer.capacity());
    }
}
