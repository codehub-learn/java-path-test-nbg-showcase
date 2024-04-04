package gr.codelearn.eshop.filter;

import gr.codelearn.eshop.domain.Order;

/**
 * Interface that is mainly used to filter out reservations in the service layer. This sort of "filtering" can also be
 * managed with queries directly within the repository, or by creating a predicate and applying a filter during the
 * .stream().filter() process of a stream. This interface helps in setting up "predefined" filters without having to
 * rewrite an identical predicate every time.
 */
public interface OrderFilter {
	boolean apply(Order order);
}
