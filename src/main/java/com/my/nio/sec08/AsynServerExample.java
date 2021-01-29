package com.my.nio.sec08;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class AsynServerExample {
    AsynchronousChannelGroup channelGroup;
    AsynchronousServerSocketChannel serverSocketChannel;
    List<Client> connections = new Vector<>();

    void startServer()
    {
        try
        {
            channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
                    Runtime.getRuntime().availableProcessors(),
                    Executors.defaultThreadFactory()
            );

            serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            serverSocketChannel.bind(new InetSocketAddress(5001));
            System.out.println("[ 서버 시작 ]");
        }
        catch (IOException e) {
            if (serverSocketChannel.isOpen())
            {
                serverStop();
            }
            return;
        }
        
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
                try {
                    System.out.println("연결 수락 :  " +  socketChannel.getRemoteAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Client client = new Client(socketChannel);
                connections.add(client);

                serverSocketChannel.accept(null, this);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                if (serverSocketChannel.isOpen())
                {
                    serverStop();
                }
            }
        });
    }

    class Client {

        AsynchronousSocketChannel socketChannel;
        public Client(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        public void receive()
        {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
            socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    String msg;
                    try {
                        msg = "[요청처리" +  socketChannel.getRemoteAddress() + "]";
                        System.out.println(msg);

                        attachment.flip();
                        Charset charset = Charset.forName("UTF-8");
                        String data = charset.decode(attachment).toString();

                        for (Client client : connections)
                        {
                            client.send(data);
                        }

                        ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
                        socketChannel.read(byteBuffer1, byteBuffer1, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        String msg = "클라이언트 통신 안됨 : " + socketChannel.getRemoteAddress();
                        System.out.println(msg);
                        connections.remove(Client.this);
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        public void send(String data)
        {
            Charset charset = Charset.forName("UTF-8");
            ByteBuffer byteBuffer = charset.encode("UTF-8");
            socketChannel.write(byteBuffer, null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void attachment) {

                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    try {
                        String msg = "클라이언트 통신 안됨 : " + socketChannel.getRemoteAddress();
                        System.out.println(msg);
                        connections.remove(Client.this);
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void serverStop() {
        connections.clear();
        if (channelGroup != null && !channelGroup.isShutdown())
        {
            try {
                channelGroup.shutdownNow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
