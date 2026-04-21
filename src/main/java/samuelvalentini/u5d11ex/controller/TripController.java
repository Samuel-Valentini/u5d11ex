package samuelvalentini.u5d11ex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import samuelvalentini.u5d11ex.dto.TripDTO;
import samuelvalentini.u5d11ex.dto.TripStatusDTO;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.entity.Trip;
import samuelvalentini.u5d11ex.enumeration.Role;
import samuelvalentini.u5d11ex.exception.BadRequestException;
import samuelvalentini.u5d11ex.exception.UnauthorizedException;
import samuelvalentini.u5d11ex.service.BookingService;
import samuelvalentini.u5d11ex.service.TripService;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;
    private final BookingService bookingService;

    public TripController(TripService tripService, BookingService bookingService) {
        this.tripService = tripService;
        this.bookingService = bookingService;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")

    public Trip saveTrip(@RequestBody @Validated TripDTO tripDTO, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new BadRequestException(errors);
        }

        return tripService.saveTrip(tripDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public List<Trip> getAllTrips() {
        return tripService.findAll();
    }

    @GetMapping("/{tripId}")
    public Trip getTripById(@PathVariable Long tripId,
                            @AuthenticationPrincipal Employee currentEmployee) {

        if (currentEmployee.getRole() == Role.ADMIN || currentEmployee.getRole() == Role.SUPERADMIN) {
            return tripService.findById(tripId);
        }

        boolean hasBooking = bookingService.existsBookingForEmployeeInTrip(
                tripId,
                currentEmployee.getEmployeeId()
        );

        if (!hasBooking) {
            throw new UnauthorizedException("You cannot access this trip");
        }

        return tripService.findById(tripId);
    }

    @PutMapping("/{tripId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Trip updateTrip(@PathVariable Long tripId, @RequestBody @Validated TripDTO tripDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new BadRequestException(errors);
        }
        return tripService.updateTrip(tripId, tripDTO);
    }

    @PatchMapping("/{tripId}/status")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Trip updateTripStatus(@PathVariable Long tripId,
                                 @RequestBody @Validated TripStatusDTO tripStatusDTO,
                                 BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        return tripService.updateTripStatus(tripId, tripStatusDTO.status());
    }

    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public void deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
    }
}