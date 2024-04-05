package gr.codelearn.eshop.service.extension;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.IOException;

@Slf4j
public class IgnoreIOExceptionHandler implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (throwable instanceof IOException) {
            String className = context.getTestClass().get().getSimpleName();
            String methodName = context.getTestMethod().get().getName();
            log.warn("Method {} ({}) threw an IOException", methodName, className);
            return;
        }
        throw throwable;
    }
}
