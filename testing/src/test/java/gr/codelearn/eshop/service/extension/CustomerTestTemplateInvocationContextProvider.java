package gr.codelearn.eshop.service.extension;

import gr.codelearn.eshop.service.base.conditions.CheckIfEnvironmentEqualsDevCondition;
import gr.codelearn.eshop.service.base.conditions.CheckIfEnvironmentEqualsQACondition;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.util.List;
import java.util.stream.Stream;

public class CustomerTestTemplateInvocationContextProvider implements TestTemplateInvocationContextProvider{
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Stream.of(devContext("with Dev sample products"), qaContext("with QA  sample products"));
    }

    private TestTemplateInvocationContext devContext(String testName) {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return testName;
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Lists.newArrayList(new CheckIfEnvironmentEqualsDevCondition(), new CustomerParameterResolver());
            }
        };
    }

    private TestTemplateInvocationContext qaContext(String testName) {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(int invocationIndex) {
                return testName;
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Lists.newArrayList(new CheckIfEnvironmentEqualsQACondition(), new CustomerParameterResolver());
            }
        };
    }
}
