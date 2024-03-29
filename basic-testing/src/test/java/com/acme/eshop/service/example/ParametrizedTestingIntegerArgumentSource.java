package com.acme.eshop.service.example;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ParametrizedTestingIntegerArgumentSource implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(Arguments.of(100, "test"),Arguments.of(200, "test2"), Arguments.of(300, "test3"), Arguments.of(400, "test4"));
    }
}
