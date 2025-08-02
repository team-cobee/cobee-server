package org.cobee.server.publicProfile.repository;

import org.cobee.server.publicProfile.domain.PublicProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicProfileRepository extends JpaRepository<PublicProfile, Long> {
}

