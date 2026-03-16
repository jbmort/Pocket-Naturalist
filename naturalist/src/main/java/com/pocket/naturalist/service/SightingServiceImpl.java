package com.pocket.naturalist.service;

import java.time.LocalDateTime;
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
import com.pocket.naturalist.exception.ResourceNotFoundException;

@Service
public class SightingServiceImpl implements SightingService {

    AnimalRepository animalRepository;
    ParkRepository parkRepository;
    SightingsRepository sightingsRepository;

    /**
     * Logs a user sighting for a specific animal at a park
     * 
     * @param animalName
     * @param locationOfAnimal
     * @param locationOfReport
     * @param parkSlug
     * 
     * @return true if sighting is created
     */
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

    /**
     * Retrieves all animal sightings at the park
     * 
     * @param parkSlug
     * @return SightingMapDTO
     * 
     * @apiNote currently returns all sightings but will eventually limit it to a
     *          certain amount or only the most recent
     */
    @Override
    public SightingMapDTO getSightingsForPark(String parkSlug) {
        List<Sighting> sightings;
        Park park = parkRepository.findByUrlSlug(parkSlug).orElseThrow(() -> new ResourceNotFoundException("Park not found!"));
        List<AnimalLocationsDTO> animalLocationsDTOs = new ArrayList<>();

            boolean highVolumeSubmission = sightingsRepository.countSightingsInLastTwoHours(park,
                    LocalDateTime.now().minusHours(2)) > 200;

            sightings = highVolumeSubmission ? sightingsRepository.findRecentSightingsByPark(park)
                    : sightingsRepository.findSightingsForToday(
                            park, LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));

            for (Animal animal : park.getAnimals()) {
                List<Point> animalLocations = sightings.stream()
                        .filter(sighting -> sighting.getAnimal().getId() == animal.getId())
                        .map(Sighting::getLocationOfAnimal)
                        .toList();

                AnimalLocationsDTO animalLocationsDTO = new AnimalLocationsDTO(animal.getCommonName(), animalLocations);
                animalLocationsDTOs.add(animalLocationsDTO);
            }
        return new SightingMapDTO(parkSlug, animalLocationsDTOs);

    }

}
