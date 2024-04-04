package gr.codelearn.example;

public class RaceService {
	private RaceRepository raceRepository;

	public RaceService(RaceRepository raceRepository) {
		this.raceRepository = raceRepository;
	}

	public void registerDriver(Driver driver, Car car) {
		if (driver != null && car != null && !raceRepository.contains(driver.getName())) {
			raceRepository.insert(driver, car);
		}
	}

	public Driver search(String name) {
		if (!name.isEmpty() && raceRepository.contains(name)) {
			return raceRepository.getDriverByName(name);
		}
		return null;
	}
}
