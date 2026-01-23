package com.pocket.naturalist.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    long id;

    @Column(nullable = false, unique = true)
    String scientificName;

    @Column(nullable = false, unique = true)
    String commonName;

    @Column(length = 2000)
    String description; 

    @ManyToMany(mappedBy = "animals", fetch = FetchType.LAZY)
    Set<Park> parks = new HashSet<>();


    public Animal(String scientificName, String commonName, String description) {
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.description = description;
    }
    
    public Animal() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Park> getParks() {
        return parks;
    }

    public void setParks(Set<Park> parks) {
        this.parks = parks;
    }
}
