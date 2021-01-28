package com.my.nio.practice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class HtServer
{
    ServerSocketChannel serverSocketChannel;
    List<Client> clientList = new Vector<>();
    Selector selector;

    class Client
    {
        SocketChannel socketChannel;

        public Client(SocketChannel channel) {
            try {
                System.out.println("[접속 : " + channel.getRemoteAddress() + " ]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void close()
        {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void readMessage() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
            Charset charset = Charset.defaultCharset();
            try {
                socketChannel.read(byteBuffer);
                byteBuffer.flip();
                String msg = charset.decode(byteBuffer).toString();
                System.out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void startServer()
    {
        try
        {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(5001));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true)
            {
                int select = selector.select();
                if (select == 0) continue;
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext())
                {
                    SelectionKey selectionKey = keyIterator.next();
                    if (selectionKey.isAcceptable())
                    {
                        ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel channel = serverChannel.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);

                        Client client = new Client(channel);
                        clientList.add(client);
                        selectionKey.attach(client);
                    }
                    else if (selectionKey.isReadable())
                    {
                        Client client = (Client) selectionKey.attachment();
                        client.readMessage();
                    }
                    else if (selectionKey.isWritable())
                    {

                    }
                }
                keyIterator.remove();;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdownServer()
    {
        try
        {
            serverSocketChannel.close();
            clientList.forEach(client -> client.close());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HtServer htServer = new HtServer();
        htServer.startServer();
    }
}
