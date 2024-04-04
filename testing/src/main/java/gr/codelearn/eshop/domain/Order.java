package gr.codelearn.eshop.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
	private long id;
	private Customer customer;
	private Date submitDate;
	private List<OrderItem> orderItems;
	private PaymentMethod paymentMethod;
	private BigDecimal cost;

	private Order(Builder builder) {
		setId(builder.id);
		setCustomer(builder.customer);
		setSubmitDate(builder.submitDate);
		setOrderItems(builder.orderItems);
		setPaymentMethod(builder.paymentMethod);
		setCost(builder.cost);
	}

	public static Builder builder(Customer customer) {
		return new Builder(customer);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public void add(OrderItem orderItem) {
		orderItems.add(orderItem);
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Order{" + "id=" + id + ", customer=" + customer + ", submitDate=" + submitDate + ", orderItems=" +
				orderItems + ", paymentMethod=" + paymentMethod + ", cost=" + cost + '}';
	}

	public static final class Builder {
		private final Customer customer;
		private long id;
		private Date submitDate;
		private List<OrderItem> orderItems = new ArrayList<>();
		private PaymentMethod paymentMethod;
		private BigDecimal cost;

		public Builder(Customer customer) {
			this.customer = Objects.requireNonNull(customer);
		}

		public Builder setId(long id) {
			this.id = id;
			return this;
		}

		public Builder setSubmitDate(Date submitDate) {
			this.submitDate = submitDate;
			return this;
		}

		public Builder setOrderItems(List<OrderItem> orderItems) {
			this.orderItems = orderItems;
			return this;
		}

		public Builder setPaymentMethod(PaymentMethod paymentMethod) {
			this.paymentMethod = paymentMethod;
			return this;
		}

		public Builder setCost(BigDecimal cost) {
			this.cost = cost;
			return this;
		}

		public Order build() {
			return new Order(this);
		}
	}
}
