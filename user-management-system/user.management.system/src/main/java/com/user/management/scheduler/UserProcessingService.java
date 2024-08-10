package com.user.management.scheduler;

import com.user.management.entity.User;
import com.user.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for processing user data periodically.
 */
@Service
public class UserProcessingService {


    @Autowired
    private UserRepository userRepository;

    /**
     * Scheduled method to process user data at a fixed interval.
     * This method updates the `lastProcessedAt` timestamp for each user
     * and saves the updated user entity to the repository.
     *
     * The method is executed every 60,000 milliseconds (60 seconds).
     */
    @Scheduled(fixedRate = 60000)
    public void processUserData() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            user.setLastProcessedAt(LocalDateTime.now());
            userRepository.save(user);
        });
    }
}
