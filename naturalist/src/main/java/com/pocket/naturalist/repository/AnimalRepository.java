package com.pocket.naturalist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pocket.naturalist.entity.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Animal findByName(String animalName);
    
    
}
