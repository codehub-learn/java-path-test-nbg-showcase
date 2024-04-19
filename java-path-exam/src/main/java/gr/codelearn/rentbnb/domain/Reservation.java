package gr.codelearn.rentbnb.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Reservation {
	private long id;
	private Guest guest;
	private Property property;
	private Date checkIn;
	private Date checkOut;
	private PaymentMethod paymentMethod;
	private BigDecimal cost;
	private Integer numOfAdults;
	private Integer numOfChildren;
	private Integer numOfInfants;

	public static ReservationBuilder builder(Guest guest, Property property) {
		Objects.requireNonNull(guest, "Guest should not be null.");
		Objects.requireNonNull(property, "Guest should not be null.");
		return new ReservationBuilder().guest(guest).property(property).numOfAdults(1).numOfChildren(0).numOfInfants(0);
	}
}
