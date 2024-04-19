package gr.codelearn.rentbnb.util;

import gr.codelearn.rentbnb.domain.Guest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class CSVGuestReader {

	/**
	 * Use this method to save a list of guests to a CSV
	 *
	 * @param guests   The list of Java Guests that will be saved into CSV credentials
	 * @param fileName The name of the CSV that will be used to store the guests
	 */
	public void saveToCsv(List<Guest> guests, String fileName) {
		try (Writer writer = Files.newBufferedWriter(Paths.get(fileName)); CSVPrinter csvPrinter = new CSVPrinter(
				writer, CSVFormat.DEFAULT)) {
			log.info("Writing multiple objects to a CSV:");
			// Lambda expression: for each guest, create a record with these 3 fields
			guests.forEach(guest -> {
				try {
					csvPrinter.printRecord(guest.getEmail(), guest.getFirstname(), guest.getLastname(),
										   guest.getDateOfBirth(), guest.getAddress());
				} catch (IOException e) {
					log.error("Exception thrown", e);
				}
			});
			csvPrinter.flush();
		} catch (IOException e) {
			log.error("Exception thrown", e);
		}
	}

	/**
	 * Use this method to convert a CSV full of guest credentials to actual Java Guests
	 *
	 * @param fileName The name of the CSV that needs to be read
	 * @return A list of guests read from a CSV file
	 */
	public List<Guest> restoreFromCsv(String fileName) {
		List<Guest> guestList = new ArrayList<>();
		try (Reader reader = Files.newBufferedReader(
				Paths.get(getClass().getClassLoader().getResource(fileName).toURI()));
			 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
			log.info("Reading multiple objects from a CSV:");
			List<CSVRecord> records = csvParser.getRecords();
			records.forEach(record -> {
				try {
					// We know beforehand the "cell location" of each guest object
					// Bad code practice in case the Guest class changes but will work for now
					SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
					Date importedDate = formatter.parse(record.get(3));
					Guest guest = Guest.builder(record.get(0), importedDate).firstname(record.get(1)).lastname(
							record.get(2)).address(record.get(4)).build();
					guestList.add(guest);
				} catch (ParseException e) {
					log.error("Exception thrown", e);
				}
			});
		} catch (IOException | URISyntaxException e) {
			log.error("Exception thrown", e);
		}
		return guestList;
	}
}
