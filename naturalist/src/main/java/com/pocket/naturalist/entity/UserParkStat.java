package com.pocket.naturalist.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_park_stats", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "park_id"}))
public class UserParkStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "park_id", nullable = false)
    private Park park;

    // --- DATA ---
    
    @Column(nullable = false)
    private int currentYearPoints = 0;

    @Column(nullable = false)
    private int lifetimePoints = 0;

    @UpdateTimestamp
    private LocalDateTime lastVisited;

    @OneToMany(mappedBy = "userParkStat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeatureVisit> featuresVisited;

    public UserParkStat(User user, Park park) {
        this.user = user;
        this.park = park;
    }

    public void addPoints(int points) {
        this.currentYearPoints += points;
        this.lifetimePoints += points;
    }

    public void resetCurrentYearPoints() {
        this.currentYearPoints = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Park getPark() {
        return park;
    }

    public void setPark(Park park) {
        this.park = park;
    }

    public int getCurrentYearPoints() {
        return currentYearPoints;
    }

    public void setCurrentYearPoints(int currentYearPoints) {
        this.currentYearPoints = currentYearPoints;
    }

    public int getLifetimePoints() {
        return lifetimePoints;
    }

    public void setLifetimePoints(int lifetimePoints) {
        this.lifetimePoints = lifetimePoints;
    }

    public LocalDateTime getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(LocalDateTime lastVisited) {
        this.lastVisited = lastVisited;
    }

    public List<FeatureVisit> getFeaturesVisited() {
        return featuresVisited;
    }

    public void setFeaturesVisited(List<FeatureVisit> featuresVisited) {
        this.featuresVisited = featuresVisited;
    }

    
    
}
