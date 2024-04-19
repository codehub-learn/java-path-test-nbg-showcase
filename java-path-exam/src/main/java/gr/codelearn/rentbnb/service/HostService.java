package gr.codelearn.rentbnb.service;

import gr.codelearn.rentbnb.domain.Host;
import gr.codelearn.rentbnb.domain.Property;
import gr.codelearn.rentbnb.exception.InvalidObjectValuesException;

import java.util.List;

public interface HostService {
	boolean register(Host host) throws InvalidObjectValuesException, NullPointerException;

	boolean registerProperty(Host host, Property property) throws InvalidObjectValuesException, NullPointerException;

	List<Host> getHosts();

	Host getHost(Long id);
}
