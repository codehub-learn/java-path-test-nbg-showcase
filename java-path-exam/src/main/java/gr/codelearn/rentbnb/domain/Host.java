package gr.codelearn.rentbnb.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Host {
	private Long id;
	private String email;
	private String firstname;
	private String lastname;
	private Date dateOfBirth;
	private List<Property> propertyList;

	public void add(Property property) {
		propertyList.add(property);
	}

	public static HostBuilder builder(String email, String firstname, String lastname) {
		Objects.requireNonNull(email, "Email should not be null");
		Objects.requireNonNull(firstname, "Firstname should not be null");
		Objects.requireNonNull(lastname, "Lastname should not be null");
		return new HostBuilder().email(email).firstname(firstname).lastname(lastname).propertyList(new ArrayList<>());
	}
}
