package gr.codelearn.eshop.service.extension;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

@Slf4j
public class ExceptionLoggingHandler implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        log.error("Unexpected Exception was thrown in one of the tests in class '{}' and method '{}'",
                context.getTestClass().get().getSimpleName(), context.getTestMethod().get().getName(), throwable);
        throw throwable;
    }
}
