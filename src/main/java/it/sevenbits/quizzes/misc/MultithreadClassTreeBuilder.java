package it.sevenbits.quizzes.misc;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class MultithreadClassTreeBuilder implements FileVisitor<Path> {
    private final ExecutorService tpe;
    private final Collection<Future<Map<String, HashSet<String>>>> futures;

    public MultithreadClassTreeBuilder(ExecutorService tpe) {
        this.futures = new ConcurrentLinkedQueue<>();
        this.tpe = tpe;
    }

    public Collection<Future<Map<String, HashSet<String>>>> getFutures() {
        return futures;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    private Map<String, HashSet<String>> handleFile(Path path) {
        Map<String, HashSet<String>> classInheritanceMap = new ConcurrentHashMap<>();
        try (Stream<String> stream = Files.lines(path)) {
            AtomicBoolean classFlag = new AtomicBoolean(false);
            AtomicBoolean implementsFlag = new AtomicBoolean(false);
            AtomicReference<String> className = new AtomicReference<>();

            stream.map(str -> str.split(" "))
                    .forEach(arr -> Stream.of(arr)
                            .forEach(word -> {
                                if (classFlag.get()) {
                                    className.set(word);
                                    classFlag.set(false);
                                }
                                if (implementsFlag.get()) {
                                    if (Objects.equals(word, "{")) implementsFlag.set(false);
                                    else if (word.length() > 0 && Character.isUpperCase(word.charAt(0))) {
                                        classInheritanceMap.computeIfAbsent(word.replaceAll("\\W(.*?)\\W|\\W", ""), w -> new HashSet<>()).add(className.get());
                                    }
                                }
                                if (Objects.equals(word, "class") || Objects.equals(word, "interface")) {
                                    classFlag.set(true);
                                }
                                if (Objects.equals(word, "implements") || Objects.equals(word, "extends")) {
                                    implementsFlag.set(true);
                                }
                            }));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can not read file");
        }
        return classInheritanceMap;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
        if (path.toString().endsWith("java")) {
            futures.add(tpe.submit(() -> handleFile(path)));
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
