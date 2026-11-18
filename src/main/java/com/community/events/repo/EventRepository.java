package com.community.events.repo;

import com.community.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
           select e from Event e
           where e.eventDate >= :now
             and (lower(e.title) like lower(concat('%', :q, '%'))
               or lower(e.location) like lower(concat('%', :q, '%'))
               or lower(e.category) like lower(concat('%', :q, '%')))
           order by e.eventDate
           """)
    List<Event> searchUpcoming(@Param("now") LocalDateTime now, @Param("q") String q);

    List<Event> findAllByOrderByEventDateDesc();
}
