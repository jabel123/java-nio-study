package com.my.nio.sec04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FilesCopyMethodExample {
    public static void main(String[] args) throws IOException {
        Path from = Paths.get("img/yumi.jpg");
        Path to = Paths.get("img/yumi1.jpg");

        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }
}
