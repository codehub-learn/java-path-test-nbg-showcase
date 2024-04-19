package gr.codelearn.rentbnb.service;

import gr.codelearn.rentbnb.domain.Guest;
import gr.codelearn.rentbnb.domain.Reservation;
import gr.codelearn.rentbnb.exception.InvalidDateOrderException;
import gr.codelearn.rentbnb.exception.ReservationAvailabilityException;

import java.util.List;

public interface ReservationService {
	boolean reserve(Reservation reservation, List<Guest> additionalGuests)
			throws ReservationAvailabilityException, InvalidDateOrderException;

	boolean cancel(Reservation reservation);

	Reservation getReservation(Long id);

	List<Reservation> getReservations();
}
