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
public class Guest {
	private Long id;
	private String email;
	private String firstname;
	private String lastname;
	private Date dateOfBirth;
	private String address;
	// range[0-5]
	private BigDecimal reputation;

	public static GuestBuilder builder(String email, Date dateOfBirth) {
		Objects.requireNonNull(email, "Email should not be null");
		Objects.requireNonNull(dateOfBirth, "Date of birth should not be null");
		return new GuestBuilder().email(email).dateOfBirth(dateOfBirth).reputation(BigDecimal.valueOf(1d));
	}
}
