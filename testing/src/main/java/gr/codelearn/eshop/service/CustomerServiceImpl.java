package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.CustomerCategory;
import gr.codelearn.eshop.exception.InvalidObjectValuesException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomerServiceImpl extends AbstractServiceImpl implements CustomerService {
	@Override
	public boolean register(Customer customer) throws InvalidObjectValuesException, NullPointerException {
		if (customer == null) {
			throw new NullPointerException("Customer must not be null.");
		}
		// Handle potential null values
		if (customer.getFirstname() != null && customer.getFirstname().matches(".*\\d.*")) {
			throw new InvalidObjectValuesException("Customer's firstname should not contain any numbers.");
		}
		if (customer.getLastname() != null && customer.getLastname().matches(".*\\d.*")) {
			throw new InvalidObjectValuesException("Customer's lastname should not contain any numbers.");
		}
		if (customer.getAge() != null && !(customer.getAge() >= 10 && customer.getAge() <= 100)) {
			throw new InvalidObjectValuesException("The customer's age should be between 10 and 100.");
		}
		return dataRepositoryService.save(customer);
	}

	@Override
	public List<Customer> getCustomers() {
		return dataRepositoryService.getCustomers();
	}

	@Override
	public List<Customer> getCustomersSortedByEmail() {
		return this.getCustomersSorted(Comparator.comparing(Customer::getEmail));
	}

	@Override
	public List<Customer> getCustomersSorted(Comparator<Customer> criteria) {
		List<Customer> customers = dataRepositoryService.getCustomers();
		return customers.stream().sorted(criteria).collect(Collectors.toList());
	}

	@Override
	public Customer getCustomer(Long id) {
		return dataRepositoryService.getCustomer(id);
	}

	@Override
	public Customer getCustomer(String email) {
		return dataRepositoryService.getCustomer(email);
	}

	@Override
	public Map<CustomerCategory, List<Customer>> groupByCustomerCategory() {
		return groupBy(Customer::getCustomerCategory);
	}

	@Override
	public <K> Map<K, List<Customer>> groupBy(Function<Customer, K> function) {
		List<Customer> customers = dataRepositoryService.getCustomers();
		return customers.stream().collect(Collectors.groupingBy(function));
	}
}
