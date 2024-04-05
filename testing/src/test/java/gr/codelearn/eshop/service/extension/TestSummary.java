package gr.codelearn.eshop.service.extension;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

@Slf4j
public class TestSummary implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        String className = context.getTestClass().get().getSimpleName();
        context.getStore(ExtensionContext.Namespace.create(className)).put(className, System.currentTimeMillis());
        /*long timeStarted = System.currentTimeMillis();
        // execution of code...
        long timeFinished = System.currentTimeMillis();
        long timeItTook = timeFinished - timeStarted;*/
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        String className = context.getTestClass().get().getSimpleName();
        String methodName = context.getTestMethod().get().getName();
        context.getStore(ExtensionContext.Namespace.create(className)).put(methodName,
                System.currentTimeMillis());
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        String className = context.getTestClass().get().getSimpleName();
        String methodName = context.getTestMethod().get().getName();
        long startTime = context.getStore(ExtensionContext.Namespace.create(className)).get(methodName, long.class);
        long timeTook = System.currentTimeMillis() - startTime;
        log.info("Method '{}' ({}) took {}ms.", methodName, className, timeTook);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        String className = context.getTestClass().get().getSimpleName();
        Long timeStarted = context.getStore(ExtensionContext.Namespace.create(className)).get(className, long.class);
        Long result = System.currentTimeMillis() - timeStarted;
        log.info("Time it took to complete class {}: {}ms", className, result);
    }
}
