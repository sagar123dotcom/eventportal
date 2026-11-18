package com.community.events.repo;

import com.community.events.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByEventIdAndEmailIgnoreCase(Long eventId, String email);
}
