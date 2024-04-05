package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.CustomerCategory;
import gr.codelearn.eshop.exception.InvalidObjectValuesException;
import gr.codelearn.eshop.service.base.ExceptionLoggingAndTestSummary;
import gr.codelearn.eshop.service.extension.CustomerParameterResolver;
import gr.codelearn.eshop.service.extension.DatabaseManager;
import gr.codelearn.eshop.service.extension.TestSummary;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A customer service")
@ExtendWith(DatabaseManager.class)
@ExtendWith(CustomerParameterResolver.class)
@ExceptionLoggingAndTestSummary
class CustomerServiceTest {

    List<Customer> customerList;
    CustomerService customerService;

    public CustomerServiceTest(Map<String, Customer> customerMap) {
        customerList = Arrays.asList(
                customerMap.get("Ioannis Daniil"),
                customerMap.get("Ioannis Klian"),
                customerMap.get("Constantinos Giannacoulis") ,
                customerMap.get("Meletis Katsifis")
        );
        customerService = new CustomerServiceImpl();
    }

    @Test
    @DisplayName("sorts customers by default order (lexicographically by email)")
    void sortsByEmail() {
        try {
            for (Customer customer : customerList) {
                customerService.register(customer);
            }
            Customer daniil = customerList.get(0);
            Customer klian = customerList.get(1);
            Customer giannacoulis = customerList.get(2);
            Customer katsifis = customerList.get(3);
            List<Customer> expectedCustomers = List.of(daniil, klian, giannacoulis, katsifis);
            List<Customer> customersFromMethod = customerService.getCustomersSortedByEmail();
            assertEquals(expectedCustomers, customersFromMethod);
        } catch (InvalidObjectValuesException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("successfully groups by customer category")
    void groupsByCustomerCategory(){
        try {
            for (Customer customer : customerList) {
                customerService.register(customer);
            }
            Map<CustomerCategory, List<Customer>> map = customerService.groupByCustomerCategory();
            Customer daniil = customerList.get(0);
            Customer klian = customerList.get(1);
            Customer giannacoulis = customerList.get(2);
            Customer katsifis = customerList.get(3);
            assertAll(
                    () ->  assertThat(map).contains(entry(CustomerCategory.INDIVIDUAL, List.of(klian, katsifis))),
                    () ->  assertThat(map).contains(entry(CustomerCategory.BUSINESS, List.of(giannacoulis))),
                    () ->  assertThat(map).contains(entry(CustomerCategory.GOVERNMENT, List.of(daniil)))
            );
        } catch (InvalidObjectValuesException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("successfully sorted by customer age")
    void sortsByCustomerAge(){
        try {
            for (Customer customer : customerList) {
                customerService.register(customer);
            }
            List<Customer> sortedByAgeList = customerService
                    // just for showcase, normally database would already have filter/sorting on the query
                    .getCustomersSorted(Comparator.comparing(Customer::getAge));
            assertThat(sortedByAgeList).isSortedAccordingTo(Comparator.comparing(Customer::getAge));
        } catch (InvalidObjectValuesException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("find the correct customer when searched by email")
    void repositoryFetchesCorrectlyByEmailWhenCustomerRegisters() {
        try {
            customerService.register(customerList.get(1));
            Customer customer = customerService.getCustomer(customerList.get(1).getEmail());
            assertThat(customer.getEmail()).withFailMessage("E-mails should be the same.")
                    .isEqualTo("iklian@gmail.com");
            //assertEquals("iklian@gmail.com", customer.getEmail(), "E-mails should be the same.");
        } catch (InvalidObjectValuesException | NullPointerException e) {
            fail();
            throw new RuntimeException(e);
        }
    }
}