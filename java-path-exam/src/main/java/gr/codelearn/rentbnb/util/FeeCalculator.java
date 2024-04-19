package gr.codelearn.rentbnb.util;

import gr.codelearn.rentbnb.domain.PaymentMethod;
import gr.codelearn.rentbnb.domain.Reservation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeeCalculator {

	public static BigDecimal calculateReservationCost(Reservation reservation) {
		BigDecimal cost = BigDecimal.valueOf(0);
		BigDecimal pricePerDay = reservation.getProperty().getPricePerDay();
		BigDecimal numOfDays = BigDecimal.valueOf(calculateNumberOfDays(reservation, null));
		BigDecimal numOfAdults = BigDecimal.valueOf(reservation.getNumOfAdults());
		BigDecimal numOfAChildren = BigDecimal.valueOf(reservation.getNumOfChildren());

		//Add cost for total number of days
		cost = cost.add(pricePerDay.multiply(numOfDays));
		log.info("Price per day {}. Cost for {} days: {}.", pricePerDay, numOfDays, cost);
		//Additional cost 3% for each adult
		cost = cost.add(cost.multiply(numOfAdults).multiply(BigDecimal.valueOf(0.03)));
		log.info("Number of adults {}. Cost: {}.", numOfAdults, cost);
		//Additional cost 1% for each child
		cost = cost.add(cost.multiply(numOfAChildren).multiply(BigDecimal.valueOf(0.01)));
		log.info("Number of children {}. Cost: {}.", numOfAChildren, cost);

		//Discount 10% if reservation is made online
		if (reservation.getPaymentMethod().equals(PaymentMethod.ONLINE)) {
			cost = cost.subtract(cost.multiply(BigDecimal.valueOf(PaymentMethod.ONLINE.getDiscount())));
			log.info("Discount for online payment. Cost: {}.", cost);
		}
		//Discount 5% if guest has reputation above 3
		if (reservation.getGuest().getReputation().compareTo(BigDecimal.valueOf(3)) >= 0) {
			cost = cost.subtract(cost.multiply(BigDecimal.valueOf(0.05)));
			log.info("Discount for good reputation. Cost: {}.", cost);
		}

		log.info("Total reservation cost: {}", cost);
		return cost;
	}

	private static long calculateNumberOfDays(Reservation reservation, Date date) {
		long diffInMillis;
		if (date == null) diffInMillis = Math.abs(
				reservation.getCheckOut().getTime() - reservation.getCheckIn().getTime());
		else diffInMillis = Math.abs(date.getTime() - reservation.getCheckIn().getTime());

		return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
	}

	public static BigDecimal calculateCostOnCancel(Reservation reservation) {
		BigDecimal cost = new BigDecimal(String.valueOf(reservation.getCost()));
		long numOfDays = calculateNumberOfDays(reservation, new Date(System.currentTimeMillis()));
		if (numOfDays <= 5 && numOfDays > 1) cost = cost.subtract(
				reservation.getCost().multiply(BigDecimal.valueOf(0.75)));
		else if (numOfDays <= 20) cost = cost.subtract(reservation.getCost().multiply(BigDecimal.valueOf(0.50)));
		else cost = cost.subtract(reservation.getCost().multiply(BigDecimal.valueOf(0.25)));

		log.info("Total cost after cancel is: {}", cost);
		return cost;
	}
}
