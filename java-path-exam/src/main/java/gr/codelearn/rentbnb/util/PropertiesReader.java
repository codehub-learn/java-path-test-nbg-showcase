package gr.codelearn.rentbnb.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class that reads a set of properties from the application context.
 */
@Slf4j
public class PropertiesReader {

	private Properties properties;

	public PropertiesReader(String propertyFileName) throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(propertyFileName);
		this.properties = new Properties();
		this.properties.load(is);
	}

	public String getProperty(String propertyName) {
		return this.properties.getProperty(propertyName);
	}

	public Properties getProperties() {
		return properties;
	}
}
