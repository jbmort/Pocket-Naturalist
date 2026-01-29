package com.pocket.naturalist.config;

import org.springframework.stereotype.Component;

import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.springframework.scheduling.annotation.Scheduled;


@Component
public class animalMovementScheduler {

   

    Coordinate[] coords = new Coordinate[] {
        new Coordinate(-93.295, 41.565),
        new Coordinate(-93.250, 41.565),
        new Coordinate(-93.250, 41.535),
        new Coordinate(-93.295, 41.535),
        new Coordinate(-93.295, 41.565)
    };
    GeometryFactory geometryFactory = new GeometryFactory();
    LinearRing shell = geometryFactory.createLinearRing(coords);
    Polygon boundary = geometryFactory.createPolygon(shell, null);
    
    org.locationtech.jts.geom.Point mapCenter = geometryFactory.createPoint(new Coordinate(-93.2725, 41.550));
    Park park = new Park("Test Park", List.of(boundary), mapCenter);

    Animal bison = new Animal("Bison bison", "American Bison", "A large, grazing mammal native to North America.");

    Sighting bisonSighting1 = new Sighting(bison, geometryFactory.createPoint(new Coordinate(-93.2725, 41.550)), geometryFactory.createPoint(new Coordinate(-93.2725, 41.550)), park);
    Sighting bisonSighting2 = new Sighting(bison, geometryFactory.createPoint(new Coordinate(-93.2725, 41.550)), geometryFactory.createPoint(new Coordinate(-93.2725, 41.550)), park);
    Sighting bisonSighting3 = new Sighting(bison, geometryFactory.createPoint(new Coordinate(-93.2725, 41.550)), geometryFactory.createPoint(new Coordinate(-93.2725, 41.550)), park);
    Sighting bisonSighting4 = new Sighting(bison, geometryFactory.createPoint(new Coordinate(-93.2725, 41.550)), geometryFactory.createPoint(new Coordinate(-93.2725, 41.550)), park);

    List<Sighting> sightings =List.of(bisonSighting1, bisonSighting2, bisonSighting3, bisonSighting4);

    @Scheduled(fixedRate = 3000)
    public List<Sighting> simulateAnimalMovement() {
        
        for(var sighting : sightings){
            System.out.println("Current Sighting Location: " + sighting.getLocationOfAnimal());
            sighting = moveAnimal(sighting);
            System.out.println("Updated Sighting Location: " + sighting.getLocationOfAnimal());

        }

        System.out.println("Simulating animal movement...");

        return sightings;
    }

    private Sighting moveAnimal(Sighting sighting) {

        Sighting updatedSighting = sighting;
        double newLng = 0.0;
        double newLat = 0.0;
        do{
        newLng = sighting.getLocationOfAnimal().getX() + (Math.random() - 0.5) * 0.01;
        newLat = sighting.getLocationOfAnimal().getY() + (Math.random() - 0.5) * 0.01;
        // newLng = sighting.getLocationOfAnimal().getX() + .001;
        // newLat = sighting.getLocationOfAnimal().getY() + .001;
        }
                // while(!geoUtils.isPointInPolygon(geometryFactory.createPoint(new Coordinate(newLng, newLat)), sighting.getPark().getBoundary()));
        while(!boundary.contains(geometryFactory.createPoint(new Coordinate(newLng, newLat))));
        updatedSighting.setLocationOfAnimal(geometryFactory.createPoint(new Coordinate(newLng, newLat)));
        return updatedSighting;
    }
    
    
}
