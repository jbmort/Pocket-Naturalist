package com.pocket.naturalist.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

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
    String urlSlug;

    @Column(columnDefinition = "geometry(Polygon, 4326)")
    List<Polygon> boundaryList;

    @Column(columnDefinition = "geometry(Point, 4326)")
    Point mapCenter;

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Sighting> sightings = new ArrayList<>();

    @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Feature> features = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    Set<Animal> animals = new HashSet<>();

    

    public Park(String name) {
        this.name = name;
        this.urlSlug = createSlug(name);
    }

   public Park(String name, List<Polygon> boundaries, Point mapCenter) {
         this.name = name;
         this.urlSlug = createSlug(name);
         this.boundaryList = boundaries;
         this.mapCenter = mapCenter;
   }

    private String createSlug(String parkName) {
        String slug = "General-Park";
        if(!parkName.isEmpty()){
            slug = parkName.toLowerCase().trim().replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "-");
        }
        return slug;
    }

    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlSlug() {
        return urlSlug;
    }

    public void setUrlSlug(String uRLSlug) {
        urlSlug = uRLSlug;
    }

    public List<Polygon> getBoundaryList() {
        return boundaryList;
    }

    public void setBoundaryList(List<Polygon> boundaryList) {
        this.boundaryList = boundaryList;
    }

    public void addBoundary(Polygon boundary) {
        if(this.boundaryList == null){
            this.boundaryList = new ArrayList<>();
        }
        this.boundaryList.add(boundary);
    }

    public Point getMapCenter() {
        return mapCenter;
    }

    public void setMapCenter(Point mapCenter) {
        this.mapCenter = mapCenter;
    }

    public List<Sighting> getSightings() {
        return sightings;
    }

    public void setSightings(List<Sighting> sightings) {
        this.sightings = sightings;
    }

    public Set<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public void addFeature(Feature feature){
        this.features.add(feature);
    }
}
