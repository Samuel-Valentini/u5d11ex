package samuelvalentini.u5d11ex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samuelvalentini.u5d11ex.entity.Booking;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.entity.Trip;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByTripTripIdAndEmployeeEmployeeId(Long tripId, Long employeeId);

    boolean existsByTripAndEmployee(Trip trip, Employee employee);

    boolean existsByEmployeeEmployeeIdAndTripTripDate(Long employeeId, LocalDate tripDate);

    List<Booking> findByEmployeeEmployeeId(Long employeeId);

    List<Booking> findByTripTripId(Long tripId);

    boolean existsByTripTripIdAndEmployeeEmployeeIdAndBookingIdNot(Long tripId, Long employeeId, Long bookingId);

    boolean existsByEmployeeEmployeeIdAndTripTripDateAndBookingIdNot(Long employeeId, LocalDate tripDate, Long bookingId);
}

