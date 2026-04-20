package samuelvalentini.u5d11ex.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import samuelvalentini.u5d11ex.dto.BookingDTO;
import samuelvalentini.u5d11ex.entity.Booking;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.entity.Trip;
import samuelvalentini.u5d11ex.enumeration.TripStatus;
import samuelvalentini.u5d11ex.exception.BadRequestException;
import samuelvalentini.u5d11ex.exception.NotFoundException;
import samuelvalentini.u5d11ex.repository.BookingRepository;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripService tripService;
    private final EmployeeService employeeService;

    public BookingService(BookingRepository bookingRepository, TripService tripService, EmployeeService employeeService) {
        this.bookingRepository = bookingRepository;
        this.tripService = tripService;
        this.employeeService = employeeService;
    }

    public Booking saveBooking(BookingDTO bookingDTO) {
        Trip trip = tripService.findById(bookingDTO.tripId());
        Employee employee = employeeService.findById(bookingDTO.employeeId());

        validateBookingRules(trip, employee);

        Booking booking = new Booking(trip, employee, normalizeNotes(bookingDTO.notesPreferences())
        );

        return bookingRepository.save(booking);
    }

    public Page<Booking> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("trip.tripDate").and(Sort.by("submissionDate")));
        return bookingRepository.findAll(pageable);
    }

    public Booking findById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.valueOf(bookingId)));
    }

    public Booking updateBooking(Long bookingId, BookingDTO bookingDTO) {
        Booking found = findById(bookingId);

        Trip trip = tripService.findById(bookingDTO.tripId());
        Employee employee = employeeService.findById(bookingDTO.employeeId());

        validateBookingRulesForUpdate(bookingId, trip, employee);

        found.setTrip(trip);
        found.setEmployee(employee);
        found.setNotesPreferences(normalizeNotes(bookingDTO.notesPreferences()));

        return bookingRepository.save(found);
    }

    public List<Booking> findByEmployeeId(Long employeeId) {
        employeeService.findById(employeeId);
        return bookingRepository.findByEmployeeEmployeeId(employeeId);
    }

    public List<Booking> findByTripId(Long tripId) {
        tripService.findById(tripId);
        return bookingRepository.findByTripTripId(tripId);
    }

    public void deleteBooking(Long bookingId) {
        Booking found = findById(bookingId);
        bookingRepository.delete(found);
    }

    private void validateBookingRules(Trip trip, Employee employee) {
        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new BadRequestException("You cannot create a booking for a completed trip");
        }

        if (bookingRepository.existsByTripTripIdAndEmployeeEmployeeId(
                trip.getTripId(), employee.getEmployeeId())) {
            throw new BadRequestException("This employee is already assigned to this trip");
        }

        if (bookingRepository.existsByEmployeeEmployeeIdAndTripTripDate(
                employee.getEmployeeId(), trip.getTripDate())) {
            throw new BadRequestException("This employee already has a booking on " + trip.getTripDate());
        }
    }

    private void validateBookingRulesForUpdate(Long bookingId, Trip trip, Employee employee) {
        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new BadRequestException("You cannot assign an employee to a completed trip");
        }

        if (bookingRepository.existsByTripTripIdAndEmployeeEmployeeIdAndBookingIdNot(
                trip.getTripId(), employee.getEmployeeId(), bookingId)) {
            throw new BadRequestException("This employee is already assigned to this trip");
        }

        if (bookingRepository.existsByEmployeeEmployeeIdAndTripTripDateAndBookingIdNot(
                employee.getEmployeeId(), trip.getTripDate(), bookingId)) {
            throw new BadRequestException("This employee already has a booking on " + trip.getTripDate());
        }
    }

    private String normalizeNotes(String notesPreferences) {
        if (notesPreferences == null || notesPreferences.isBlank()) {
            return null;
        }
        return notesPreferences.trim();
    }
}