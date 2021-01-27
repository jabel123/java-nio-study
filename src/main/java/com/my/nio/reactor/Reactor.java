package com.my.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable{
    final Selector selector;
    final ServerSocketChannel serverSocketChannel;
    final boolean isWithThreadPool;

    Reactor(int port, boolean isWithThreadPool) throws IOException
    {
        this.isWithThreadPool = isWithThreadPool;
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(5001));
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }

    @Override
    public void run() {
        try
        {
            while (!Thread.interrupted())
            {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext())
                {
                    dispatch(iterator.next());
                }
                selectionKeys.clear();;
            }
        }
        catch(IOException ex)
        {

        }
    }

    private void dispatch(SelectionKey next) {
        Runnable r = (Runnable) next.attachment();
        if (r != null)
        {
            r.run();
        }
    }
    class Acceptor implements Runnable
    {

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null)
                {
                    if (isWithThreadPool)
                    {
                        new HandlerWithThreadPool(socketChannel, selector);
                    }
                    else
                        new Handler(socketChannel, selector);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
