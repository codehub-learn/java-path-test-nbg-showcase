package gr.codelearn.eshop.service.example;

import gr.codelearn.eshop.service.extension.IgnoreIOExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

@ExtendWith(IgnoreIOExceptionHandler.class)
public class IOExceptionExceptionTest {

    @Test
    void throwIOException() throws IOException{
        // execution of test code...
        throw new IOException();
    }
}
