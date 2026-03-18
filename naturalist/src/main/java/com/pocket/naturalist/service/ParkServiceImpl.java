package com.pocket.naturalist.service;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.AdminParkDataDTO;
import com.pocket.naturalist.dto.ParkDataDTO;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.exception.ResourceNotFoundException;
import com.pocket.naturalist.repository.ParkRepository;

@Service
public class ParkServiceImpl implements ParkService {

    ParkRepository parkRepository;

    public ParkServiceImpl(ParkRepository parkRepository) {
        this.parkRepository = parkRepository;
    }

    /**
     * Retrieves basic data needed to load the parks main page when a user first
     * visits
     * 
     * @param parkSlug
     * @return ParkDataDTO
     */
    @Override
    public ParkDataDTO getMainPageParkData(String parkSlug) {
        Park park = parkRepository.findByUrlSlug(parkSlug).orElseThrow(() -> new ResourceNotFoundException(String.format(
                "Park with slug '%s' not found.", parkSlug
            )));

        return new ParkDataDTO(park.getName(), park.getBoundaryList(), park.getFeatures(), park.getAnimals());
       
    }

    /**
     * Retrives park data to populate the park admin dashboard
     * 
     * @param parkSlug
     * @return ParkDataDTO
     */
    @Override
    public AdminParkDataDTO getAdminParkData(String parkSlug) {
        Park park = parkRepository.findByUrlSlug(parkSlug).orElseThrow(() -> new ResourceNotFoundException(String.format(
                "Park with slug '%s' not found.", parkSlug
            )));

        return new AdminParkDataDTO(
                park.getName(),
                park.getBoundaryList(),
                park.getFeatures(),
                park.getAnimals(),
                park.getManagers());
    }

    /**
     * Applies updates from the admin to the associated park
     * 
     * @param parkSlug
     * @param updatedData
     */

    @Override
    public ParkDataDTO updateParkData(String parkSlug, ParkDataDTO updatedData) {

        Park park = parkRepository.findByUrlSlug(parkSlug).orElseThrow(() -> new ResourceNotFoundException(String.format(
                "Park with slug '%s' not found.", parkSlug
            )));

        park.setName(updatedData.parkName());
        park.setBoundaryList(updatedData.boundaries());
        park.setFeatures(updatedData.features());
        park.setAnimals(updatedData.animals());
        parkRepository.save(park);

        return new ParkDataDTO(
                park.getName(),
                park.getBoundaryList(),
                park.getFeatures(),
                park.getAnimals());
    }

}
