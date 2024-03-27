package gr.codelearn.core.showcase.oop;

import gr.codelearn.core.showcase.oop.domain.ElectricBike;
import gr.codelearn.core.showcase.oop.domain.MountainBike;
import gr.codelearn.core.showcase.oop.service.BikeService;
import gr.codelearn.core.showcase.oop.service.Transmission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ElectricBike electricBike = new ElectricBike("ElBike", "Yellow", 2);
        MountainBike mountainBike = new MountainBike("MntBike", "Green", 13, 3);
        BikeService bikeService = new BikeService();
        Transmission transmission = new BikeService();

        bikeService.cycle(electricBike);
        bikeService.cycle(mountainBike);
        bikeService.gearUp(mountainBike);

        transmission.gearUp(mountainBike);

        bikeService.increaseTyrePressure(mountainBike);
    }
}
