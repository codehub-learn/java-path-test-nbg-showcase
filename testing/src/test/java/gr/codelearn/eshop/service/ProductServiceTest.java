package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.CustomerCategory;
import gr.codelearn.eshop.exception.InvalidObjectValuesException;
import gr.codelearn.eshop.service.base.ExceptionLoggingAndTestSummary;
import gr.codelearn.eshop.service.extension.DatabaseManager;
import gr.codelearn.eshop.service.extension.TestSummary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DatabaseManager.class)
@ExceptionLoggingAndTestSummary
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {

    // for showcase only
    @Test
    @DisplayName("sorts customers by default order (lexicographically by email")
    void sortsByEmail2() {
        try {
            CustomerService customerService = new CustomerServiceImpl();
            Customer daniil = Customer.builder("aidaniil@gmail.com", CustomerCategory.GOVERNMENT)
                    .setFirstname("Ioannis").setLastname("Daniil").setAge(27).setAddress("Schiller Diec 17").build();
            Customer klian = Customer.builder("biklian@gmail.com", CustomerCategory.INDIVIDUAL).setFirstname(
                    "Ioannis").setLastname("Klian").setAge(16).setAddress("New Illinois 1").build();
            Customer giannacoulis = Customer.builder("cgiannacoulis@gmail.com",
                    CustomerCategory.BUSINESS).setFirstname(
                    "Constantinos").setLastname("Giannacoulis").setAge(80).setAddress("Athens St. 1337").build();
            Customer katsifis = Customer.builder("dmkatsifis@gmail.com", CustomerCategory.INDIVIDUAL)
                    .setFirstname("Meletis").setLastname("Katsifis").setAge(68).setAddress("Leof. Alexandras 205").build();
            customerService.register(daniil);
            customerService.register(klian);
            customerService.register(giannacoulis);
            customerService.register(katsifis);
            List<Customer> expectedCustomers = List.of(daniil, klian, giannacoulis, katsifis);
            List<Customer> customersFromMethod = customerService.getCustomersSortedByEmail();
            assertEquals(expectedCustomers, customersFromMethod);
        } catch (InvalidObjectValuesException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

}