package gr.codelearn.rentbnb.service;

import gr.codelearn.rentbnb.domain.Guest;
import gr.codelearn.rentbnb.domain.Host;
import gr.codelearn.rentbnb.domain.Property;
import gr.codelearn.rentbnb.domain.Reservation;

import java.util.Date;
import java.util.List;

public interface DataRepositoryService {
	List<Guest> getGuests();

	Guest getGuest(Long id);

	Guest getGuest(String email);

	boolean updateGuest(Guest guest);

	boolean save(Guest guest);

	List<Reservation> getReservations();

	Reservation getReservation(Long id);

	Reservation getReservation(Long guestId, Long propertyId);

	boolean delete(Reservation reservation);

	boolean save(Reservation reservation);

	boolean save(Host host);

	List<Host> getHosts();

	Host getHost(Long id);

	boolean save(Host host, Property property);

	List<Property> getProperties();

	Property getProperty(Long id);

	boolean isPropertyAvailableAtSpecifiedDates(Long id, Date checkIn, Date checkout);

}
