package samuelvalentini.u5d11ex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samuelvalentini.u5d11ex.entity.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
