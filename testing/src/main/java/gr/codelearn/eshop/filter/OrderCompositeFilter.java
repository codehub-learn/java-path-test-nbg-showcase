package gr.codelearn.eshop.filter;

import gr.codelearn.eshop.domain.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A composite filter that contains a set of other filter within it.
 */
public class OrderCompositeFilter implements OrderFilter {
	private List<OrderFilter> filters = new ArrayList<>();

	@Override
	public boolean apply(Order order) {
		return filters.stream().map(orderFilter -> orderFilter.apply(order)).reduce(true, (b1, b2) -> b1 && b2);
	}

	public void addFilter(OrderFilter... orderFilters) {
		filters.addAll(Arrays.asList(orderFilters));
	}
}
