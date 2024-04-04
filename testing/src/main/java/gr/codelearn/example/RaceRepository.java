package gr.codelearn.example;

public interface RaceRepository {
	void insert(Driver driver, Car car);

	Driver getDriverByName(String name);

	boolean contains(String driverName);
}
