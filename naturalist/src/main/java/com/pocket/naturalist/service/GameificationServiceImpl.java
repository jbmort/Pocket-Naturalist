package com.pocket.naturalist.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.entity.Feature;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.repository.FeatureRepository;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.UserParkStatRepository;
import com.pocket.naturalist.repository.UserRepository;

@Service
public class GameificationServiceImpl implements GameificationService{

    UserRepository userRepository;
    ParkRepository parkRepository;
    FeatureRepository featureRepository;
    UserParkStatRepository userParkStatRepository;

    Logger logger = Logger.getLogger(getClass().getName());


    // check in points for the first time visiting a park
    private static final int CHECK_IN_POINTS = 1;

    GameificationServiceImpl(UserRepository userRepository, ParkRepository parkRepository, FeatureRepository featureRepository,UserParkStatRepository userParkStatRepository){
        this.parkRepository = parkRepository;
        this.userRepository = userRepository;
        this.featureRepository = featureRepository;
        this.userParkStatRepository = userParkStatRepository;
    }

    //Methods should add points for various activities
    // // check in at park
    // // interact with park features

    // ADD 
    // include checks for badge achievement

    @Override
    public void addCheckInPointsForUser(long userId, String parkSlug) {
        Optional<User> user = userRepository.findById(userId);

        Park park = parkRepository.findByUrlSlug(parkSlug);

        if(user.isPresent()){
            User currentUser = user.orElseThrow();

            List<UserParkStat> currentStats = currentUser.getUserParkStats();

            boolean alreadyVisited = currentUser.getParkStat(parkSlug).isPresent();

            UserParkStat currentStat;

            if(alreadyVisited){
                //Ensures last check in was the previous day to prevent multiple check ins per day
                currentStat = currentUser.getParkStat(parkSlug).orElseThrow();
                if (currentStat.getLastVisited().toLocalDate().isAfter(LocalDateTime.now().toLocalDate()) ) {
                    currentStat.addPoints(CHECK_IN_POINTS);
                    List<UserParkStat> newStats = currentStats.stream().filter(s -> !s.getPark().equals(park)).collect(Collectors.toCollection(ArrayList::new));
                    newStats.add(currentStat);
                    currentUser.setUserParkStats(newStats);
                }
                else {return;}
            }
            // creates a new stat for the first time visiting a park
            else{
                currentStat = new UserParkStat(currentUser, park);
                currentStat.addPoints(CHECK_IN_POINTS);
                currentStat.setLastVisited(LocalDateTime.now());
                currentUser.addUserParkStat(currentStat);

            }

            userParkStatRepository.save(currentStat);
            userRepository.save(currentUser);
        }
        else{
            logger.info("User not found");
        }
    }

    public void awardFeaturePoints(long userId, String parkSlug, long featureId) {

        User user = userRepository.findById(userId).orElseThrow();
        Feature feature = featureRepository.findById(featureId). orElseThrow();
        UserParkStat parkStat;

        if(user.getParkStat(parkSlug).isPresent()){
            parkStat = user.getParkStat(parkSlug).orElseThrow();
        }
        else{
            addCheckInPointsForUser(userId, parkSlug);
            parkStat = user.getParkStat(parkSlug).orElseThrow();
        }

        parkStat.addPoints(feature.getPointValue());
        userParkStatRepository.save(parkStat);
    }
    
}
