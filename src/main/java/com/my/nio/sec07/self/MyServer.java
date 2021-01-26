package com.my.nio.sec07.self;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class MyServer {
    ServerSocketChannel serverSocketChannel;
    Selector selector;
    List<Client> clientList = new Vector<>();

    class Client
    {
        String sendData;
        SocketChannel socketChannel;

        public Client(SocketChannel socketChannel) {
            try {
                this.socketChannel = socketChannel;
                socketChannel.configureBlocking(false);
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                selectionKey.attach(this);
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

        public void receive(SelectionKey selectionKey) {
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

                for(Client client : clientList)
                {
                    client.sendData = data;
                    SelectionKey key = client.socketChannel.keyFor(selector);
                    key.interestOps(SelectionKey.OP_WRITE);
                }
                selector.wakeup(); // 작업유형이 변경이되면 selector의 wakeup을 호출하라!
            } catch (IOException e) {
                try {
                    clientList.remove(this);
                    String msg = "[클라이언트 통신 안됨: " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName() + " ]";
                    System.out.println(msg);
                    socketChannel.close();
                } catch (IOException ioException) {


                }
            }
        }
    }

    public void serverStart() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(5001));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(() -> {
            try
            {
                while (true)
                {
                    int select = selector.select();
                    if (select == 0) continue;
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    for (SelectionKey selectionKey : selectionKeys)
                    {
                        if (selectionKey.isAcceptable())
                        {
                            accept(selectionKey);
                        }
                        else if (selectionKey.isReadable())
                        {
                            Client client = (Client) selectionKey.attachment();
                            client.receive(selectionKey);
                        }
                        else if (selectionKey.isWritable())
                        {

                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }

    private void accept(SelectionKey selectionKey) {
        try
        {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            clientList.add(new Client(socketChannel));
            System.out.println("[연결된 클라이언트 수 : " + clientList.size() + " ]");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopServer()
    {
        try {
            clientList.stream().forEach(client -> client.close());
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyServer myServer = new MyServer();
        myServer.serverStart();
    }
}
