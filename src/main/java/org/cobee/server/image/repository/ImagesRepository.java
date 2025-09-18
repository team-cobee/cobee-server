package org.cobee.server.image.repository;

import org.cobee.server.image.domain.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
}
