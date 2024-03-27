package gr.codelearn.core.showcase.oop.domain;

public class MountainBike extends Bike{
    private int numberOfSuspensions;

    public MountainBike(String name, String color, int numberOfGears, int numberOfSuspensions) {
        super(name, color, numberOfGears);
        this.numberOfSuspensions = numberOfSuspensions;
    }

    public int getNumberOfSuspensions() {
        return numberOfSuspensions;
    }

    public void setNumberOfSuspensions(int numberOfSuspensions) {
        this.numberOfSuspensions = numberOfSuspensions;
    }

    @Override
    public String cyclingSound() {
        return "vroom vroom";
    }

    @Override
    public String toString() {
        return "MountainBike{" + super.toString() +
                "numberOfSuspensions=" + numberOfSuspensions +
                '}';
    }
}
