#AsyncSupplierDemo

AsyncSupplierDemo demonstrates the use of asynchronous suppliers in Java using CompletableFuture.
It includes practical examples such as async execution, exception handling, combining results, and working with a cached Fibonacci sequence.

#Features

Basic Async Supply – Run a supplier asynchronously and get the result.

Async with Custom Executor – Supply tasks using a fixed thread pool.

Exception Handling – Handle exceptions gracefully with fallback values.

Combine Two Suppliers – Combine results from multiple async suppliers.

#Fibonacci Utilities:

Generate Fibonacci sequence with caching.

Partition sequence into odd and even numbers.

Sort partitions asynchronously.

Cache Management – Clear cache and adjust sequence length dynamically.

Configuration

THREAD_POOL_SIZE – Size of the thread pool used for async tasks.

FIB_CACHE_INITIAL_SIZE – Initial Fibonacci cache size and sequence length.

#Usage

Clone or copy the project.

Adjust configuration values if needed.

Run the main method:

java org.asyncsupplierdemo.AsyncSupplierDemo

#Expected Logs:

Async task execution and thread names.

Exception handling demonstration.

Fibonacci sequence generation, partitioning, and sorting.

Cache reuse behavior.

#Utility Methods

clearFibonacciCache() – Clears the cached Fibonacci sequences.

setFibonacciCacheSize(int size) – Updates the cache size and sequence length.

#Example Output

--- demoBasicSupplyAsync ---
BasicSupplier started [Thread: ForkJoinPool.commonPool-worker-1]
Doing other work while supplier runs...
BasicSupplier finished [Thread: ForkJoinPool.commonPool-worker-1]
Result: BasicSupplier-result
...
Odd (sorted): [1, 1, 3, 5, ...]
Even (sorted): [0, 2, 8, 34, ...]
Fibonacci cache cleared.

#Notes

Thread pool ensures controlled concurrency.

Fibonacci cache demonstrates computeIfAbsent usage with ConcurrentMap.

Perfect for learning async patterns in Java and practicing CompletableFuture usage.
