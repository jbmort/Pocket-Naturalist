package com.pocket.naturalist.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name = "park_visits", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_park_stat_id", "visit_date"})
})
public class ParkVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_park_stat_id", nullable = false)
    private UserParkStat userParkStat;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    @Column(name = "is_first_ever_visit", nullable = false)
    private boolean firstEverVisit;

    public ParkVisit() {
    }

    public ParkVisit(UserParkStat userParkStat, boolean isFirstEverVisit) {
        this.userParkStat = userParkStat;
        this.checkInTime = LocalDateTime.now();
        this.visitDate = this.checkInTime.toLocalDate(); 
        this.firstEverVisit = isFirstEverVisit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserParkStat getUserParkStat() { return userParkStat; }
    public void setUserParkStat(UserParkStat userParkStat) { this.userParkStat = userParkStat; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public boolean isFirstEverVisit() { return firstEverVisit; }
    public void setFirstEverVisit(boolean firstEverVisit) { this.firstEverVisit = firstEverVisit; }
}