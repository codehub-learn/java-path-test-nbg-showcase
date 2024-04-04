package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.Order;
import gr.codelearn.eshop.domain.Product;

import java.util.List;

public interface DataRepositoryService {
	List<Customer> getCustomers();

	Customer getCustomer(Long id);

	Customer getCustomer(String email);

	boolean save(Customer customer);

	List<Order> getOrders();

	Order getOrder(Long id);

	boolean save(Order order);

	boolean save(Product product);

	List<Product> getProducts();

	Product getProduct(String serial);

	void totalNumberAndCostOfPurchases(Customer customer);

	void totalNumberAndCostOfPurchases(Product product);

	void logData(String command);
}
