package com.my.nio.sec08.self;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

public class MyAsyncClient
{
    AsynchronousSocketChannel clientChannel;
    AsynchronousChannelGroup group;

    public void clientStart()
    {
        try
        {
            group = AsynchronousChannelGroup.withFixedThreadPool(
                    Runtime.getRuntime().availableProcessors()
                    , Executors.defaultThreadFactory()
            );
            clientChannel = AsynchronousSocketChannel.open();
            clientChannel.connect(
                    new InetSocketAddress("localhost", 5001)
                    , null
                    , new CompletionHandler<Void, Void>()
                    {
                        @Override
                        public void completed (Void result, Void attachment)
                        {
                            System.out.println("접속 성공");
                        }

                        @Override
                        public void failed (Throwable exc, Void attachment)
                        {
                            System.out.println("접속 실패");
                        }
                    }
            );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main (String[] args)
    {
        MyAsyncClient client = new MyAsyncClient();
        client.clientStart();
    }
}
