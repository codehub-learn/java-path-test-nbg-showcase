package gr.codelearn.rentbnb;

import gr.codelearn.rentbnb.domain.Guest;
import gr.codelearn.rentbnb.domain.Host;
import gr.codelearn.rentbnb.domain.PaymentMethod;
import gr.codelearn.rentbnb.domain.Property;
import gr.codelearn.rentbnb.domain.Reservation;
import gr.codelearn.rentbnb.exception.InvalidDateOrderException;
import gr.codelearn.rentbnb.exception.InvalidObjectValuesException;
import gr.codelearn.rentbnb.exception.ReservationAvailabilityException;
import gr.codelearn.rentbnb.service.DataRepositoryService;
import gr.codelearn.rentbnb.service.GuestService;
import gr.codelearn.rentbnb.service.HostService;
import gr.codelearn.rentbnb.service.MailService;
import gr.codelearn.rentbnb.service.PaymentService;
import gr.codelearn.rentbnb.service.ReservationService;
import gr.codelearn.rentbnb.service.impl.DataRepositoryServiceImpl;
import gr.codelearn.rentbnb.service.impl.GuestServiceImpl;
import gr.codelearn.rentbnb.service.impl.HostServiceImpl;
import gr.codelearn.rentbnb.service.impl.MailServiceImpl;
import gr.codelearn.rentbnb.service.impl.PaymentServiceImpl;
import gr.codelearn.rentbnb.service.impl.ReservationServiceImpl;
import gr.codelearn.rentbnb.util.PropertiesReader;
import org.h2.message.DbException;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static java.lang.System.exit;

public class RentBnbDemo {
	public static Properties sqlCommands;
	private static final Logger logger = LoggerFactory.getLogger(RentBnbDemo.class);
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

	private final DataRepositoryService dataRepositoryService = new DataRepositoryServiceImpl();
	private final MailService mailService = new MailServiceImpl();
	private final PaymentService paymentService = new PaymentServiceImpl();
	private final GuestService guestService = new GuestServiceImpl(dataRepositoryService);
	private final HostService hostService = new HostServiceImpl(dataRepositoryService);
	private final ReservationService reservationService = new ReservationServiceImpl(dataRepositoryService,
																					 guestService, mailService,
																					 paymentService);

	public RentBnbDemo() {
		// Create required database structure
		createStructure();
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

	private void runCommands(Statement statement, String... commands) throws SQLException {
		for (String command : commands) {
			logger.debug("'{}' command was successful with {} row(s) affected.", command,
						 statement.executeUpdate(command));
		}
	}

	public static void main(String[] args) {
		// Start database server
		startH2Server();

		logger.info("Application is starting...");

		RentBnbDemo demo = new RentBnbDemo();
		demo.runStep1Scenario();
		demo.runStep2Scenario();
		demo.runStep3Scenario();
		demo.runStep4Scenario();

		logger.info("|----------------------- End of scenarios -----------------------|");

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
		logger.info("|----------------------- Scenario 1 -----------------------|");
		// Create 3 new guests
		Guest guest1 = Guest.builder("j.gosling@codehub.gr", new Date("05/19/1955")).firstname("James").lastname(
				"Gosling").address("Java Street. 11").build();

		Guest guest2 = Guest.builder("l.torvalds@codehub.gr", new Date("12/28/1969")).firstname("Linus").lastname(
				"Torvalds").address("Linux Kernel 100").build();

		Guest guest3 = Guest.builder("t.berners@codehub.gr", new Date("06/08/1955")).firstname("Tim").lastname(
				"Berners-Lee").address("Cern 30").build();

		try {
			logger.info("Guest registration status is {}. {}", guestService.register(guest1), guest1);
			logger.info("Guest registration status is {}. {}", guestService.register(guest2), guest2);
			logger.info("Guest registration status is {}. {}", guestService.register(guest3), guest3);
		} catch (InvalidObjectValuesException | NullPointerException e) {
			logger.info("Invalid guest: {}", e.getMessage());
		}

		// Get all guests
		guestService.getGuests().forEach(g -> logger.info("{}", g));

		// Get guest with id 1
		logger.info("Guest corresponding to id {}: {}", 1L, guestService.getGuest(1L));
		// Get guest with email
		logger.info("Guest corresponding to email {}: {}", "j.gosling@codehub.gr",
					guestService.getGuest("j.gosling@codehub.gr"));
		// Get guest with non-existent email
		logger.info("Guest corresponding to email {}: {}", "someone@gmail.com",
					guestService.getGuest("someone@gmail.com"));
	}

	private void runStep2Scenario() {
		logger.info("|----------------------- Scenario 2 -----------------------|");
		Host host1 = Host.builder("j.gosling@codehub.gr", "James", "Gosling").dateOfBirth(new Date("05/19/1955"))
				.build();
		List<Property> propertyList = new ArrayList<>();
		Property property1 = Property.builder("Ikaria, Armenistis 12", BigDecimal.valueOf(40)).build();
		Property property2 = Property.builder("Barcelona, Barceloneta 41", BigDecimal.valueOf(105)).build();
		propertyList.add(property1);
		propertyList.add(property2);
		host1.setPropertyList(propertyList);

		Host host2 = Host.builder("l.torvalds@codehub.gr", "Linus", "Torvalds").dateOfBirth(new Date("12/28/1969"))
				.build();

		Host host3 = Host.builder("jt.berners@codehub.gr", "Tim", "Berners-Lee").dateOfBirth(new Date("06/08/1955"))
				.build();

		Property property3 = Property.builder("Madrid, Placa-De-Mayor 1", BigDecimal.valueOf(200)).build();

		try {
			logger.info("Host registration status is {}. {}", hostService.register(host1), host1);
			logger.info("Host registration status is {}. {}", hostService.register(host2), host2);
			logger.info("Host registration status is {}. {}", hostService.register(host3), host3);
		} catch (InvalidObjectValuesException | NullPointerException e) {
			logger.info("Invalid host: {}", e.getMessage());
		}

		// Get all hosts
		hostService.getHosts().forEach(h -> logger.info("{}", h));

		try {
			logger.info("Host - property registration status is {}. {}", hostService.registerProperty(host3, property3),
						host3);
		} catch (InvalidObjectValuesException | NullPointerException e) {
			logger.info("Invalid host: {}", e.getMessage());
		}

		// Get host with id 3
		logger.info("Host corresponding to id {}: {}", 3L, hostService.getHost(3L));
	}

	private void runStep3Scenario() {
		logger.info("|----------------------- Scenario 3 -----------------------|");
		// Create 1 new guest
		Guest guest1 = Guest.builder("g1s3.guest@codehub.gr", new Date("05/19/2000")).firstname("Guest one").lastname(
				"scenario three").address("Guest 1").build();
		try {
			logger.info("Guest registration status is {}. {}", guestService.register(guest1), guest1);
		} catch (InvalidObjectValuesException | NullPointerException e) {
			logger.info("Invalid guest: {}", e.getMessage());
		}

		Host host1 = Host.builder("h1s3.host@codehub.gr", "Host one", "scenario 3").dateOfBirth(new Date("06/08/2000"))
				.build();

		Property property1 = Property.builder("Portugal, Cabo-Da-Roca 1", BigDecimal.valueOf(50)).build();

		List<Property> propertyList = new ArrayList<>();
		propertyList.add(property1);
		host1.setPropertyList(propertyList);

		try {
			logger.info("Host registration status is {}. {}", hostService.register(host1), host1);
		} catch (InvalidObjectValuesException | NullPointerException e) {
			logger.info("Invalid host: {}", e.getMessage());
		}

		Reservation reservation = Reservation.builder(guest1, property1).checkIn(new Date("08/15/2021")).checkOut(
				new Date("08/20/2021")).paymentMethod(PaymentMethod.ONLINE).build();

		try {
			reservationService.reserve(reservation, null);
		} catch (ReservationAvailabilityException | InvalidDateOrderException e) {
			e.printStackTrace();
		}

	}

	private void runStep4Scenario() {
		logger.info("|----------------------- Scenario 4 -----------------------|");
		// Create 2 new guests
		Guest guest1 = Guest.builder("g1s4.guest@codehub.gr", new Date("03/15/1999")).firstname("Guest one").lastname(
				"scenario four").address("Guest somewhere").build();

		Guest guest2 = Guest.builder("g2s4.guest2@codehub.gr", new Date("05/18/2000")).firstname("Guest two").lastname(
				"scenario four").address("Guest 2").build();
		try {
			logger.info("Guest registration status is {}. {}", guestService.register(guest1), guest1);
			logger.info("Guest registration status is {}. {}", guestService.register(guest2), guest2);
		} catch (InvalidObjectValuesException | NullPointerException e) {
			logger.info("Invalid guest: {}", e.getMessage());
		}

		Host host1 = Host.builder("h2s4.host@codehub.gr", "Host two", "scenario 4").dateOfBirth(new Date("06/08/2000"))
				.build();
		Property property1 = Property.builder("Brazil, Copacabana 2", BigDecimal.valueOf(35)).build();
		List<Property> propertyList = new ArrayList<>();
		propertyList.add(property1);
		host1.setPropertyList(propertyList);
		try {
			logger.info("Host registration status is {}. {}", hostService.register(host1), host1);
		} catch (InvalidObjectValuesException | NullPointerException e) {
			logger.info("Invalid host: {}", e.getMessage());
		}
		List<Guest> additionalGuests = new ArrayList<>();
		additionalGuests.add(Guest.builder("cs4.child@codehub.gr", new Date("05/19/2014")).firstname("Young").lastname(
				"scenario four").address("Parents home").build());
		additionalGuests.add(Guest.builder("is4.infant@codehub.gr", new Date("02/20/2020")).firstname("Baby").lastname(
				"scenario four").address("Parents home").build());

		Reservation reservation = Reservation.builder(guest1, property1).checkIn(new Date("02/20/2022")).checkOut(
				new Date("02/25/2022")).paymentMethod(PaymentMethod.ONLINE).build();
		try {
			reservationService.reserve(reservation, additionalGuests);
		} catch (ReservationAvailabilityException | InvalidDateOrderException e) {
			e.printStackTrace();
		}

		//Reservation should fail as the property is not available at the specified dates
		Reservation reservation2 = Reservation.builder(guest2, property1).checkIn(new Date("02/15/2022")).checkOut(
				new Date("02/25/2022")).paymentMethod(PaymentMethod.CASH).build();
		try {
			reservationService.reserve(reservation2, null);
		} catch (ReservationAvailabilityException | InvalidDateOrderException e) {
			logger.error("Reservation error: {}", reservation2, e);
		}

		//Get all reservations
		reservationService.getReservations().forEach(r -> logger.info("{}", r));

		//Cancel reservation
		reservationService.cancel(reservation);

		//Get all reservations
		reservationService.getReservations().forEach(r -> logger.info("{}", r));
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
