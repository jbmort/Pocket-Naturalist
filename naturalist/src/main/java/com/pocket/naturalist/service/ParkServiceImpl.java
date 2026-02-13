package com.pocket.naturalist.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.dto.ParkDataDTO;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.repository.ParkRepository;

@Service
public class ParkServiceImpl implements ParkService{

    ParkRepository parkRepository;

    public ParkServiceImpl(ParkRepository parkRepository){
        this.parkRepository = parkRepository;
    }

    @Override
    public ParkDataDTO getMainPageParkData(String parkSlug) {
        Optional<Park> optionalPark = parkRepository.findByUrlSlug(parkSlug);

        if(optionalPark.isPresent()){
            Park park = optionalPark.get();
            return new ParkDataDTO(park.getName(), park.getBoundaryList(), park.getFeatures(), park.getAnimals());
        }
        else{
            return null;
        }
    }
    
}
