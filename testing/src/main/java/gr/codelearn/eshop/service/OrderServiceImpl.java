package gr.codelearn.eshop.service;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.Order;
import gr.codelearn.eshop.domain.OrderItem;
import gr.codelearn.eshop.domain.PaymentMethod;
import gr.codelearn.eshop.domain.Product;
import gr.codelearn.eshop.filter.OrderFilter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl extends AbstractServiceImpl implements OrderService {
	private MailService mailService;

	public OrderServiceImpl() {
	}

	public OrderServiceImpl(MailService mailService) {
		this.mailService = mailService;
	}

	@Override
	public Order initiateOrder(Customer customer) {
		return Order.builder(customer).build();
	}

	@Override
	public List<Order> getOrders() {
		return dataRepositoryService.getOrders();
	}

	@Override
	public List<Order> getOrdersFiltered(OrderFilter orderFilter) {
		List<Order> unfilteredOrders = dataRepositoryService.getOrders();
		return unfilteredOrders.stream().filter(orderFilter::apply).collect(Collectors.toList());
	}

	@Override
	public Order getOrder(Long id) {
		return dataRepositoryService.getOrder(id);
	}

	@Override
	public void addItem(Order order, Product product, int quantity) {
		if (checkNullability(order, product)) return;

		boolean increasedQuantity = false;

		// If product is already contained in the order, don't add it again, just increase the quantity accordingly
		for (OrderItem oi : order.getOrderItems()) {
			if (oi.getSerial().equals(product.getSerial())) {
				oi.setQuantity(oi.getQuantity() + quantity);
				increasedQuantity = true;
				break;
			}
		}

		if (!increasedQuantity) {
			order.add(OrderItem.builder(product.getSerial(), quantity, product.getPrice()).build());
		}

		logger.debug("Product[{}] added to Order[{}]", product, order);
	}

	private boolean checkNullability(Order order, Product product) {
		if (order == null) {
			logger.warn("Order is null.");
			return true;
		}
		if (product == null) {
			logger.warn("Product is null.");
			return true;
		}
		return false;
	}

	@Override
	public void updateItem(Order order, Product product, int quantity) {
		if (checkNullability(order, product)) return;

		order.getOrderItems().removeIf(oi -> oi.getSerial().equals(product.getSerial()));
		order.add(OrderItem.builder(product.getSerial(), quantity, product.getPrice()).build());

		logger.debug("Product[{}] updated in Order[{}]", product, order);
	}

	@Override
	public void removeItem(Order order, Product product) {
		if (checkNullability(order, product)) return;

		order.getOrderItems().removeIf(oi -> oi.getSerial().equals(product.getSerial()));
		logger.debug("Product[{}] removed from Order[{}]", product, order);
	}

	@Override
	public Order checkout(Order order, PaymentMethod paymentMethod) {
		if (!validate(order)) {
			logger.warn("Order should have customer, order items, and payment type defined before being able to be " +
								"checkout the order.");
			return null;
		}

		// Set all order fields with proper values
		order.setPaymentMethod(paymentMethod);
		order.setSubmitDate(new Date());
		order.setCost(giveDiscounts(order));

		dataRepositoryService.save(order);
		if (mailService != null) {
			mailService.send("Order was checked out", "Order details: " + order, order.getCustomer().getEmail());
		}
		return order;
	}

	private boolean validate(Order order) {
		return order != null && !order.getOrderItems().isEmpty() && order.getCustomer() != null;
	}

	private BigDecimal giveDiscounts(Order order) {
		float totalDiscount =
				order.getCustomer().getCustomerCategory().getDiscount() + order.getPaymentMethod().getDiscount();

		// Calculate original order cost
		//@formatter:off
		BigDecimal originalCost = order.getOrderItems().stream()
				.map(oi -> oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		//@formatter:on

		// Apply discount
		BigDecimal finalCost = originalCost.multiply(BigDecimal.valueOf(1f - totalDiscount));

		logger.debug("Order[{}], originalCost: {}, totalDiscount: {}, finalCost: {}.", order.getId(), originalCost,
					 totalDiscount, finalCost);

		return finalCost;
	}
}
