package gr.codelearn.core.showcase.oop.service;

import gr.codelearn.core.showcase.oop.domain.Bike;

public class BikeService extends AbstractBikeService implements Transmission, WheelRotation {
    public void increaseTyrePressure(Bike bike) {
        int tyrePressure = bike.getTyrePressure();
        if (tyrePressure < 3) {
            bike.setTyrePressure(tyrePressure + 1);
            logger.info("Tyre pressure increased to {}", bike.getTyrePressure());
        }
    }

    @Override
    public void cycle(Bike bike) {
        logger.info("{} cycles and sound like {}.", bike.getName(), bike.cyclingSound());
    }

    @Override
    public void gearUp(Bike bike) {
        int currentGear = bike.getCurrentGear();
        if (currentGear < bike.getNumberOfGears()) {
            logger.info("Changing gear from {} to  {}.", currentGear, currentGear + 1);
            bike.setCurrentGear(currentGear + 1);
        }
    }

    @Override
    public void rotate(Bike bike) {
        logger.info("{} rotating...", bike.getName());
    }
}
