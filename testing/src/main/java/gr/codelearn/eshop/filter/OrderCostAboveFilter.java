package gr.codelearn.eshop.filter;

import gr.codelearn.eshop.domain.Order;

import java.math.BigDecimal;

/**
 * Filter that checks if an order has an accumulated cost above the one specified in the filter
 */
public class OrderCostAboveFilter implements OrderFilter {
	private BigDecimal cost;

	public OrderCostAboveFilter(int cost) {
		this.cost = new BigDecimal(cost);
	}

	@Override
	public boolean apply(Order order) {
		if (order == null) return false;
		return order.getCost().compareTo(cost) >= 0;
	}
}
