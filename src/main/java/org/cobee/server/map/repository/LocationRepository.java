package org.cobee.server.map.repository;

import java.util.List;
import org.cobee.server.map.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(value = """
                SELECT l.*
                FROM location l
                WHERE (6371 * acos(
                           cos(radians(:lat)) * cos(radians(l.latitude)) *
                           cos(radians(l.longitude) - radians(:lng)) +
                           sin(radians(:lat)) * sin(radians(l.latitude))
                       )) <= :radius
            """, nativeQuery = true)
    List<Location> findWithinDistance(@Param("lat") double lat,
                                      @Param("lng") double lng,
                                      @Param("radius") double radiusKm);

}

