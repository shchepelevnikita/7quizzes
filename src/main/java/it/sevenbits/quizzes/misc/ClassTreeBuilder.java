package it.sevenbits.quizzes.misc;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ClassTreeBuilder implements FileVisitor<Path> {
    Map<String, HashSet<AtomicReference<String>>> classInheritanceMap;

    public ClassTreeBuilder() {
        this.classInheritanceMap = new HashMap<>();
    }

    public Map<String, HashSet<AtomicReference<String>>> getClassInheritanceMap() {
        return classInheritanceMap;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
        if (path.toString().endsWith("java")) {
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
                                        else if (word.length() > 0 && Character.isUpperCase(word.charAt(0))){
                                            classInheritanceMap.computeIfAbsent(word.replaceAll("\\W(.*?)\\W|\\W", ""), w -> new HashSet<>()).add(className);
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
