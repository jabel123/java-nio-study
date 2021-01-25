package com.my.nio.sec06;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ServerExample {
    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = null;

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.bind(new InetSocketAddress(5001));

            while (true)
            {
                System.out.println("[연결 기다림..]");
                SocketChannel socketChannel = serverSocketChannel.accept();
                InetSocketAddress isa = (InetSocketAddress) socketChannel.getRemoteAddress();

                ByteBuffer buffer = ByteBuffer.allocate(100);
                Charset charset = Charset.forName("UTF-8");
                String msg = "";
                while (true)
                {
                    int byteCount = socketChannel.read(buffer);
                    if (byteCount == -1) break;
                    buffer.flip();
                    msg += charset.decode(buffer).toString();
                    buffer.clear();
                }

                System.out.println("데이터 받기 성공 : " + msg);

                buffer = charset.encode("Hello Client");
                socketChannel.write(buffer);




                System.out.println("[연결 수락함]" + isa.getHostName());
            }
        }
        catch (IOException e ) {
            e.printStackTrace();
        }

        if (serverSocketChannel.isOpen()) {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
