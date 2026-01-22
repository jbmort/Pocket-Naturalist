package com.pocket.naturalist.entity;

import java.time.LocalDateTime;

import org.springframework.data.geo.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sightings")
public class Sighting {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    Animal animal;

    @Column(nullable = false, columnDefinition = "geometry(Point, 4326)")
    Point locationOfAnimal;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    Point locationOfReport;

    LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id", nullable = false)
    Park park;

    public Sighting(Animal animal, Point locationOfAnimal, Point locationOfReport, Park park) {
            this.animal = animal;
            this.locationOfAnimal = locationOfAnimal;
            this.locationOfReport = locationOfReport;
            this.park = park;
        }
        
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Point getLocationOfAnimal() {
        return locationOfAnimal;
    }

    public void setLocationOfAnimal(Point locationOfAnimal) {
        this.locationOfAnimal = locationOfAnimal;
    }

    public Point getLocationOfReport() {
        return locationOfReport;
    }

    public void setLocationOfReport(Point locationOfReport) {
        this.locationOfReport = locationOfReport;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }



}
