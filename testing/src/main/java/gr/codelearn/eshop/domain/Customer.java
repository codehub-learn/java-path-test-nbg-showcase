package gr.codelearn.eshop.domain;

import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable, Comparable<Customer> {
	private Long id;
	private String email;
	private String firstname;
	private String lastname;
	private Integer age;
	private String address;
	private CustomerCategory customerCategory;

	private Customer(Builder builder) {
		setId(builder.id);
		setEmail(builder.email);
		setFirstname(builder.firstname);
		setLastname(builder.lastname);
		setAge(builder.age);
		setAddress(builder.address);
		setCustomerCategory(builder.customerCategory);
	}

	public static Builder builder(String email, CustomerCategory customerCategory) {
		return new Builder(email, customerCategory);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public CustomerCategory getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(CustomerCategory customerCategory) {
		this.customerCategory = customerCategory;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Customer{" + "id=" + id + ", email='" + email + '\'' + ", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' + ", age=" + age + ", address='" + address + '\'' +
				", customerCategory=" + customerCategory + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Customer customer = (Customer) o;
		return id.equals(customer.id) && email.equals(customer.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}

	@Override
	public int compareTo(Customer o) {
		return this.email.compareTo(o.getEmail());
	}

	public static final class Builder {
		private final String email;
		private final CustomerCategory customerCategory;
		private Long id;
		private String firstname;
		private String lastname;
		private String address;
		private Integer age;

		public Builder(String email, CustomerCategory customerCategory) {
			this.email = Objects.requireNonNull(email);
			this.customerCategory = Objects.requireNonNull(customerCategory);
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setFirstname(String firstname) {
			this.firstname = firstname;
			return this;
		}

		public Builder setLastname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public Builder setAddress(String address) {
			this.address = address;
			return this;
		}

		public Builder setAge(Integer age) {
			this.age = age;
			return this;
		}

		public Customer build() {
			return new Customer(this);
		}
	}
}
