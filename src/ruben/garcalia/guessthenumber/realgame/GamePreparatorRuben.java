package ruben.garcalia.guessthenumber.realgame;

import java.util.List;
import java.util.Random;

import ruben.garcalia.guessthenumber.realgame.GameRuben;

public class GamePreparatorRuben {

  static String GetRandomError(int value) {
    String[] memoryErrors = {
        "\033[31mError\033[0m: Memory leak detected for value \033[33m%d\033[0m. Failed to deallocate memory after 3 attempts.",
        "\033[31mError\033[0m: Segmentation fault (core dumped) while accessing memory location for value \033[33m%d\033[0m.",
        "\033[31mError\033[0m: Double free detected for value \033[33m%d\033[0m. Memory was deallocated twice.",
        "\033[31mError\033[0m: Buffer overflow detected when storing value \033[33m%d\033[0m. Exceeded allocated memory space.",
        "\033[31mError\033[0m: Invalid memory access for value \033[33m%d\033[0m. Attempted to read/write memory not owned by process.",
        "\033[31mError\033[0m: Memory corruption detected for value \033[33m%d\033[0m. Data integrity check failed.",
        "\033[31mError\033[0m: Uninitialized memory access detected for value \033[33m%d\033[0m. Variable used before being initialized.",
        "\033[31mError\033[0m: Out of memory when attempting to store value \033[33m%d\033[0m. No available memory to allocate.",
        "\033[31mError\033[0m: Stack overflow detected while processing value \033[33m%d\033[0m. Maximum stack size exceeded.",
        "\033[31mError\033[0m: Heap exhaustion detected for value \033[33m%d\033[0m. Unable to allocate memory from the heap.",
        "\033[31mError\033[0m: Invalid free operation for value \033[33m%d\033[0m. Attempted to deallocate unallocated memory.",
        "\033[31mError\033[0m: Null pointer dereference when accessing value \033[33m%d\033[0m. Attempted to use null pointer.",
        "\033[31mError\033[0m: Memory alignment error for value \033[33m%d\033[0m. Incorrectly aligned memory access detected.",
        "\033[31mError\033[0m: Dangling pointer detected for value \033[33m%d\033[0m. Access to memory after it was freed.",
        "\033[31mError\033[0m: Garbage collection failure when attempting to clean up value \033[33m%d\033[0m. Memory not reclaimed.",
        "\033[31mError\033[0m: Critical memory error encountered for value \033[33m%d\033[0m. System stability at risk.",
        "\033[31mError\033[0m: Page fault detected when accessing value \033[33m%d\033[0m. Requested page not in memory.",
        "\033[31mError\033[0m: Severe memory fragmentation detected for value \033[33m%d\033[0m. Inefficient memory allocation pattern.",
        "\033[31mError\033[0m: Cache coherency issue detected for value \033[33m%d\033[0m. Inconsistent data across CPU caches.",
        "\033[31mError\033[0m: Virtual memory exhaustion detected when allocating for value \033[33m%d\033[0m. Swap space depleted."
    };

    Random random = new Random();
    String randomError = memoryErrors[random.nextInt(memoryErrors.length)];

    String formattedError = String.format(randomError, value);

    return formattedError;
  }

  public static void printErrors(List<Integer> numbers) {
    System.out.println("Starting garbage collector...");
    for (int i = 0; i < numbers.size(); i++) {
      System.out.println(GetRandomError(numbers.get(i)));
      try {
        Thread.sleep((int) (Math.random() * 2000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    runGame(numbers);
  }

  public static void runGame(List<Integer> numbers) {
    new GameRuben().run();
  }

}
