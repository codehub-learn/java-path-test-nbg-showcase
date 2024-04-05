package gr.codelearn.eshop.service.base.conditions;

import gr.codelearn.eshop.util.PropertiesReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

@Slf4j
public class CheckIfEnvironmentEqualsQACondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        try {
            PropertiesReader propertiesReader = new PropertiesReader("application.properties");
            String environment = propertiesReader.getProperty("environment");
            if (environment.equals("qa")) {
                return ConditionEvaluationResult.enabled("In Dev environment");
            }
        } catch (IOException e) {
            log.error("Properties file does not exist: {}.", e.getMessage());
        }
        return ConditionEvaluationResult.disabled("Not in Dev environment or properties file was not found.");
    }
}
