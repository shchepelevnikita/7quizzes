package it.sevenbits.quizzes;

import it.sevenbits.quizzes.misc.MultithreadClassTreeBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Application {
    public static <T> HashSet<T> mergeSet(Set<T> a, Set<T> b) {
        HashSet<T> mergedSet = new HashSet<T>();
        mergedSet.addAll(a);
        mergedSet.addAll(b);
        return mergedSet;
    }

    public static void main(final String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the path: ");
        Path path = Paths.get(in.next());
        try {
            Map<String, HashSet<String>> classInheritanceMap = new HashMap<>();
            ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(4);

            MultithreadClassTreeBuilder mfa = new MultithreadClassTreeBuilder(threadPoolExecutor);

            Files.walkFileTree(path, mfa);

            for (Future<Map<String, HashSet<String>>> future: mfa.getFutures()) {
                future.get().forEach((key, value) -> classInheritanceMap.merge(key, value, Application::mergeSet));
            }
            threadPoolExecutor.shutdown();
            classInheritanceMap.entrySet().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
