package gr.codelearn.rentbnb.service.impl;

import gr.codelearn.rentbnb.domain.Guest;
import gr.codelearn.rentbnb.domain.PaymentMethod;
import gr.codelearn.rentbnb.domain.Property;
import gr.codelearn.rentbnb.domain.Reservation;
import gr.codelearn.rentbnb.exception.InvalidDateOrderException;
import gr.codelearn.rentbnb.exception.ReservationAvailabilityException;
import gr.codelearn.rentbnb.service.DataRepositoryService;
import gr.codelearn.rentbnb.service.GuestService;
import gr.codelearn.rentbnb.service.MailService;
import gr.codelearn.rentbnb.service.PaymentService;
import gr.codelearn.rentbnb.service.ReservationService;
import gr.codelearn.rentbnb.util.FeeCalculator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class ReservationServiceImpl implements ReservationService {
	private DataRepositoryService dataRepositoryService;
	private GuestService guestService;
	private MailService mailService;
	private PaymentService paymentService;

	public ReservationServiceImpl(DataRepositoryService dataRepositoryService, GuestService guestService,
								  MailService mailService, PaymentService paymentService) {
		this.dataRepositoryService = dataRepositoryService;
		this.guestService = guestService;
		this.mailService = mailService;
		this.paymentService = paymentService;
	}

	@Override
	public boolean reserve(Reservation reservation, List<Guest> additionalGuests)
			throws ReservationAvailabilityException, InvalidDateOrderException {
		if (checkNullability(reservation.getGuest(), reservation.getProperty())) return false;
		if (!reservation.getCheckIn().before(reservation.getCheckOut())) throw new InvalidDateOrderException(
				"Checkout date must " + "be after " + "check in" + " date");
		if (!dataRepositoryService.isPropertyAvailableAtSpecifiedDates(reservation.getProperty().getId(),
																	   reservation.getCheckIn(),
																	   reservation.getCheckOut()))
			throw new ReservationAvailabilityException("Property is not available at the requested dates");

		if (additionalGuests != null && !additionalGuests.isEmpty()) calculateAdditionalGuestsAge(additionalGuests,
																								  reservation);

		//Set total cost
		reservation.setCost(FeeCalculator.calculateReservationCost(reservation));

		if (reservation.getPaymentMethod().equals(PaymentMethod.ONLINE) && !paymentService.executePayment(
				reservation.getCost())) {
			return false;
		}

		if (dataRepositoryService.save(reservation)) {
			if (mailService != null) {
				mailService.send("SUCCESSFUL RESERVATION",
								 "You have made a reservation!!!\nTotal cost: " + reservation.getCost(),
								 reservation.getGuest().getEmail());
			}
			guestService.updateReputation(reservation.getGuest(), BigDecimal.valueOf(0.1));
			return true;
		}
		return false;
	}

	@Override
	public boolean cancel(Reservation reservation) {
		if (checkNullability(reservation.getGuest(), reservation.getProperty())) return false;
		BigDecimal returnedValue = FeeCalculator.calculateCostOnCancel(reservation);
		if (reservation.getPaymentMethod().equals(PaymentMethod.ONLINE) && !paymentService.addMoney(returnedValue)) {
			return false;
		}
		if (dataRepositoryService.delete(reservation)) {
			if (mailService != null) {
				mailService.send("SUCCESSFUL CANCELLATION",
								 "You have successfully canceled your reservation!!!\n " + "Returned amount in case " +
										 "of online payment: " + returnedValue, reservation.getGuest().getEmail());
			}
			guestService.updateReputation(reservation.getGuest(), BigDecimal.valueOf(-0.2));
			return true;
		}
		return false;
	}

	@Override
	public Reservation getReservation(Long id) {
		return dataRepositoryService.getReservation(id);
	}

	@Override
	public List<Reservation> getReservations() {
		return dataRepositoryService.getReservations();
	}

	private void calculateAdditionalGuestsAge(List<Guest> guests, Reservation reservation) {
		Calendar birthDay = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		long currentTime = System.currentTimeMillis();
		for (Guest guest : guests) {
			birthDay.setTimeInMillis(guest.getDateOfBirth().getTime());
			now.setTimeInMillis(currentTime);
			int yearsOld = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
			if (yearsOld < 2) reservation.setNumOfInfants(reservation.getNumOfInfants() + 1);
			else if (yearsOld < 18) reservation.setNumOfChildren(reservation.getNumOfChildren() + 1);
			else reservation.setNumOfAdults(reservation.getNumOfAdults() + 1);
		}
	}

	private boolean checkNullability(Guest guest, Property property) {
		if (guest == null) {
			log.warn("Guest is null.");
			return true;
		}
		if (property == null) {
			log.warn("Property is null.");
			return true;
		}
		return false;
	}
}
