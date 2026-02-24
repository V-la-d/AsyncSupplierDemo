package org.asyncsupplierdemo;

import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AsyncSupplierDemo
 *
 * Demonstrates asynchronous suppliers in Java using CompletableFuture.
 *
 * Features:
 * - Basic async supply
 * - Async with custom ExecutorService
 * - Exception handling
 * - Combining results of two async suppliers
 * - Fibonacci generation with caching, odd/even partitioning, and sorting
 *
 * Configuration:
 * - THREAD_POOL_SIZE: thread pool size
 * - FIB_CACHE_INITIAL_SIZE: initial cache capacity and Fibonacci sequence length, can change for cache & sequence length
 *
 */
class AsyncSupplierDemo {

    /** ------------------- CONFIGURATION ------------------- */

    // Thread pool size
    private static final int THREAD_POOL_SIZE = 3; // <-- Change if needed

    // Fibonacci cache size and sequence length
    private static int FIB_CACHE_INITIAL_SIZE = 50; // <-- Change for cache & sequence length
    private static final ConcurrentMap<Integer, List<Long>> FIB_CACHE =
            new ConcurrentHashMap<>(FIB_CACHE_INITIAL_SIZE);

    private static final ExecutorService POOL = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    /** ------------------- Utility Methods ------------------- */

    private static Supplier<String> slowSupplier(String name, long millis) {
        return () -> {
            try {
                log(name + " started");
                Thread.sleep(millis);
                log(name + " finished");
                return name + "-result";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return name + "-interrupted";
            }
        };
    }

    private static void log(String message) {
        System.out.println(message + " [Thread: " + Thread.currentThread().getName() + "]");
    }

    /** ------------------- Demo Methods ------------------- */

    public static void demoBasicSupplyAsync() throws Exception {
        log("--- demoBasicSupplyAsync ---");
        CompletableFuture<String> f = CompletableFuture.supplyAsync(slowSupplier("BasicSupplier", 800));
        log("Doing other work while supplier runs...");
        log("Result: " + f.get(2, TimeUnit.SECONDS));
    }

    public static void demoSupplyAsyncWithExecutor() throws Exception {
        log("--- demoSupplyAsyncWithExecutor ---");
        CompletableFuture<String> f = CompletableFuture.supplyAsync(slowSupplier("ExecutorSupplier", 600), POOL);
        log("Result from custom executor: " + f.get(2, TimeUnit.SECONDS));
    }

    public static void demoExceptionHandling() throws Exception {
        log("--- demoExceptionHandling ---");
        Supplier<String> bad = () -> {
            log("Bad supplier running");
            throw new RuntimeException("boom");
        };

        CompletableFuture<String> f = CompletableFuture.supplyAsync(bad, POOL)
                .handle((res, ex) -> ex != null ? "fallback" : res);

        log("Handled result: " + f.get());
    }

    public static void demoCombineTwoSuppliers() throws Exception {
        log("--- demoCombineTwoSuppliers ---");
        CompletableFuture<String> a = CompletableFuture.supplyAsync(slowSupplier("Left", 700), POOL);
        CompletableFuture<String> b = CompletableFuture.supplyAsync(slowSupplier("Right", 500), POOL);

        CompletableFuture<String> combined = a.thenCombine(b, (ra, rb) -> ra + "+" + rb);
        log("Combined result: " + combined.get(2, TimeUnit.SECONDS));
    }

    /** ------------------- Fibonacci Utilities ------------------- */

    // Generate Fibonacci sequence using FIB_CACHE_INITIAL_SIZE
    private static List<Long> generateFibonacci() {
        int n = FIB_CACHE_INITIAL_SIZE; // Use config as sequence length
        return FIB_CACHE.computeIfAbsent(n, key -> {
            List<Long> fib = new ArrayList<>(key);
            fib.add(0L);
            if (key > 1) fib.add(1L);
            for (int i = 2; i < key; i++) {
                fib.add(fib.get(i - 1) + fib.get(i - 2));
            }
            return Collections.unmodifiableList(fib);
        });
    }

    public static void demoFibonacciPartitionAndSortAsync() throws Exception {
        log("--- demoFibonacciPartitionAndSortAsync ---");

        CompletableFuture<List<Long>> fibFuture = CompletableFuture.supplyAsync(() -> {
            log("Generating Fibonacci sequence...");
            return generateFibonacci();
        }, POOL);

        CompletableFuture<List<Long>> oddFuture = fibFuture.thenApplyAsync(list ->
                list.stream().filter(i -> i % 2L != 0L).sorted().collect(Collectors.toList()), POOL);

        CompletableFuture<List<Long>> evenFuture = fibFuture.thenApplyAsync(list ->
                list.stream().filter(i -> i % 2L == 0L).sorted().collect(Collectors.toList()), POOL);

        log("Odd (sorted): " + oddFuture.get(1, TimeUnit.SECONDS));
        log("Even (sorted): " + evenFuture.get(1, TimeUnit.SECONDS));
    }

    /** ------------------- Cache Utilities ------------------- */

    public static void clearFibonacciCache() {
        FIB_CACHE.clear();
        log("Fibonacci cache cleared.");
    }

    public static void setFibonacciCacheSize(int size) {
        FIB_CACHE_INITIAL_SIZE = size;
        log("Fibonacci cache initial size and sequence length set to " + size);
    }

    /** ------------------- Main ------------------- */

    public static void main(String[] args) {
        try {
            demoBasicSupplyAsync();
            demoSupplyAsyncWithExecutor();
            demoExceptionHandling();
            demoCombineTwoSuppliers();
            demoFibonacciPartitionAndSortAsync();

            log("Requesting Fibonacci sequence again to demonstrate cache reuse...");
            List<Long> cached = generateFibonacci();
            log("Cached Fibonacci: " + cached);

        } catch (Exception e) {
            System.err.println("Demo run error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdownExecutor();
        }
    }

    private static void shutdownExecutor() {
        POOL.shutdownNow();
        try {
            if (!POOL.awaitTermination(1, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate in time");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}