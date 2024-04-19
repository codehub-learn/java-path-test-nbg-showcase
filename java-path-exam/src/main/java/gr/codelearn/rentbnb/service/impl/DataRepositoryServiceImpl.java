package gr.codelearn.rentbnb.service.impl;

import gr.codelearn.rentbnb.DataSource;
import gr.codelearn.rentbnb.RentBnbDemo;
import gr.codelearn.rentbnb.domain.Guest;
import gr.codelearn.rentbnb.domain.Host;
import gr.codelearn.rentbnb.domain.PaymentMethod;
import gr.codelearn.rentbnb.domain.Property;
import gr.codelearn.rentbnb.domain.Reservation;
import gr.codelearn.rentbnb.service.DataRepositoryService;
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
import java.util.Date;
import java.util.List;

public class DataRepositoryServiceImpl implements DataRepositoryService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	// ---------------------- GUEST ------------------------------
	@Override
	public boolean save(Guest guest) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("insert.table.guest.000"), Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, guest.getEmail());

			// Handle potential null values
			if (guest.getFirstname() != null) {
				preparedStatement.setString(2, guest.getFirstname());
			} else {
				preparedStatement.setNull(2, Types.VARCHAR);
			}
			if (guest.getLastname() != null) {
				preparedStatement.setString(3, guest.getLastname());
			} else {
				preparedStatement.setNull(3, Types.VARCHAR);
			}
			if (guest.getAddress() != null) {
				preparedStatement.setString(4, guest.getAddress());
			} else {
				preparedStatement.setNull(4, Types.VARCHAR);
			}

			preparedStatement.setDate(5, new java.sql.Date(guest.getDateOfBirth().getTime()));
			preparedStatement.setBigDecimal(6, guest.getReputation());

			preparedStatement.executeUpdate();
			try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
				if (keys.next()) {
					guest.setId(keys.getLong(1));
				}
			}
			logger.debug("Insert command was successful with returning guest {}.", guest);
			return true;
		} catch (SQLException ex) {
			logger.error("Error while inserting guest {}.", guest, ex);
			return false;
		}
	}

	@Override
	public List<Guest> getGuests() {
		try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement();
			 ResultSet results = statement.executeQuery(
					 RentBnbDemo.sqlCommands.getProperty("select.table.guest.000"))) {

			List<Guest> guests = new ArrayList<>();
			while (results.next()) {
				//@formatter:off
				guests.add(Guest.builder(results.getString("email"), results.getDate("dateofbirth"))
								      .id(results.getLong("id"))
								      .firstname(results.getString("firstname"))
									  .lastname(results.getString("lastname"))
									  .address(results.getString("address"))
								      .reputation(results.getBigDecimal("reputation"))
									  .build());
				//@formatter:on
			}

			logger.debug("Retrieved {} guests.", guests.size());

			return guests;
		} catch (SQLException ex) {
			logger.error("Error while retrieving all guests.", ex);
		}
		// Instead of delegating the error by bubbling up the exception via method signature, we are handling it
		// locally and return an empty list
		return Collections.emptyList();
	}

	@Override
	public Guest getGuest(String email) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("select.table.guest.002"))) {

			preparedStatement.setString(1, email);

			return getGuest(preparedStatement);
		} catch (SQLException ex) {
			logger.error("Error while retrieving Guest['{}'].", email, ex);
		}
		return null;
	}

	@Override
	public boolean updateGuest(Guest guest) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("update.table.guest.000"), Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setBigDecimal(1, guest.getReputation());

			preparedStatement.setLong(2, guest.getId());

			preparedStatement.executeUpdate();
			try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
				if (keys.next()) {
					guest.setId(keys.getLong(1));
				}
			}
			logger.debug("Update command was successful with returning guest {}.", guest);
			return true;
		} catch (SQLException ex) {
			logger.error("Error while inserting guest {}.", guest, ex);
			return false;
		}
	}

	@Override
	public Guest getGuest(Long id) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("select.table.guest.001"))) {

			preparedStatement.setLong(1, id);

			return getGuest(preparedStatement);
		} catch (SQLException ex) {
			logger.error("Error while retrieving Guest[{}].", id, ex);
		}
		return null;
	}

	private Guest getGuest(PreparedStatement preparedStatement) throws SQLException {
		try (ResultSet resultSet = preparedStatement.executeQuery()) {
			if (resultSet.next()) {
				//@formatter:off
				Guest guest = Guest.builder(resultSet.getString("email"), resultSet.getDate("dateofbirth"))
						.id(resultSet.getLong("id"))
						.firstname(resultSet.getString("firstname"))
						.lastname(resultSet.getString("lastname"))
						.address(resultSet.getString("address"))
						.reputation(resultSet.getBigDecimal("reputation")).build();
				//@formatter:on
				logger.debug("Retrieved {}.", guest);

				return guest;
			}
		}
		return null;
	}

	// ---------------------- RESERVATION ------------------------------
	@Override
	public List<Reservation> getReservations() {
		try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement();
			 ResultSet results = statement.executeQuery(
					 RentBnbDemo.sqlCommands.getProperty("select.table.reservation.000"))) {

			List<Reservation> reservations = new ArrayList<>();
			while (results.next()) {
				//@formatter:off
				Reservation reservation = Reservation.builder(getGuest(results.getLong("guest_id")),
															  getProperty(results.getLong("property_id")))
								   .id(results.getLong("id"))
								   .checkIn(results.getDate("checkin_date"))
								   .checkOut(results.getDate("checkout_date"))
								   .paymentMethod(PaymentMethod.valueOf(results.getString("payment_method")))
								   .cost(results.getBigDecimal("cost"))
						           .numOfAdults(results.getInt("numofadults"))
						           .numOfChildren(results.getInt("numofchildren"))
						           .numOfInfants(results.getInt("numofinfants"))
								   .build();
				//@formatter:on

				reservations.add(reservation);
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
	public Reservation getReservation(Long id) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("select.table.reservation.001"))) {

			preparedStatement.setLong(1, id);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					//@formatter:off
					Reservation reservation = Reservation.builder(getGuest(resultSet.getLong("guest_id")),
																  getProperty(resultSet.getLong("property_id")))
							.id(resultSet.getLong("id"))
							.checkIn(resultSet.getDate("checkin_date"))
							.checkOut(resultSet.getDate("checkout_date"))
							.paymentMethod(PaymentMethod.valueOf(resultSet.getString("payment_method")))
							.cost(resultSet.getBigDecimal("cost"))
							.numOfAdults(resultSet.getInt("numofadults"))
							.numOfChildren(resultSet.getInt("numofchildren"))
							.numOfInfants(resultSet.getInt("numofinfants"))
							.build();
					//@formatter:on

					logger.debug("Retrieved {}.", reservation);

					return reservation;
				}
			}
		} catch (SQLException ex) {
			logger.error("Error while retrieving reservation[{}].", id, ex);
		}
		return null;
	}

	@Override
	public Reservation getReservation(Long guestId, Long propertyId) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("select.table.reservation.003"))) {

			preparedStatement.setLong(1, guestId);
			preparedStatement.setLong(2, propertyId);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					//@formatter:off
					Reservation reservation = Reservation.builder(getGuest(resultSet.getLong("guest_id")),
																  getProperty(resultSet.getLong("property_id")))
							.id(resultSet.getLong("id"))
							.checkIn(resultSet.getDate("checkin_date"))
							.checkOut(resultSet.getDate("checkout_date"))
							.paymentMethod(PaymentMethod.valueOf(resultSet.getString("payment_method")))
							.cost(resultSet.getBigDecimal("cost"))
							.numOfAdults(resultSet.getInt("numofadults"))
							.numOfChildren(resultSet.getInt("numofchildren"))
							.numOfInfants(resultSet.getInt("numofinfants"))
							.build();
					//@formatter:on

					logger.debug("Retrieved {}.", reservation);

					return reservation;
				}
			}
		} catch (SQLException ex) {
			logger.error("Error while retrieving reservation[{},{}].", guestId, propertyId, ex);
		}
		return null;
	}

	@Override
	public boolean delete(Reservation reservation) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("delete.table.reservation.000"),
					 Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setLong(1, reservation.getGuest().getId());
			preparedStatement.setLong(2, reservation.getProperty().getId());

			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				logger.debug("Delete command was successful for reservation {}.", reservation);
				return true;
			}
			return false;
		} catch (SQLException ex) {
			logger.error("Error while inserting reservation {}.", reservation, ex);
			return false;
		}
	}

	@Override
	public boolean save(Reservation reservation) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("insert.table.reservation.000"),
					 Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setLong(1, reservation.getGuest().getId());
			preparedStatement.setLong(2, reservation.getProperty().getId());
			preparedStatement.setTimestamp(3, new Timestamp(reservation.getCheckIn().getTime()));
			preparedStatement.setTimestamp(4, new Timestamp(reservation.getCheckOut().getTime()));
			preparedStatement.setString(5, reservation.getPaymentMethod().name());
			preparedStatement.setBigDecimal(6, reservation.getCost());
			preparedStatement.setInt(7, reservation.getNumOfAdults());
			preparedStatement.setInt(8, reservation.getNumOfChildren());
			preparedStatement.setInt(9, reservation.getNumOfInfants());

			preparedStatement.executeUpdate();
			try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
				if (keys.next()) {
					reservation.setId(keys.getLong(1));
				}
			}

			logger.debug("Insert command was successful with returning reservation {}.", reservation);

			return true;
		} catch (SQLException ex) {
			logger.error("Error while inserting reservation {}.", reservation, ex);
			return false;
		}
	}

	// ---------------------- HOST ------------------------------
	@Override
	public boolean save(Host host) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("insert.table.host.000"), Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setString(1, host.getEmail());

			// Handle potential null values
			if (host.getFirstname() != null) {
				preparedStatement.setString(2, host.getFirstname());
			} else {
				preparedStatement.setNull(2, Types.VARCHAR);
			}
			if (host.getLastname() != null) {
				preparedStatement.setString(3, host.getLastname());
			} else {
				preparedStatement.setNull(3, Types.VARCHAR);
			}
			if (host.getDateOfBirth() != null) {
				preparedStatement.setDate(4, new java.sql.Date(host.getDateOfBirth().getTime()));
			} else {
				preparedStatement.setNull(4, Types.DATE);
			}

			preparedStatement.executeUpdate();
			try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
				if (keys.next()) {
					host.setId(keys.getLong(1));
					logger.debug("Insert command was successful with returning host {}.", host);
					if (host.getPropertyList() != null && !host.getPropertyList().isEmpty()) {
						for (Property property : host.getPropertyList()) {
							save(host, property);
						}
					}
				}
			}
			return true;
		} catch (SQLException ex) {
			logger.error("Error while inserting host {}.", host, ex);
			return false;
		}
	}

	@Override
	public List<Host> getHosts() {
		try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement();
			 ResultSet results = statement.executeQuery(RentBnbDemo.sqlCommands.getProperty("select.table.host.000"))) {

			List<Host> hosts = new ArrayList<>();
			while (results.next()) {
				//@formatter:off
				Host host = Host.builder(results.getString("email"), results.getString("firstname"),
										   results.getString("lastname"))
									   .id(results.getLong("id"))
						               .dateOfBirth(results.getDate("dateofbirth"))
									   .build();
				//@formatter:on

				getProperties(host);
				hosts.add(host);
			}

			logger.debug("Retrieved {} hosts.", hosts.size());

			return hosts;
		} catch (SQLException ex) {
			logger.error("Error while retrieving all hosts.", ex);
		}
		// Instead of delegating the error by bubbling up the exception via method signature, we are handling it
		// locally and return an empty list
		return Collections.emptyList();
	}

	private void getProperties(Host host) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("select.table.property.002"))) {
			preparedStatement.setLong(1, host.getId());

			try (ResultSet innerResults = preparedStatement.executeQuery()) {
				while (innerResults.next()) {
					//@formatter:off
					host.add(Property.builder(innerResults.getString("address"),
												innerResults.getBigDecimal("price"))
									  .id(innerResults.getLong("id"))
									 .hostId(host.getId())
									  .build());
					//@formatter:on
				}
			}
		} catch (SQLException ex) {
			logger.error("Error while retrieving properties for Host[{}].", host.getId(), ex);
		}
	}

	@Override
	public Host getHost(Long id) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("select.table.host.001"))) {

			preparedStatement.setLong(1, id);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					//@formatter:off
					Host host = Host.builder(resultSet.getString("email"),
											  resultSet.getString("firstname"),
											  resultSet.getString("lastname"))
							.id(resultSet.getLong("id"))
							.dateOfBirth(resultSet.getDate("dateofbirth"))
							.build();
					//@formatter:on

					getProperties(host);
					logger.debug("Retrieved {}.", host);

					return host;
				}
			}
		} catch (SQLException ex) {
			logger.error("Error while retrieving host[{}].", id, ex);
		}
		return null;
	}

	// ---------------------- PROPERTY ------------------------------
	@Override
	public List<Property> getProperties() {
		try (Connection connection = DataSource.getConnection(); Statement statement = connection.createStatement();
			 ResultSet results = statement.executeQuery(
					 RentBnbDemo.sqlCommands.getProperty("select.table.property.000"))) {

			List<Property> properties = new ArrayList<>();
			while (results.next()) {
				//@formatter:off
				properties.add(Property.builder(results.getString("address"),
									  results.getBigDecimal("price"))
									   .id(results.getLong("id"))
									   .hostId(results.getLong("host_id"))
									 .build());
				//@formatter:on
			}

			logger.debug("Retrieved {} properties.", properties.size());

			return properties;
		} catch (SQLException ex) {
			logger.error("Error while retrieving all properties.", ex);
		}
		// Instead of delegating the error by bubbling up the exception via method signature, we are handling it
		// locally and return an empty list
		return Collections.emptyList();
	}

	@Override
	public Property getProperty(Long id) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("select.table.property.001"))) {

			preparedStatement.setLong(1, id);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					//@formatter:off
					Property property = Property.builder(resultSet.getString("address"),
														 resultSet.getBigDecimal("price"))
							.id(id)
							.build();
					//@formatter:on
					logger.debug("Retrieved {}.", property);

					return property;
				}
			}
			return null;
		} catch (SQLException ex) {
			logger.error("Error while retrieving Property['{}'].", id, ex);
		}
		return null;
	}

	@Override
	public boolean isPropertyAvailableAtSpecifiedDates(Long id, Date checkIn, Date checkOut) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("select.table.reservation.002"))) {
			preparedStatement.setLong(1, id);
			preparedStatement.setTimestamp(2, new Timestamp(checkIn.getTime()));
			preparedStatement.setTimestamp(3, new Timestamp(checkOut.getTime()));
			preparedStatement.setTimestamp(4, new Timestamp(checkIn.getTime()));
			preparedStatement.setTimestamp(5, new Timestamp(checkOut.getTime()));

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return false;
				}
			}

		} catch (SQLException ex) {
			logger.error("Error while retrieving all reservations.", ex);
			return false;
		}
		return true;
	}

	@Override
	public boolean save(Host host, Property property) {
		try (Connection connection = DataSource.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(
					 RentBnbDemo.sqlCommands.getProperty("insert.table.property.000"),
					 Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setLong(1, host.getId());
			preparedStatement.setString(2, property.getAddress());
			preparedStatement.setBigDecimal(3, property.getPricePerDay());

			preparedStatement.executeUpdate();
			try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
				if (keys.next()) {
					property.setId(keys.getLong(1));
					property.setHostId(host.getId());
				}
			}

			logger.debug("Insert command was successful with returning property {}.", property);
			return true;
		} catch (SQLException ex) {
			logger.error("Error while inserting property {}.", property, ex);
			return false;
		}
	}
}
