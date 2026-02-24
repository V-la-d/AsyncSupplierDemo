# AsyncSupplierDemo (Developer README)

> **Note for developers:**  
> For a full explanation of how the program works, see the main project README.

**AsyncSupplierDemo** demonstrates the use of asynchronous suppliers in Java using `CompletableFuture`.  
It includes practical examples such as async execution, exception handling, combining results, and working with a cached Fibonacci sequence.

---

## Features

- **Basic Async Supply** – Run a supplier asynchronously and get the result.
- **Async with Custom Executor** – Supply tasks using a fixed thread pool.
- **Exception Handling** – Handle exceptions gracefully with fallback values.
- **Combine Two Suppliers** – Combine results from multiple async suppliers.
- **Fibonacci Utilities**:
  - Generate Fibonacci sequence with caching
  - Partition sequence into odd and even numbers
  - Sort partitions asynchronously
- **Cache Management** – Clear cache and adjust sequence length dynamically

---

## Configuration

- **THREAD_POOL_SIZE** – Size of the thread pool used for async tasks
- **FIB_CACHE_INITIAL_SIZE** – Initial Fibonacci cache size and sequence length

---

## Usage

1. Clone or copy the project.
2. Adjust configuration values if needed.
3. Run the main method:

```bash
java org.asyncsupplierdemo.AsyncSupplierDemo
