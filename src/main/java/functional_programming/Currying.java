package functional_programming;

import java.util.function.Function;

public class Currying {

    public static void main(String[] args) {
        basicExample1();
        basicExample2();
    }

    private static void basicExample1() {
        System.out.println("********** basicExample1 called **********");

        // A curried function: Takes `a` and returns another function to take `b`
        Function<Integer, Function<Integer, Integer>> curriedAdd = a -> b -> a + b;

        // Partially apply the function by fixing the first argument
        Function<Integer, Integer> addFive = curriedAdd.apply(5);

        // Now, call the function with the second argument
        System.out.println(addFive.apply(10)); // Outputs: 15

        // OR call both arguments at once:
        System.out.println(curriedAdd.apply(3).apply(7)); // Outputs: 10
    }


    private static void basicExample2() {
        System.out.println("********** basicExample2 called **********");

        // (a) -> (b) -> (c) -> a + b + c - This is a curried function for three parameters
        Function<Integer, Function<Integer, Function<Integer, Integer>>> sumWithThreeParams =
                (a) -> (b) -> (c) -> a + b + c;

        // (a) -> (b) -> (c) -> (d) -> a + b + c + d - This is a curried function for four parameters
        Function<Integer, Function<Integer, Function<Integer, Function<Integer, Integer>>>> sumWithFourParams =
                (a) -> (b) -> (c) -> (d) -> a + b + c + d;

        // Example 1: Using sumWithThreeParams
        // Step 1: Partially apply the first argument `a = 1`
        Function<Integer, Function<Integer, Integer>> partialSum1 = sumWithThreeParams.apply(1);

        // Step 2: Partially apply the second argument `b = 2`
        Function<Integer, Integer> partialSum2 = partialSum1.apply(2);

        // Step 3: Fully apply the third argument `c = 3`
        int result = partialSum2.apply(3);
        System.out.println("Result of sumWithThreeParams: " + result); // Outputs: 6 (1 + 2 + 3)

        // Example 2: Using sumWithFourParams
        // Apply arguments step by step
        Function<Integer, Function<Integer, Function<Integer, Integer>>> partialSum3 = sumWithFourParams.apply(1); // Fix a

        Function<Integer, Function<Integer, Integer>> partialSum4 = partialSum3.apply(2); // Fix b

        Function<Integer, Integer> partialSum5 = partialSum4.apply(3); // Fix c

        int finalResult = partialSum5.apply(4); // Fix d

        System.out.println("Result of sumWithFourParams: " + finalResult); // Outputs: 10 (1 + 2 + 3 + 4)

        // Example 3: One-liner usage for sumWithThreeParams
        int quickResult = sumWithThreeParams.apply(5).apply(6).apply(7);

        System.out.println("Result of quick sumWithThreeParams: " + quickResult); // Outputs: 18 (5 + 6 + 7)

        // Example 4: One-liner usage for sumWithFourParams
        int quickResultFourParams = sumWithFourParams.apply(1).apply(2).apply(3).apply(4);

        System.out.println("Result of quick sumWithFourParams: " + quickResultFourParams); // Outputs: 10 (1 + 2 + 3 + 4)
    }

}
