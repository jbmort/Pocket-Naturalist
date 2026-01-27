package com.pocket.naturalist.entity;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "features")
public class Feature {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    long id;

    @Column(nullable = false)
    String name;

    @Column(columnDefinition = "geometry(Point, 4326)", nullable = false)
    Point location;

    @Column(nullable = false, columnDefinition = "integer default 0")
    int pointValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id", nullable = false)
    Park park;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews = new ArrayList<>();
    
    public Feature(String name, Point location, int pointValue, Park park) {
        this.name = name;
        this.location = location;
        this.pointValue = pointValue;
        this.park = park;
    }

    public Feature() {
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

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    
}
