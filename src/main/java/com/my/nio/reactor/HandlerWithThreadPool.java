package com.my.nio.reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerWithThreadPool extends Handler{
    static ExecutorService pool = Executors.newFixedThreadPool(2);
    static final int PROCESSING = 2;

    public HandlerWithThreadPool(SocketChannel socketChannel, Selector selector) throws IOException {
        super(socketChannel, selector);
    }


    void read() throws IOException {
        int readCount = socketChannel.read(input);
        if (readCount > 0) {
            state = PROCESSING;
            pool.execute(
                    new Processer(readCount)
            );
        }
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    class Processer implements Runnable {

        int readCount;

        Processer(int readCount)
        {
            this.readCount = readCount;
        }

        @Override
        public void run() {

        }
    }

}
