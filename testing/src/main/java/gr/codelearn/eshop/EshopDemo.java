package gr.codelearn.eshop;

import gr.codelearn.eshop.domain.Customer;
import gr.codelearn.eshop.domain.CustomerCategory;
import gr.codelearn.eshop.domain.Order;
import gr.codelearn.eshop.domain.PaymentMethod;
import gr.codelearn.eshop.domain.Product;
import gr.codelearn.eshop.exception.InvalidObjectValuesException;
import gr.codelearn.eshop.service.*;
import gr.codelearn.eshop.util.PropertiesReader;
import org.h2.message.DbException;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.lang.System.exit;

public class EshopDemo {
	public static Properties sqlCommands;
	private static final Logger logger = LoggerFactory.getLogger(EshopDemo.class);
	private static Server tcpServer, webServer;

	static {
		try {
			PropertiesReader propertiesReader = new PropertiesReader("sql.properties");
			//load a properties file from class path, inside static method
			sqlCommands = propertiesReader.getProperties();
		} catch (IOException ex) {
			logger.error("Sorry, unable to find sql.properties, exiting application.", ex);
			// Abnormal exit
			exit(-1);
		}
	}

	private final CustomerService customerService = new CustomerServiceImpl();
	private final MailService mailService = new MailServiceImpl();
	private final OrderService orderService = new OrderServiceImpl(mailService);
	private final ProductService productService = new ProductServiceImpl();
	private final ReportService reportService = new ReportServiceImpl();

	public EshopDemo() {
		// Create required database structure
		if (createStructure()) {
			// load and print sample products created
			loadSampleProducts();
		}
		logger.info("Initial database content is ready.");
	}

	private boolean createStructure() {
		try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement()) {
			//@formatter:off
			runCommands(statement,
						sqlCommands.getProperty("create.table.001"),
						sqlCommands.getProperty("create.table.002"),
						sqlCommands.getProperty("create.table.003"),
						sqlCommands.getProperty("create.table.004"),
						sqlCommands.getProperty("create.index.001"),
						sqlCommands.getProperty("create.index.002"));
			//@formatter:on

			return true;
		} catch (SQLException ex) {
			logger.warn("Error while creating table(s), {}.", ex.getMessage());

			// Most probably the tables already exist.
			return false;
		}
	}

	private void loadSampleProducts() {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 sqlCommands.getProperty("insert.table.product.000"))) {
			for (Product product : generateProducts()) {
				preparedStatement.clearParameters();

				preparedStatement.setString(1, product.getSerial());
				preparedStatement.setString(2, product.getName());
				preparedStatement.setBigDecimal(3, product.getPrice());

				// Add command to batch
				preparedStatement.addBatch();
			}

			int[] rowsAffectedArray = preparedStatement.executeBatch();
			logger.info("Insert batch command were successful with {} row(s) affected.", Arrays.stream(
					rowsAffectedArray).summaryStatistics().getSum());
		} catch (SQLException ex) {
			logger.error("Error while inserting data.", ex);
		}
	}

	private void runCommands(Statement statement, String... commands) throws SQLException {
		for (String command : commands) {
			logger.debug("'{}' command was successful with {} row(s) affected.", command,
						 statement.executeUpdate(command));
		}
	}

	private List<Product> generateProducts() {
		//@formatter:off
		List<Product> products = Arrays.asList(
			Product.builder("SN1000-0001", "Apple iPhone 12 Pro 5G 512GB", BigDecimal.valueOf(1629)).build(),
			Product.builder("SN1000-0002", "Apple iPhone 12 Pro Max 5G 512GB", BigDecimal.valueOf(1749)).build(),
			Product.builder("SN1100-0001", "Samsung Galaxy S21 Ultra", BigDecimal.valueOf(1479.99)).build(),
			Product.builder("SN1100-0002", "Samsung Galaxy S20 Ultra", BigDecimal.valueOf(1199)).build(),
			Product.builder("SN1200-0001", "Huawei P40 Pro", BigDecimal.valueOf(899.49)).build(),
			Product.builder("SN1300-0001", "Xiaomi Redmi 9A", BigDecimal.valueOf(199.75)).build(),
			Product.builder("SN1400-0001", "RealMe C11", BigDecimal.valueOf(129)).build(),
			Product.builder("SN1500-0001", "Honor 10 Lite", BigDecimal.valueOf(179)).build(),
			Product.builder("SN1000-0003", "Apple iPhone 12 Pro Max 5G 128GB", BigDecimal.valueOf(1339)).build(),
			Product.builder("SN1000-0004", "Apple iPhone 11 Pro 256GB", BigDecimal.valueOf(1299.99)).build());
		//@formatter:on

		return products;
	}

	public static void main(String[] args) {
		// Start database server
		startH2Server();

		logger.info("Application is starting...");

		EshopDemo demo = new EshopDemo();
		demo.runStep1Scenario();
		demo.runStep2Scenario();
		demo.runStep3Scenario();

		// Shutdown database server when application exits
		Runtime.getRuntime().addShutdownHook(new Thread(() -> stopH2Server()));

		// This will allow us to keep database server alive in order to visit its web query editor
		while (true) {
		}
	}

	private static void startH2Server() {
		try {
			tcpServer = Server.createTcpServer("-tcpAllowOthers", "-tcpDaemon");
			tcpServer.start();

			webServer = Server.createWebServer("-webAllowOthers", "-webDaemon");
			webServer.start();
		} catch (SQLException e) {
			//
			logger.error("Error while starting H2 server.", DbException.convert(e));
			exit(-1);
		}
		logger.info("H2 server has started with status '{}'.", tcpServer.getStatus());
	}

	private void runStep1Scenario() {
		// Get all products
		productService.getProducts().forEach(p -> logger.info("{}", p));

		// Create 3 new customers
		Customer me = Customer.builder("c.giannacoulis@codehub.gr", CustomerCategory.INDIVIDUAL).setFirstname(
				"Constantinos").setLastname("Giannacoulis").setAge(47).build();
		Customer someone = Customer.builder("someone@gmail.com", CustomerCategory.BUSINESS).setFirstname("John")
				.setLastname("Porter").setAge(40).build();
		Customer someoneElse = Customer.builder("someone.else@gmail.com", CustomerCategory.GOVERNMENT).setFirstname(
				"Malcolm").setLastname("Parker").setAge(40).build();
		Customer businessCustomer = Customer.builder("business@somedomain.com", CustomerCategory.BUSINESS).setFirstname(
				"Terry").setLastname("Jones").setAge(55).build();

		try {
			logger.info("Customer registration status is {}. {}", customerService.register(me), me);
			logger.info("Customer registration status is {}. {}", customerService.register(someone), someone);
			logger.info("Customer registration status is {}. {}", customerService.register(someoneElse), someoneElse);
			logger.info("Customer registration status is {}. {}", customerService.register(businessCustomer),
						someoneElse);
		} catch (InvalidObjectValuesException | NullPointerException e) {
			logger.info("Invalid customer credentials: {}", e.getMessage());
		}

		// Get all customers
		customerService.getCustomers().forEach(c -> logger.info("{}", c));

		// Get customer with id 1
		logger.info("Customer corresponding to id {}: {}", 1L, customerService.getCustomer(1L));
		// Get customer with email
		logger.info("Customer corresponding to email {}: {}", "someone.else@gmail.com",
					customerService.getCustomer("someone.else@gmail.com"));
		// Get customer with non-existent email
		logger.info("Customer corresponding to email {}: {}", "someone.else.not@gmail.com",
					customerService.getCustomer("someone_else.not@gmail.com"));
	}

	private void runStep2Scenario() {
		// Get a customer
		Customer me = customerService.getCustomer("c.giannacoulis@codehub.gr");
		// Initiate an order
		Order myOrder = orderService.initiateOrder(me);
		// Add item(s) both existing and non-existing
		orderService.addItem(myOrder, productService.getProduct("SN1000-0001"), 2);
		orderService.addItem(myOrder, productService.getProduct("SN1100-0001"), 1);
		orderService.addItem(myOrder, productService.getProduct("SN1000-0008"), 1);
		orderService.addItem(myOrder, productService.getProduct("SN1000-0004"), 1);
		// Update item(s)
		orderService.addItem(myOrder, productService.getProduct("SN1000-0001"), 1);
		orderService.updateItem(myOrder, productService.getProduct("SN1000-0004"), 2);
		// Remove item(s)
		orderService.removeItem(myOrder, productService.getProduct("SN1100-0001"));

		logger.info("{}", myOrder);
		// Add some more item(s)
		orderService.addItem(myOrder, productService.getProduct("SN1300-0001"), 2);
		// Checkout order
		orderService.checkout(myOrder, PaymentMethod.CREDIT_CARD);

		Customer someone = customerService.getCustomer("someone@gmail.com");
		// Initiate an second order
		Order secondOrder = orderService.initiateOrder(someone);
		// Add item(s) to second order
		orderService.addItem(secondOrder, productService.getProduct("SN1000-0002"), 1);
		orderService.addItem(secondOrder, productService.getProduct("SN1200-0001"), 1);
		orderService.addItem(secondOrder, productService.getProduct("SN1200-0001"), 1);
		// Checkout 2nd order
		orderService.checkout(secondOrder, PaymentMethod.WIRE_TRANSFER);

		// Add a third order
		Customer someoneElse = customerService.getCustomer("someone.else@gmail.com");
		Order thirdOrder = orderService.initiateOrder(someoneElse);
		orderService.addItem(thirdOrder, productService.getProduct("SN1000-0001"), 3);
		orderService.addItem(thirdOrder, productService.getProduct("SN1000-0002"), 2);
		orderService.addItem(thirdOrder, productService.getProduct("SN1000-0003"), 1);
		orderService.checkout(thirdOrder, PaymentMethod.CREDIT_CARD);

		// Add a fourth order
		Customer businessCustomer = customerService.getCustomer("business@somedomain.com");
		Order fourthOrder = orderService.initiateOrder(businessCustomer);
		orderService.addItem(fourthOrder, productService.getProduct("SN1300-0001"), 1);
		orderService.addItem(fourthOrder, productService.getProduct("SN1400-0001"), 2);
		orderService.addItem(fourthOrder, productService.getProduct("SN1500-0001"), 1);
		orderService.addItem(fourthOrder, productService.getProduct("SN1000-0003"), 1);
		orderService.addItem(fourthOrder, productService.getProduct("SN1000-0004"), 1);
		orderService.checkout(fourthOrder, PaymentMethod.CREDIT_CARD);

		// Get all reservations
		orderService.getOrders().forEach(o -> logger.info("{}", o));
	}

	private void runStep3Scenario() {
		reportService.totalNumberAndCostOfPurchases(customerService.getCustomer("c.giannacoulis@codehub.gr"));
		// Try a non-existent customer
		reportService.totalNumberAndCostOfPurchases(customerService.getCustomer("non.existent.email@gmail.com"));
		reportService.totalNumberAndCostOfPurchases(productService.getProduct("SN1300-0001"));
		// Try a non-existent product
		reportService.totalNumberAndCostOfPurchases(productService.getProduct("SN1300-00011"));
		reportService.totalNumberAndCostOfPurchasesPerCustomer();
		reportService.totalNumberAndCostOfPurchasesPerCustomerCategory();
		reportService.totalNumberAndCostOfPurchasesPerPaymentMethod();
		reportService.averageOrderCost();
		reportService.averageOrderCostPerCustomer();
		reportService.getCustomersPurchasedMostExpensiveProduct();
	}

	private static void stopH2Server() {
		if (tcpServer == null || webServer == null) {
			return;
		}
		if (tcpServer.isRunning(true)) {
			tcpServer.stop();
			tcpServer.shutdown();
		}
		if (webServer.isRunning(true)) {
			webServer.stop();
			webServer.shutdown();
		}
		logger.info("H2 server has been shutdown.");
	}
}
