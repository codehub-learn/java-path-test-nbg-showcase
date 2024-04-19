package gr.codelearn.rentbnb.service.impl;

import gr.codelearn.rentbnb.domain.Guest;
import gr.codelearn.rentbnb.exception.InvalidObjectValuesException;
import gr.codelearn.rentbnb.service.DataRepositoryService;
import gr.codelearn.rentbnb.service.GuestService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class GuestServiceImpl implements GuestService {
	private DataRepositoryService dataRepositoryService;

	public GuestServiceImpl(DataRepositoryService dataRepositoryService) {
		this.dataRepositoryService = dataRepositoryService;
	}

	@Override
	public boolean register(Guest guest) throws InvalidObjectValuesException, NullPointerException {
		if (guest == null) {
			throw new NullPointerException("Guest must not be null.");
		}
		// Handle potential null values
		if (guest.getFirstname() != null && guest.getFirstname().matches(".*\\d.*")) {
			throw new InvalidObjectValuesException("Guest's firstname should not contain any numbers.");
		}
		if (guest.getLastname() != null && guest.getLastname().matches(".*\\d.*")) {
			throw new InvalidObjectValuesException("Guest's lastname should not contain any numbers.");
		}
		if (guest.getDateOfBirth() == null || calculateAge(guest.getDateOfBirth()) < 1 || calculateAge(
				guest.getDateOfBirth()) > 120) {
			throw new InvalidObjectValuesException("The guest's age should be between 1 and 100.");
		}
		return dataRepositoryService.save(guest);
	}

	private Integer calculateAge(Date dateOfBirth) {
		Calendar birthDay = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		long currentTime = System.currentTimeMillis();
		birthDay.setTimeInMillis(dateOfBirth.getTime());
		now.setTimeInMillis(currentTime);
		return now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
	}

	@Override
	public void updateReputation(Guest guest, BigDecimal value) {
		log.info("Guest current reputation {}", guest.getReputation());

		guest.setReputation(guest.getReputation().add(value));

		//Checks if updated reputation value is out of range [0..5] and corrects it
		if (guest.getReputation().compareTo(BigDecimal.valueOf(5L)) > 0) {
			guest.setReputation(BigDecimal.valueOf(5L));
		} else if (guest.getReputation().compareTo(BigDecimal.ZERO) < 0) {
			guest.setReputation(BigDecimal.ZERO);
		}

		log.info("Guest updated reputation {}", guest.getReputation());
		dataRepositoryService.updateGuest(guest);
	}

	@Override
	public List<Guest> getGuests() {
		return dataRepositoryService.getGuests();
	}

	@Override
	public Guest getGuest(Long id) {
		return dataRepositoryService.getGuest(id);
	}

	@Override
	public Guest getGuest(String email) {
		return dataRepositoryService.getGuest(email);
	}
}
