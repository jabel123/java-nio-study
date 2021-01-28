package com.my.nio.practice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class HtClient {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("localhost", 5001));
        System.out.println("보내기 요청");


        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = charset.encode("메시지 전달되라고!!! 망할놈아!!");
        channel.write(byteBuffer);

        System.out.println("채널 닫을래");
        channel.close();
    }
}
