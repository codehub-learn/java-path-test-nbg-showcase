package gr.codelearn.rentbnb.util;

import gr.codelearn.rentbnb.domain.Guest;
import net.andreinc.mockneat.MockNeat;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GuestGenerator {
	private GuestGenerator() {
	}

	;

	public static List<Guest> createRandomGuests(int sampleSize) {
		ArrayList<Guest> guestList = new ArrayList<>();
		for (int i = 0; i < sampleSize; i++) {
			String randomFirstname = MockNeat.secure().names().first().get();
			String randomLastname = MockNeat.secure().names().last().get();
			//following method creates a random number between 1900 and 2021
			//nextInt is normally exclusive of the top value,
			//so add 1 to make it inclusive
			int randomNumber = ThreadLocalRandom.current().nextInt(2, 99999);
			String email = MockNeat.secure().emails().get();
			email = randomNumber + email;

			LocalDate randomLocalDate = MockNeat.secure().localDates().get();
			java.util.Date randomDate = Date.from(
					randomLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

			String randomCountry = MockNeat.secure().countries().get();
			String randomCity = MockNeat.secure().cities().capitals().get();
			String address = randomCountry + ", " + randomCity + " " + (randomNumber - 1);

			Guest randomGuest = Guest.builder(email, randomDate).firstname(randomFirstname).lastname(randomLastname)
					.address(address).build();
			guestList.add(randomGuest);
		}
		return guestList;
	}
}
