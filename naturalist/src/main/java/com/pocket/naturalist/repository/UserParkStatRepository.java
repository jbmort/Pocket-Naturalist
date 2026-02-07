package com.pocket.naturalist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pocket.naturalist.entity.UserParkStat;

public interface UserParkStatRepository extends JpaRepository<UserParkStat, Long> {
    
}
