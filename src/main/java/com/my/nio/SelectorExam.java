package com.my.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorExam {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        Selector selector = Selector.open();
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector, SelectionKey.OP_READ);

        while (true)
        {
            int readChannels = selector.selectNow();
            if (readChannels == 0) continue;

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext())
            {
                SelectionKey tkey = iterator.next();

                if (tkey.isAcceptable()) {

                }
                else if (tkey.isConnectable()){

                }
                else if (tkey.isReadable()) {

                }
                else if (tkey.isWritable()) {

                }

                iterator.remove();
            }
        }

    }
}
