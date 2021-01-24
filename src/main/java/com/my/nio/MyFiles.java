package com.my.nio;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class MyFiles {
    public static class WatchServiceThread extends Thread
    {
        @Override
        public void run() {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path directory = Paths.get("dir/tail");
                directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE
                                                , StandardWatchEventKinds.ENTRY_DELETE
                                                , StandardWatchEventKinds.ENTRY_MODIFY
                );

                while (true)
                {
                    WatchKey watchKey = watchService.take();
                    List<WatchEvent<?>> list = watchKey.pollEvents();
                    for (WatchEvent watchEvent : list)
                    {
                        WatchEvent.Kind kind = watchEvent.kind();
                        Path context = (Path)watchEvent.context();
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE)
                            System.out.println("file created " + context.toString());
                        else if (kind == StandardWatchEventKinds.ENTRY_DELETE)
                            System.out.println("file deleted " + context.toString());
                        else if (kind == StandardWatchEventKinds.ENTRY_MODIFY)
                            System.out.println("file modified " + context.toString());
                        else if (kind == StandardWatchEventKinds.OVERFLOW)
                        {}

                    }
                    boolean valid = watchKey.reset();
                    if(!valid) { break; }

                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        WatchServiceThread th = new WatchServiceThread();
        th.start();
    }
}
