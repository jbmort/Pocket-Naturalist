package com.pocket.naturalist.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "parks")
public class Park {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, unique = true)
    String URLSlug;

    @Column(columnDefinition = "geometry(Polygon, 4326)")
    Polygon boundary;

    @Column(columnDefinition = "geometry(Point, 4326)")
    Point mapCenter;

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Sighting> sightings = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    Set<Animal> animals = new HashSet<>();
}
