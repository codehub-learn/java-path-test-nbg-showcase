package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.service.base.ExceptionLoggingAndTestSummary;
import gr.codelearn.eshop.service.extension.CustomerTestTemplateInvocationContextProvider;
import gr.codelearn.eshop.service.extension.DatabaseManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

@Slf4j
@ExceptionLoggingAndTestSummary
@ExtendWith(DatabaseManager.class)
public class CustomerTemplateTest {

    @TestTemplate
    @ExtendWith(CustomerTestTemplateInvocationContextProvider.class) //TestTemplateInvocationContextProvider
    void registerMultipleCustomersTemplate(Map<String, Customer> customerMap){
        //... test code ...
    }
}
