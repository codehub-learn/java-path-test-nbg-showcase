package gr.codelearn.core.showcase.oop.domain;

public class Bike {
    private String name;
    private int tyrePressure;
    private String color;
    private int numberOfGears;
    private int currentGear;

    public Bike(String name, String color, int numberOfGears) {
        this.name = name;
        this.color = color;
        this.numberOfGears = numberOfGears;
        this.tyrePressure= 0;
        this.currentGear = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTyrePressure() {
        return tyrePressure;
    }

    public void setTyrePressure(int tyrePressure) {
        this.tyrePressure = tyrePressure;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNumberOfGears() {
        return numberOfGears;
    }

    public void setNumberOfGears(int numberOfGears) {
        this.numberOfGears = numberOfGears;
    }

    public int getCurrentGear() {
        return currentGear;
    }

    public void setCurrentGear(int currentGear) {
        this.currentGear = currentGear;
    }

    public String cyclingSound() {
        return "Tik tik tik!";
    }

    @Override
    public String toString() {
        return "Bike{" +
                "name='" + name + '\'' +
                ", tyrePressure=" + tyrePressure +
                ", color='" + color + '\'' +
                ", numberOfGears=" + numberOfGears +
                ", currentGear=" + currentGear +
                '}';
    }
}
