package com.my.nio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable{
    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    ByteBuffer input = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;
    String clientName = "";

    public Handler(SocketChannel socketChannel, Selector selector) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try
        {
            if (state == READING)
                read();
            else if(state == SENDING)
                send();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void send() throws IOException {
        System.out.println("Say hello to " + clientName);
        ByteBuffer output = ByteBuffer.wrap(("Hello" + clientName + "\n").getBytes());
        socketChannel.write(output);
        selectionKey.interestOps(SelectionKey.OP_READ);
        state = READING;
    }

    synchronized private void readProcess(int readCount) {
        StringBuilder sb = new StringBuilder();
        input.flip();
        byte[] subStringBytes = new byte[readCount];
        byte[] array = input.array();
        System.arraycopy(array, 0, subStringBytes, 0, readCount);

        sb.append(new String(subStringBytes));
        input.clear();
        clientName = sb.toString().trim();

    }

    private void read() throws IOException {
        int readCount = socketChannel.read(input);
        if (readCount > 0)
        {
            readProcess(readCount);
        }
        state = SENDING;
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }
}
