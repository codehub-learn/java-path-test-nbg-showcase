package gr.codelearn.core.showcase.oop.service;

import gr.codelearn.core.showcase.oop.domain.Bike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBikeService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public abstract void cycle(Bike bike);

    public final void brake(Bike bike) {
        logger.info("{} stopping!", bike.getName());
    }
}
