package com.my.nio.sec07;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ServerExample
{
    Selector selector;
    ServerSocketChannel serverSocketChannel;
    List<Client> connections = new Vector<Client>();

    void startServer()
    {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(5001));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            if (serverSocketChannel.isOpen())
            {
                stopServer();
            }
            return;
        }

        Thread thread = new Thread(() -> {
            while (true) {
                try
                {
                    int keyCount = selector.select();
                    if (keyCount == 0) continue;

                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext())
                    {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable())
                        {
                            accept(selectionKey);
                        }
                        else if(selectionKey.isReadable())
                        {
                            Client client = (Client) selectionKey.attachment();
                            client.receive(selectionKey);
                        }
                        else if(selectionKey.isWritable())
                        {
                            Client client = (Client) selectionKey.attachment();
                            client.send(selectionKey);
                        }
                        iterator.remove();
                    }
                }
                catch (IOException e) {
                    if (serverSocketChannel.isOpen())
                    {
                        stopServer();
                    }
                    break;
                }
            }
        });
        thread.start();
        System.out.println("[서버 시작]");
    }

    void stopServer()
    {
        try
        {
            Iterator<Client> iterator = connections.iterator();
            while (iterator.hasNext())
            {
                Client client = iterator.next();
                client.socketChannel.close();
                iterator.remove();
            }

            if (serverSocketChannel != null && serverSocketChannel.isOpen())
            {
                serverSocketChannel.close();
            }

            if (selector != null && selector.isOpen())
                selector.close();

            System.out.println("[서버 멈춤]");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void accept(SelectionKey selectionKey)
    {
        try
        {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();

            String msg = "[연결 수락 : " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + "]";

            Client client = new Client(socketChannel);
            connections.add(client);

            System.out.println("[ 연결개수 : " + connections.size() + " ]");

        }
        catch (IOException e)
        {
            if (serverSocketChannel.isOpen())
            {
                stopServer();
            }
        }

    }

    class Client {
        SocketChannel socketChannel;
        String sendData;

        public Client(SocketChannel socketChannel) throws IOException {
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
            selectionKey.attach(this);
        }

        void receive(SelectionKey selectionKey) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(100);

                int byteCount = socketChannel.read(byteBuffer);

                if (byteCount == -1)
                {
                    throw new IOException();
                }

                String msg = "[요청 처리: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + " ]";

                byteBuffer.flip();
                Charset charset = Charset.forName("UTF-8");
                String data = charset.decode(byteBuffer).toString();

                for(Client client : connections)
                {
                    client.sendData = data;
                    SelectionKey key = client.socketChannel.keyFor(selector);
                    key.interestOps(SelectionKey.OP_WRITE);
                }
                selector.wakeup(); // 작업유형이 변경이되면 selector의 wakeup을 호출하라!
            } catch (IOException e) {
                try {
                    connections.remove(this);
                    String msg = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + " ]";
                    System.out.println(msg);
                    socketChannel.close();
                } catch (IOException ioException) {


                }
            }
        }

        void send(SelectionKey selectionKey) {
            try {
                Charset charset = Charset.forName("UTF-8");
                ByteBuffer byteBuffer = charset.encode(sendData);
                socketChannel.write(byteBuffer);
                selectionKey.interestOps(SelectionKey.OP_READ);
                selector.wakeup();
            } catch (IOException e) {
                try {
                    connections.remove(this);
                    String msg = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + " ]";
                    System.out.println(msg);
                    socketChannel.close();
                } catch (IOException ioException) {
                }
            }
        }
    }
}
