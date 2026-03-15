package com.pocket.naturalist.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pocket.naturalist.entity.Badge;
import com.pocket.naturalist.entity.Feature;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.User;
import com.pocket.naturalist.entity.UserParkStat;
import com.pocket.naturalist.repository.FeatureRepository;
import com.pocket.naturalist.repository.ParkRepository;
import com.pocket.naturalist.repository.UserParkStatRepository;
import com.pocket.naturalist.repository.UserRepository;

@Service
public class GameificationServiceImpl implements GameificationService {

    UserRepository userRepository;
    ParkRepository parkRepository;
    FeatureRepository featureRepository;
    UserParkStatRepository userParkStatRepository;
    BadgeService badgeService;

    Logger logger = Logger.getLogger(getClass().getName());

    // check in points for the first time visiting a park
    private static final int CHECK_IN_POINTS = 1;

    GameificationServiceImpl(UserRepository userRepository,
            ParkRepository parkRepository,
            FeatureRepository featureRepository,
            UserParkStatRepository userParkStatRepository,
            BadgeService badgeService) {
        this.parkRepository = parkRepository;
        this.userRepository = userRepository;
        this.featureRepository = featureRepository;
        this.userParkStatRepository = userParkStatRepository;
        this.badgeService = badgeService;
    }

    // Methods should add points for various activities
    // // check in at park
    // // interact with park features
    // // check for badge achievement

    /**
     * Adds points for the user for checking in at a park if they have not visited
     * yet that day.
     * Also checks for milestone achievement if it the users first daily visit.
     * 
     * @param userId
     * @param parkSlug
     */
    @Override
    public void addCheckInPointsForUser(String username, String parkSlug) {
        Optional<User> user = userRepository.findByUsername(username);

        Optional<Park> optionalPark = parkRepository.findByUrlSlug(parkSlug);

        if (optionalPark.isPresent()) {
            Park park = optionalPark.get();

            if (user.isPresent()) {
                User currentUser = user.orElseThrow();

                List<UserParkStat> currentStats = currentUser.getUserParkStats();

                boolean alreadyVisited = currentUser.getParkStat(parkSlug).isPresent();

                UserParkStat currentStat;

                if (alreadyVisited) {
                    // Ensures last check in was the previous day to prevent multiple check ins per
                    // day
                    currentStat = currentUser.getParkStat(parkSlug).orElseThrow();
                    if (currentStat.getLastVisited().toLocalDate().isAfter(LocalDateTime.now().toLocalDate())) {
                        currentStat.addPoints(CHECK_IN_POINTS);
                        List<UserParkStat> newStats = currentStats.stream()
                                .filter(s -> !s.getPark().equals(park))
                                .collect(Collectors.toCollection(ArrayList::new));
                        newStats.add(currentStat);
                        currentUser.setUserParkStats(newStats);
                        checkForMilestoneBadgeAward(currentUser, park);

                    } else {
                        return;
                    }
                }
                // creates a new stat for the first time visiting a park
                else {
                    currentStat = new UserParkStat(currentUser, park);
                    currentStat.addPoints(CHECK_IN_POINTS);
                    currentStat.setLastVisited(LocalDateTime.now());
                    currentUser.addUserParkStat(currentStat);
                }

                userParkStatRepository.save(currentStat);
                userRepository.save(currentUser);
            } else {
                logger.info("User not found");
            }
        } else {
            logger.log(Level.INFO, "No park found for {}", parkSlug);
        }
    }

    /**
     * Awards the appropriate amount of points to the user based on the point value
     * of the feature they have interacted with
     * 
     * @param userId
     * @param parkSlug
     * @param featureId
     */
    public void awardFeaturePoints(String username, String parkSlug, long featureId) {

        User user = userRepository.findByUsername(username).orElseThrow();
        Feature feature = featureRepository.findById(featureId).orElseThrow();
        UserParkStat parkStat;

        if (user.getParkStat(parkSlug).isPresent()) {
            parkStat = user.getParkStat(parkSlug).orElseThrow();
        } else {
            addCheckInPointsForUser(username, parkSlug);
            parkStat = user.getParkStat(parkSlug).orElseThrow();
        }

        parkStat.addPoints(feature.getPointValue());
        userParkStatRepository.save(parkStat);
    }

    /**
     * Checks if the user has achieved a new visitation milestone for the park and
     * awards them if appropriate.
     * 
     * @param user the user entity to be checked
     * @param park the park entity to check visitation milestones for
     * 
     * @return boolean value that represents if a new badge was awarded or not. Will
     *         update the user entity with a new badge and save the user if a badge
     *         is awarded.
     */
    public boolean checkForMilestoneBadgeAward(User user, Park park) {
        List<Badge> badges = user.getBadges();
        int visits = user.getParkStat(park.getUrlSlug()).orElseThrow().getNumberOfVisits();

        if (visits >= 10) {

            for (int milestone : badgeService.getBadgeMilestones()) {
                if (visits >= milestone && badges.stream()
                        .noneMatch(b -> b.getName().equals(park.getName() + " : " + milestone + " Visits"))) {
                    Badge newBadge = badgeService.createMilestoneBadge(park.getName(), milestone);
                    user.addBadge(newBadge);
                    userRepository.save(user);
                    return true;
                }
            }
        }
        return false;

    }

    @Override
    public void awardPointsForFeatureCheckIn(String username, long featureId, String parkSlug) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Feature feature = featureRepository.findById(featureId).orElseThrow();

        user.getParkStat(parkSlug).orElseThrow().visitFeature(feature);

        userRepository.save(user);
    }

}
