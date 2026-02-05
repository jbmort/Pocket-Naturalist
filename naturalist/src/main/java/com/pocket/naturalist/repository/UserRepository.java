package com.pocket.naturalist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pocket.naturalist.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String string);


}
