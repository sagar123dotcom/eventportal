package com.community.events.config;

import com.community.events.model.Event;
import com.community.events.repo.EventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seed(EventRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.save(make("Neighborhood Clean-Up Drive", "Volunteering",
                    "Join your neighbours to clean the park and nearby streets. Gloves and bags provided.",
                    "Central Park Main Gate", 3, 9, 100));
            repo.save(make("Open Mic Night", "Culture",
                    "Share your poetry, music or comedy. Everyone is welcome to perform or just enjoy.",
                    "Community Hall", 7, 18, 60));
            repo.save(make("Beginner Yoga in the Park", "Health",
                    "A relaxed one-hour session suitable for all levels. Bring your own mat.",
                    "Lakeside Garden", 10, 7, 25));
            repo.save(make("Local Business Meetup", "Networking",
                    "Meet local entrepreneurs, share ideas and find collaborators.",
                    "Town Library, Room 2", 14, 17, 40));
        };
    }

    private Event make(String title, String category, String desc, String location, int daysAhead, int hour, int cap) {
        Event e = new Event();
        e.setTitle(title);
        e.setCategory(category);
        e.setDescription(desc);
        e.setLocation(location);
        e.setEventDate(LocalDateTime.now().plusDays(daysAhead).withHour(hour).withMinute(0).withSecond(0).withNano(0));
        e.setCapacity(cap);
        return e;
    }
}
