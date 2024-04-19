package gr.codelearn.rentbnb.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Property {
	private Long id;
	private Long hostId;
	private String address;
	private BigDecimal pricePerDay;

	public static PropertyBuilder builder(String address, BigDecimal pricePerDay) {
		Objects.requireNonNull(address, "Address should not be null");
		Objects.requireNonNull(pricePerDay, "Price per day should not be null");
		return new PropertyBuilder().address(address).pricePerDay(pricePerDay);
	}
}
