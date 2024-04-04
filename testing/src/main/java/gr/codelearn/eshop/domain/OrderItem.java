package gr.codelearn.eshop.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {
	private Long id;
	private String serial;
	private Integer quantity;
	private BigDecimal price;

	private OrderItem(Builder builder) {
		setId(builder.id);
		setSerial(builder.serial);
		setQuantity(builder.quantity);
		setPrice(builder.price);
	}

	public static Builder builder(String serial, Integer quantity, BigDecimal price) {
		return new Builder(serial, quantity, price);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderItem{" + "id=" + id + ", serial='" + serial + '\'' + ", quantity=" + quantity + ", price=" +
				price + '}';
	}

	public static final class Builder {
		private final String serial;
		private final Integer quantity;
		private final BigDecimal price;
		private Long id;

		private Builder(String serial, Integer quantity, BigDecimal price) {
			this.serial = Objects.requireNonNull(serial);
			this.quantity = Objects.requireNonNull(quantity);
			this.price = Objects.requireNonNull(price);
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public OrderItem build() {
			return new OrderItem(this);
		}
	}
}
