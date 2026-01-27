package com.pocket.naturalist.configTests;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import com.pocket.naturalist.config.animalMovementScheduler;
// import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;

public class animalMovementSchedulerTest {
    List<Sighting> sightings;
    Park park;


    @BeforeEach
    public void setUp() {
    }

    @Test
    void shouldMoveAnimalWithinParkBoundary(){
        
            animalMovementScheduler scheduler = new animalMovementScheduler();
            Point firstSighting = scheduler.simulateAnimalMovement().get(0).getLocationOfAnimal();
            double firstSightingX = firstSighting.getX();
            double firstSightingY = firstSighting.getY();
            Point secondSighting = scheduler.simulateAnimalMovement().get(0).getLocationOfAnimal();
            double secondSightingX = secondSighting.getX();
            double secondSightingY = secondSighting.getY();

            Point firstSighting1 = scheduler.simulateAnimalMovement().get(1).getLocationOfAnimal();
            double firstSightingX1 = firstSighting1.getX();
            double firstSightingY1 = firstSighting1.getY();
            Point secondSighting1 = scheduler.simulateAnimalMovement().get(1).getLocationOfAnimal();
            double secondSightingX1 = secondSighting1.getX();
            double secondSightingY1 = secondSighting1.getY();

            assertNotEquals(firstSightingX, secondSightingX);
            assertNotEquals(firstSightingY, secondSightingY);
            assertNotEquals(firstSightingX1, secondSightingX1);
            assertNotEquals(firstSightingY1, secondSightingY1);
            

        }
    }

    

