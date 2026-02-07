package com.pocket.naturalist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.UserRepository;

@Service
public class GameificationServiceImpl implements GameificationService{

    UserRepository userRepository;
    ParkRepository parkRepository;

    private static final int CHECK_IN_POINTS = 1;

    GameificationServiceImpl(UserRepository userRepository, ParkRepository parkRepository){
        this.parkRepository = parkRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addCheckInPointsForUser(long userId, String parkSlug) {
        Optional<User> user = userRepository.findById(userId);

        Park park = parkRepository.findByUrlSlug(parkSlug);

        if(user.isPresent()){
            User currentUser = user.orElseThrow();

            List<UserParkStat> currentStats = currentUser.getUserParkStats();

            List<UserParkStat> filteredStats = currentStats.stream()
                                                            .filter(s -> s.getPark().equals(park))
                                                            .toList();
            UserParkStat currentStat;

            if(!filteredStats.isEmpty()){
                currentStat = filteredStats.getFirst();
                currentStat.addPoints(CHECK_IN_POINTS);
                List<UserParkStat> newStats = currentStats.stream().filter(s -> !s.getPark().equals(park)).toList();
                newStats.add(currentStat);
                currentUser.setUserParkStats(newStats);
            }
            else{
                currentStat = new UserParkStat(user.orElse(new User()), park);
                currentStat.addPoints(CHECK_IN_POINTS);
                currentUser.addUserParkStat(currentStat);
            }
            
            userRepository.save(currentUser);
        }
        else{
            System.out.println("User not found");
        }
    }
    
}
