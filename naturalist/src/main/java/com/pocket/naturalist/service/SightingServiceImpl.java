package com.pocket.naturalist.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;
import com.pocket.naturalist.dto.AnimalLocationsDTO;
import com.pocket.naturalist.dto.SightingMapDTO;

import com.pocket.naturalist.repository.AnimalRepository;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.SightingsRepository;

@Service
public class SightingServiceImpl implements SightingService {

    AnimalRepository animalRepository;
    ParkRepository parkRepository;
    SightingsRepository sightingsRepository;

    @Override
    public boolean createSighting(String animalName, Point locationOfAnimal, Point locationOfReport, String parkSlug) {
        boolean isCreated = false;

        Animal animal = animalRepository.findByCommonName(animalName);
        Optional<Park> optionalPark = parkRepository.findByUrlSlug(parkSlug);
        if (animal != null && optionalPark.isPresent()) {
            Sighting sighting = new Sighting(animal, locationOfAnimal, locationOfReport, optionalPark.orElseThrow());
            

            sightingsRepository.save(sighting);
            isCreated = true;
        }
        return isCreated;
    }

    @Override
    public SightingMapDTO getSightingsForPark(String parkSlug) {
        List<Sighting> sightings;
        Optional<Park> optionalPark = parkRepository.findByUrlSlug(parkSlug);
        List<AnimalLocationsDTO> animalLocationsDTOs = new ArrayList<>();

        if (optionalPark.isPresent()) {

            Park park = optionalPark.orElseThrow();
            sightings = sightingsRepository.findAllByPark(park);

            for (Animal animal : park.getAnimals()) {
                List<Point> animalLocations = sightings.stream()
                        .filter(sighting -> sighting.getAnimal().getId() == animal.getId())
                        .map(Sighting::getLocationOfAnimal)
                        .toList();

                AnimalLocationsDTO animalLocationsDTO = new AnimalLocationsDTO(animal.getCommonName(), animalLocations);
                animalLocationsDTOs.add(animalLocationsDTO);
            }
        }
        return new SightingMapDTO(parkSlug, animalLocationsDTOs);


    }
    
}
