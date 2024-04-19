package gr.codelearn.rentbnb.service.impl;

import gr.codelearn.rentbnb.domain.Host;
import gr.codelearn.rentbnb.domain.Property;
import gr.codelearn.rentbnb.exception.InvalidObjectValuesException;
import gr.codelearn.rentbnb.service.DataRepositoryService;
import gr.codelearn.rentbnb.service.HostService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HostServiceImpl implements HostService {
	private DataRepositoryService dataRepositoryService;

	public HostServiceImpl(DataRepositoryService dataRepositoryService) {
		this.dataRepositoryService = dataRepositoryService;
	}

	@Override
	public boolean register(Host host) throws InvalidObjectValuesException, NullPointerException {
		if (host == null) {
			throw new NullPointerException("Host must not be null.");
		} else if (host.getEmail() == null || host.getFirstname() == null || host.getLastname() == null) {
			throw new InvalidObjectValuesException("Host must not have any empty fields.");
		}
		return dataRepositoryService.save(host);
	}

	@Override
	public boolean registerProperty(Host host, Property property)
			throws InvalidObjectValuesException, NullPointerException {
		if (host == null) {
			throw new NullPointerException("Host must not be null.");
		} else if (property == null) {
			throw new NullPointerException("Property must not be null.");
		} else if (host.getId() == null || host.getFirstname() == null || host.getLastname() == null) {
			throw new InvalidObjectValuesException("Host must not have any empty fields.");
		} else if (property.getAddress() == null || property.getPricePerDay() == null) {
			throw new InvalidObjectValuesException("Property must not have any empty fields.");
		} else if (host.getDateOfBirth() == null || calculateAge(host.getDateOfBirth()) < 18 || calculateAge(
				host.getDateOfBirth()) > 120) {
			throw new InvalidObjectValuesException("The guest's age should be between 18 and 100.");
		}
		return dataRepositoryService.save(host, property);
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
	public List<Host> getHosts() {
		return dataRepositoryService.getHosts();
	}

	@Override
	public Host getHost(Long id) {
		return dataRepositoryService.getHost(id);
	}
}
