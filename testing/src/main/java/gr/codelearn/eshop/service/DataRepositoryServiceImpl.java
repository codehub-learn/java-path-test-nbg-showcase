package gr.codelearn.eshop.service;

import gr.codelearn.eshop.DataSource;
import gr.codelearn.eshop.EshopDemo;
import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.CustomerCategory;
import gr.codelearn.eshop.domain.Order;
import gr.codelearn.eshop.domain.OrderItem;
import gr.codelearn.eshop.domain.PaymentMethod;
import gr.codelearn.eshop.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataRepositoryServiceImpl implements DataRepositoryService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Override
	public boolean save(Customer customer) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("insert.table.customer.000"), Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, customer.getEmail());

			// Handle potential null values
			if (customer.getFirstname() != null) {
				preparedStatement.setString(2, customer.getFirstname());
			} else {
				preparedStatement.setNull(2, Types.VARCHAR);
			}
			if (customer.getLastname() != null) {
				preparedStatement.setString(3, customer.getLastname());
			} else {
				preparedStatement.setNull(3, Types.VARCHAR);
			}
			if (customer.getAge() != null) {
				preparedStatement.setInt(4, customer.getAge());
			} else {
				preparedStatement.setNull(4, Types.INTEGER);
			}
			if (customer.getAddress() != null) {
				preparedStatement.setString(5, customer.getAddress());
			} else {
				preparedStatement.setNull(5, Types.VARCHAR);
			}

			preparedStatement.setString(6, customer.getCustomerCategory().name());

			int rowsAffected = preparedStatement.executeUpdate();
			try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
				if (keys.next()) {
					customer.setId(keys.getLong(1));
				}
			}
			logger.debug("Insert command was successful with returning customer {}.", customer);
			return true;
		} catch (SQLException ex) {
			logger.error("Error while inserting customer {}.", customer, ex);
			return false;
		}
	}

	@Override
	public List<Customer> getCustomers() {
		try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement();
			 ResultSet results = statement.executeQuery(
					 EshopDemo.sqlCommands.getProperty("select.table.customer.000"))) {

			List<Customer> customers = new ArrayList<>();
			while (results.next()) {
				//@formatter:off
				customers.add(Customer.builder(results.getString("email"),
											   CustomerCategory.valueOf(results.getString("category")))
									  .setId(results.getLong("id"))
									  .setFirstname(results.getString("firstname"))
									  .setLastname(results.getString("lastname"))
									  .setAge(results.getInt("age"))
									  .setAddress(results.getString("address"))
									  .build());
				//@formatter:on
			}

			logger.debug("Retrieved {} customers.", customers.size());

			return customers;
		} catch (SQLException ex) {
			logger.error("Error while retrieving all customers.", ex);
		}
		// Instead of delegating the error by bubbling up the exception via method signature, we are handling it
		// locally and return an empty list
		return Collections.emptyList();
	}

	@Override
	public Customer getCustomer(String email) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("select.table.customer.002"))) {

			preparedStatement.setString(1, email);

			return getCustomer(preparedStatement);
		} catch (SQLException ex) {
			logger.error("Error while retrieving Customer['{}'].", email, ex);
		}
		return null;
	}

	private Customer getCustomer(PreparedStatement preparedStatement) throws SQLException {
		try (ResultSet resultSet = preparedStatement.executeQuery()) {
			if (resultSet.next()) {
				//@formatter:off
				Customer customer = Customer.builder(resultSet.getString("email"),
													 CustomerCategory.valueOf(resultSet.getString("category")))
						.setId(resultSet.getLong("id"))
						.setFirstname(resultSet.getString("firstname"))
						.setLastname(resultSet.getString("lastname"))
						.setAge(resultSet.getInt("age"))
						.setAddress(resultSet.getString("address")).build();
				//@formatter:on
				logger.debug("Retrieved {}.", customer);

				return customer;
			}
		}
		return null;
	}

	@Override
	public List<Order> getOrders() {
		try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement();
			 ResultSet results = statement.executeQuery(EshopDemo.sqlCommands.getProperty("select.table.order.000"))) {

			List<Order> reservations = new ArrayList<>();
			while (results.next()) {
				//@formatter:off
				Order order = Order.builder(getCustomer(results.getLong("customer_id")))
								   .setId(results.getLong("id"))
								   .setSubmitDate(results.getDate("submit_date"))
								   .setPaymentMethod(PaymentMethod.valueOf(results.getString("payment_method")))
								   .setCost(results.getBigDecimal("cost"))
								   .build();
				//@formatter:on

				getOrderItems(order);

				reservations.add(order);
			}

			logger.debug("Retrieved {} reservations.", reservations.size());

			return reservations;
		} catch (SQLException ex) {
			logger.error("Error while retrieving all reservations.", ex);
		}
		// Instead of delegating the error by bubbling up the exception via method signature, we are handling it
		// locally and return an empty list
		return Collections.emptyList();
	}

	@Override
	public Customer getCustomer(Long id) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("select.table.customer.001"))) {

			preparedStatement.setLong(1, id);

			return getCustomer(preparedStatement);
		} catch (SQLException ex) {
			logger.error("Error while retrieving Customer[{}].", id, ex);
		}
		return null;
	}

	private void getOrderItems(Order order) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("select.table.orderitem.001"))) {
			preparedStatement.setLong(1, order.getId());

			try (ResultSet innerResults = preparedStatement.executeQuery()) {
				while (innerResults.next()) {
					//@formatter:off
					order.add(OrderItem.builder(innerResults.getString("serial"),
												innerResults.getInt("quantity"),
												innerResults.getBigDecimal("price"))
									  .setId(innerResults.getLong("id"))
									  .build());
					//@formatter:on
				}
			}
		} catch (SQLException ex) {
			logger.error("Error while retrieving order items for Order[{}].", order.getId(), ex);
		}
	}

	@Override
	public Order getOrder(Long id) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("select.table.order.001"))) {

			preparedStatement.setLong(1, id);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					//@formatter:off
					Order order = Order.builder(getCustomer(resultSet.getLong("customer_id")))
							.setId(resultSet.getLong("id"))
							.setSubmitDate(resultSet.getDate("submit_date"))
							.setPaymentMethod(PaymentMethod.valueOf(resultSet.getString("payment_method")))
							.setCost(resultSet.getBigDecimal("cost"))
							.build();
					//@formatter:on

					getOrderItems(order);
					logger.debug("Retrieved {}.", order);

					return order;
				}
			}
		} catch (SQLException ex) {
			logger.error("Error while retrieving order[{}].", id, ex);
		}
		return null;
	}

	@Override
	public boolean save(Order order) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("insert.table.order.000"), Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setLong(1, order.getCustomer().getId());
			preparedStatement.setTimestamp(2, new Timestamp(order.getSubmitDate().getTime()));
			preparedStatement.setString(3, order.getPaymentMethod().name());
			preparedStatement.setBigDecimal(4, order.getCost());

			int rowsAffected = preparedStatement.executeUpdate();
			try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
				if (keys.next()) {
					order.setId(keys.getLong(1));
					for (OrderItem orderItem : order.getOrderItems()) {
						save(orderItem, order.getId());
					}
				}
			}

			logger.debug("Insert command was successful with returning order {}.", order);

			return true;
		} catch (SQLException ex) {
			logger.error("Error while inserting order {}.", order, ex);
			return false;
		}
	}

	@Override
	public boolean save(Product product) {
		// Potential problems: 1) db does not manage product ids 2) nullability of serials without check
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("insert.table.product.000"), Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setString(1, product.getSerial());

			// Handle potential null values
			if (product.getName() != null) {
				preparedStatement.setString(2, product.getName());
			} else {
				preparedStatement.setNull(2, Types.VARCHAR);
			}
			if (product.getPrice() != null) {
				preparedStatement.setBigDecimal(3, product.getPrice());
			} else {
				preparedStatement.setNull(3, Types.DECIMAL);
			}

			logger.debug("Insert command was successful with returning product {}.", product);
			int rowsAffected = preparedStatement.executeUpdate();
			return true;
		} catch (SQLException ex) {
			logger.error("Error while inserting product {}.", product, ex);
			return false;
		}
	}

	private void save(OrderItem orderItem, Long orderId) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("insert.table.orderitem.000"))) {

			preparedStatement.setLong(1, orderId);
			preparedStatement.setString(2, orderItem.getSerial());
			preparedStatement.setInt(3, orderItem.getQuantity());
			preparedStatement.setBigDecimal(4, orderItem.getPrice());

			int rowsAffected = preparedStatement.executeUpdate();

			logger.debug("Insert command was successful with returning orderItem {}.", orderItem);
		} catch (SQLException ex) {
			logger.error("Error while inserting orderItem {}.", orderItem, ex);
		}
	}

	@Override
	public List<Product> getProducts() {
		try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement();
			 ResultSet results = statement.executeQuery(
					 EshopDemo.sqlCommands.getProperty("select.table.product.000"))) {

			List<Product> products = new ArrayList<>();
			while (results.next()) {
				//@formatter:off
				products.add(Product.builder(results.getString("serial"),
									  results.getString("name"),
									  results.getBigDecimal("price"))
									 .build());
				//@formatter:on
			}

			logger.debug("Retrieved {} products.", products.size());

			return products;
		} catch (SQLException ex) {
			logger.error("Error while retrieving all products.", ex);
		}
		// Instead of delegating the error by bubbling up the exception via method signature, we are handling it
		// locally and return an empty list
		return Collections.emptyList();
	}

	@Override
	public Product getProduct(String serial) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("select.table.product.001"))) {

			preparedStatement.setString(1, serial);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					//@formatter:off
					Product product = Product.builder(resultSet.getString("serial"),
													  resultSet.getString("name"),
													  resultSet.getBigDecimal("price")).build();
					//@formatter:on
					logger.debug("Retrieved {}.", product);

					return product;
				}
			}
			return null;
		} catch (SQLException ex) {
			logger.error("Error while retrieving Product['{}'].", serial, ex);
		}
		return null;
	}

	@Override
	public void totalNumberAndCostOfPurchases(Customer customer) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("reports.001"))) {

			preparedStatement.setLong(1, customer.getId());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				logResults(resultSet);
			}
		} catch (SQLException ex) {
			logger.error("Error while logging data", ex);
		}
	}

	@Override
	public void totalNumberAndCostOfPurchases(Product product) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 EshopDemo.sqlCommands.getProperty("reports.002"))) {

			preparedStatement.setString(1, product.getSerial());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				logResults(resultSet);
			}
		} catch (SQLException ex) {
			logger.error("Error while logging data", ex);
		}
	}

	@Override
	public void logData(String command) {
		try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(command)) {

			logResults(resultSet);

		} catch (SQLException ex) {
			logger.error("Error while logging data.", ex);
		}
	}

	private void logResults(ResultSet resultSet) throws SQLException {
		logger.info("---------------------------------------------------------------------");
		int rowCount = 1;
		while (resultSet.next()) {
			StringBuilder rowContent = new StringBuilder();
			for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
				rowContent.append(resultSet.getMetaData().getColumnName(i)).append(":").append(resultSet.getString(i));
				rowContent.append(i == resultSet.getMetaData().getColumnCount() ? "." : ", ");
			}
			logger.info("{}. {}", rowCount++, rowContent);
		}
		logger.info("---------------------------------------------------------------------");
	}
}
