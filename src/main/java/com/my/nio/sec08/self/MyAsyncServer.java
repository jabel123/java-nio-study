package com.my.nio.sec08.self;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;

public class MyAsyncServer
{
    AsynchronousServerSocketChannel serverSocketChannel;
    AsynchronousChannelGroup channelGroup;
    List<Client> connectors = new Vector<>();

    class Client
    {
        AsynchronousSocketChannel channel;

        public Client (AsynchronousSocketChannel channel)
        {
            this.channel = channel;
            try
            {
                System.out.println("[ 연결 : " + channel.getRemoteAddress() + " ]");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }

        public void send()
        {

        }

        public void receive()
        {

        }
    }

    public void serverStart()
    {
        System.out.println("[ 서버가 시작되었습니다.]");
        try
        {
            channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
                    Runtime.getRuntime().availableProcessors()
                    , Executors.defaultThreadFactory()
            );
            serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            serverSocketChannel.bind(new InetSocketAddress(5001));
            serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>()
            {
                @Override
                public void completed (AsynchronousSocketChannel channel, Void attachment)
                {
                    connectors.add(new Client(channel));
                    serverSocketChannel.accept(null, this);
                }

                @Override
                public void failed (Throwable exc, Void attachment)
                {
                    System.out.println("연결에 실패했습니다.");

                }
            });

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main (String[] args)
    {
        MyAsyncServer server = new MyAsyncServer();
        server.serverStart();
    }
}
