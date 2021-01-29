package com.my.nio.sec08;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

public class AsynClientExample
{
    AsynchronousChannelGroup channelGroup;
    AsynchronousSocketChannel socketChannel;

    void startClient()
    {
        try {
            channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
                    Runtime.getRuntime().availableProcessors(),
                    Executors.defaultThreadFactory()
            );

            socketChannel = AsynchronousSocketChannel.open(channelGroup);
            socketChannel.connect(new InetSocketAddress("localhost", 5001), null
                    , new CompletionHandler<Void, Object>() {
                @Override
                public void completed(Void result, Object attachment) {
                    System.out.println("연결 완료");
                    receive();
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println("[서버 통신 안됨]");
                    if (socketChannel.isOpen()) {stopClient();};
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stopClient() {
        try {
            if (channelGroup !=null && !channelGroup.isShutdown())
            {
                channelGroup.shutdownNow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String data)
    {
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer byteBuffer = charset.encode(data);
        socketChannel.write(byteBuffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {
                System.out.println("보내기 완료");
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.out.println("서버 통신 안됨");
            }
        });
    }
    private void receive() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
        socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                attachment.flip();

                Charset charset = Charset.forName("UTF-8");
                String data = charset.decode(attachment).toString();
                System.out.println("받기 완료 : " + data);

                ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
                socketChannel.read(byteBuffer1, byteBuffer1, this);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("[서버 통신 안됨]");
                if (socketChannel.isOpen())
                {
                    stopClient();
                };
            }
        });
    }

}
