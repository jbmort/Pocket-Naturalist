package com.pocket.naturalist.config;

import org.springframework.stereotype.Component;

import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.geo.Point;
import org.springframework.scheduling.annotation.Scheduled;


@Component
public class animalMovementScheduler {

    Park park;

    Animal bison = new Animal("Bison bison", "American Bison", "A large, grazing mammal native to North America.");

    Sighting bisonSighting1 = new Sighting(bison, new Point(-105.0, 40.0), new Point(-104.0, 41.0), park);
    Sighting bisonSighting2 = new Sighting(bison, new Point(-106.0, 39.5), new Point(-105.5, 40.5), park);
    Sighting bisonSighting3 = new Sighting(bison, new Point(-104.5, 40.5), new Point(-104.0, 41.0), park);
    Sighting bisonSighting4 = new Sighting(bison, new Point(-105.5, 39.0), new Point(-105.0, 40.0), park);

    List<Sighting> sightings =List.of(bisonSighting1, bisonSighting2, bisonSighting3, bisonSighting4);

    @Scheduled(fixedRate = 3000)
    public void simulateAnimalMovement() {
        
        for(var sighting : sightings){
            moveAnimal(sighting);
        }

        System.out.println("Simulating animal movement...");
    }

    private void moveAnimal(Sighting sighting) {
        double newLng = 0.0;
        double newLat = 0.0;
        do{
        newLng = sighting.getLocationOfAnimal().getX() + (Math.random() - 0.5) * 0.1;
        newLat = sighting.getLocationOfAnimal().getY() + (Math.random() - 0.5) * 0.1;
        }
        while(!geoUtils.isPointInPolygon(new Point(newLng, newLat), sighting.getPark().getBoundary()));
        
        sighting.setLocationOfAnimal(new Point(newLng, newLat));
    }

    
}
