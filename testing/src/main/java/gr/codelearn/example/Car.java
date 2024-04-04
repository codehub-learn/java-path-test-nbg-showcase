package gr.codelearn.example;

public class Car {
	private String plateNumber;
	private String model;
	private Integer horsePower;

	public Car(String plateNumber, String model, Integer horsePower) {
		this.plateNumber = plateNumber;
		this.model = model;
		this.horsePower = horsePower;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getHorsePower() {
		return horsePower;
	}

	public void setHorsePower(Integer horsePower) {
		this.horsePower = horsePower;
	}
}
