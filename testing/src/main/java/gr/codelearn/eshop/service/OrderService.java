package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.Order;
import gr.codelearn.eshop.domain.PaymentMethod;
import gr.codelearn.eshop.domain.Product;
import gr.codelearn.eshop.filter.OrderFilter;

import java.util.List;

public interface OrderService {
	Order initiateOrder(Customer customer);

	List<Order> getOrders();

	List<Order> getOrdersFiltered(OrderFilter orderFilter);

	Order getOrder(Long id);

	void addItem(Order Order, Product product, int quantity);

	void updateItem(Order order, Product product, int quantity);

	void removeItem(Order order, Product product);

	Order checkout(Order order, PaymentMethod paymentMethod);
}
