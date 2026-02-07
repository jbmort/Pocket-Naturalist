package com.pocket.naturalist.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name="feature_visit", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "feature_id"}))
public class FeatureVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", nullable = false)
    Feature feature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_park_stat", nullable = false)
    UserParkStat userParkStat;

    @Column(name="visits")
    List<LocalDateTime> visits;


    public FeatureVisit(Feature feature, UserParkStat userParkStat){
        this.feature = feature;
        this.userParkStat = userParkStat;
        this.visits = new ArrayList<>();
        this.visits.add(LocalDateTime.now());
    }

    
    public List<LocalDateTime> getVisits() {
        return visits;
    }

    public void setVisits(List<LocalDateTime> visits) {
        this.visits = visits;
    }

    public void visit(){
        this.visits.add(LocalDateTime.now());
    }

    public LocalDateTime latestVisit(){
        return this.visits.getLast();
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public UserParkStat getUserParkStat() {
        return userParkStat;
    }


    public void setUserParkStat(UserParkStat userParkStat) {
        this.userParkStat = userParkStat;
    }




    
    
}
