package ru.mikemimike.filegluer.service;

import org.eclipse.jgit.annotations.Nullable;
import org.eclipse.jgit.ignore.IgnoreNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static ru.mikemimike.filegluer.util.SeparatorUtil.getDefaultSeparator;

public class GluerService {
    private static final Logger log = LoggerFactory.getLogger(GluerService.class);

    public void concatenateFiles(String rootDir, String outputFile, @Nullable String ignoreFilePath, @Nullable String separator) {
        log.info("Gluing starting...");

        String sep;

        if (separator != null) {
            sep = separator;
        } else {
            sep = getDefaultSeparator();
        }

        if (ignoreFilePath == null) {
            walkFiles(rootDir, outputFile, sep);
        } else {
            walkFiles(rootDir, outputFile, ignoreFilePath, sep);
        }

        log.info("Gluing is completed. Result file is: {}", outputFile);

    }

    private void walkFiles(String rootDir, String outputFile, String ignoreFilePath, String separator) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(outputFile));
             InputStream inputStream = new FileInputStream(ignoreFilePath)) {
            IgnoreNode ignoreNode = new IgnoreNode();
            ignoreNode.parse(inputStream);

            Files.walkFileTree(Path.of(rootDir), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!shouldIgnore(file, ignoreNode, Path.of(rootDir))) {
                        byte[] fileContent = Files.readAllBytes(file);
                        writer.write(new String(fileContent));
                        writer.newLine();
                        writer.write(separator);
                        writer.newLine();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (shouldIgnore(dir, ignoreNode, Path.of(rootDir))) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void walkFiles(String rootDir, String outputFile, String separator) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(outputFile))) {
            Files.walkFileTree(Path.of(rootDir), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    byte[] fileContent = Files.readAllBytes(file);
                    writer.write(new String(fileContent));
                    writer.newLine();
                    writer.write(separator);
                    writer.newLine();

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean shouldIgnore(Path path, IgnoreNode ignoreNode, Path rootDir) {
        boolean isDirectory = Files.isDirectory(path);
        String relativizePath = rootDir.relativize(path).toString().replace("\\", "/");

        Boolean isIgnored = ignoreNode.checkIgnored(relativizePath, isDirectory);
        return isIgnored == null ? false : isIgnored;
    }
}
