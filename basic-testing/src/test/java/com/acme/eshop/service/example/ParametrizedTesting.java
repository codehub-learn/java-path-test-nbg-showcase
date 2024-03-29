package com.acme.eshop.service.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

public class ParametrizedTesting {
    // ValueSource (values on the spot)
    // MethodSource (values from another method)
    // ArgumentSource (values from another class)

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
        // useful for: boundary testing <--- boundary values
    void valueSourceTesting(int integerToTest) {
        System.out.println(integerToTest);
    }

    @ParameterizedTest(name = "Iteration {index} : Validating value: {0}") //{index}, {arguments}, {0}...{n}
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
        // useful for: boundary testing <--- boundary values
    void valueSourceTesting2(int integerToTest) {
        System.out.println(integerToTest);
    }

    @ParameterizedTest
    @MethodSource("integerProvider")
    void methodSourceTesting(int integerToTest) {
        System.out.println(integerToTest);
    }

    static Stream<Integer> integerProvider() {
        return Stream.of(8, 9, 10, 11, 12, 13); // could also be return of arguments (JUNit class)
    }

    @ParameterizedTest
    @ArgumentsSource(ParametrizedTestingIntegerArgumentSource.class)
    void argumentsSourceTesting(int integerToTest, String stringToTest) {
        System.out.println(integerToTest);
        System.out.println(stringToTest);
    }

}
