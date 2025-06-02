package functional_programming;

import java.util.function.Function;

public class Composition {
    public static void main(String[] args) {
        // First function: square a number
        Function<Integer, Integer> square = x -> x * x;

        // Second function: double a number
        Function<Integer, Integer> doubleIt = x -> x * 2;

        // Compose square and double into a new function
        Function<Integer, Integer> doubleOfSquare = doubleIt.compose(square);

        // Test it
        System.out.println(doubleOfSquare.apply(3)); // Outputs: 18 (3^2 = 9, 9 * 2 = 18)

        // You can also go in reverse order
        Function<Integer, Integer> squareOfDouble = square.compose(doubleIt);
        System.out.println(squareOfDouble.apply(3)); // Outputs: 36 (3 * 2 = 6, 6^2 = 36)
    }
}