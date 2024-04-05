package gr.codelearn.eshop.service.base;

import gr.codelearn.eshop.service.extension.ExceptionLoggingHandler;
import gr.codelearn.eshop.service.extension.TestSummary;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.Element;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({ExceptionLoggingHandler.class, TestSummary.class})
public @interface ExceptionLoggingAndTestSummary {
}
