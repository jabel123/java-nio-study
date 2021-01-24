package com.my.nio;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class MyFileSystem {
    public static void main(String[] args) throws IOException {
        FileSystem fileSystem = FileSystems.getDefault();

        for (FileStore store: fileSystem.getFileStores())
        {
            System.out.println("driver name " + store.name());
            System.out.println("filesystem name " + store.type());
            System.out.println("total space " + store.getTotalSpace() + "byte");
            System.out.println("used space " + (store.getTotalSpace() - store.getUnallocatedSpace()) + "byte");
            System.out.println("allocate space " + store.getUsableSpace() + "byte");
            System.out.println();

            System.out.println("file seperator : " + fileSystem.getSeparator());
            System.out.println();
        }

        for (Path path: fileSystem.getRootDirectories())
        {
            System.out.println(path.toString());
        }
    }
}
