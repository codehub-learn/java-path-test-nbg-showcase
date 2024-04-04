package gr.codelearn.eshop.filter;

import gr.codelearn.eshop.domain.Order;
import lombok.Data;

/**
 * Filter that simulates a custom mocking mechanism. Mainly used to detect if the filter has been invoked or not
 */
@Data
public class OrderMockedFilter implements OrderFilter {
	private boolean invoked;

	@Override
	public boolean apply(Order order) {
		invoked = true;
		return true;
	}
}
