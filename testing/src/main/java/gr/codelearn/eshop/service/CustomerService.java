package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.CustomerCategory;
import gr.codelearn.eshop.exception.InvalidObjectValuesException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface CustomerService {
	boolean register(Customer customer) throws InvalidObjectValuesException, NullPointerException;

	List<Customer> getCustomers();

	List<Customer> getCustomersSortedByEmail();

	List<Customer> getCustomersSorted(Comparator<Customer> criteria);

	Customer getCustomer(Long id);

	Customer getCustomer(String email);

	Map<CustomerCategory, List<Customer>> groupByCustomerCategory();

	<K> Map<K, List<Customer>> groupBy(Function<Customer, K> function);
}
