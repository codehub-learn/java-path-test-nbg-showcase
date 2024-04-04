package gr.codelearn.eshop.filter;

import gr.codelearn.eshop.domain.Order;
import gr.codelearn.eshop.domain.PaymentMethod;

/**
 * Filter that filters a list of reservations based on the payment method
 */
public class OrderPaymentMethodFilter implements OrderFilter {
	private PaymentMethod paymentMethod;

	public OrderPaymentMethodFilter(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Override
	public boolean apply(Order order) {
		if (order == null) return false;
		return order.getPaymentMethod().equals(paymentMethod);
	}
}
