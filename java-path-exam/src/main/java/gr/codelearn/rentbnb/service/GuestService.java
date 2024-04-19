package gr.codelearn.rentbnb.service;

import gr.codelearn.rentbnb.domain.Guest;
import gr.codelearn.rentbnb.exception.InvalidObjectValuesException;

import java.math.BigDecimal;
import java.util.List;

public interface GuestService {
	boolean register(Guest guest) throws InvalidObjectValuesException, NullPointerException;

	void updateReputation(Guest guest, BigDecimal value);

	List<Guest> getGuests();

	Guest getGuest(Long id);

	Guest getGuest(String email);
}
