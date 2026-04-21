package samuelvalentini.u5d11ex.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import samuelvalentini.u5d11ex.dto.BookingDTO;
import samuelvalentini.u5d11ex.entity.Booking;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.enumeration.Role;
import samuelvalentini.u5d11ex.exception.BadRequestException;
import samuelvalentini.u5d11ex.exception.UnauthorizedException;
import samuelvalentini.u5d11ex.service.BookingService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public Booking saveBooking(@RequestBody @Validated BookingDTO bookingDTO, BindingResult validationResult, @AuthenticationPrincipal Employee currentEmployee) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        boolean isAdmin = currentEmployee.getRole() == Role.ADMIN
                || currentEmployee.getRole() == Role.SUPERADMIN;

        boolean isOwner = Objects.equals(
                currentEmployee.getEmployeeId(),
                bookingDTO.employeeId());

        if (!isOwner && !isAdmin) throw new UnauthorizedException("You cannot create a booking for another employee");


        return bookingService.saveBooking(bookingDTO);
    }

    @GetMapping({"", "/"})
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Page<Booking> getAllBookings(@RequestParam(defaultValue = "0") String page) {
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new BadRequestException("The page index must be a number.");
        }

        if (pageNumber < 0) {
            throw new BadRequestException("The page index must be 0 or greater.");
        }

        return bookingService.findAll(pageNumber);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@PathVariable Long bookingId, @AuthenticationPrincipal Employee currentEmployee) {
        Booking booking = bookingService.findById(bookingId);
        if (currentEmployee.getRole().name().equals(Role.ADMIN.name()) || currentEmployee.getRole().name().equals(Role.SUPERADMIN.name())) {
            return booking;
        }
        if (!Objects.equals(currentEmployee.getEmployeeId(), booking.getEmployee().getEmployeeId()))
            throw new UnauthorizedException("This booking is not associated with you");
        return booking;
    }

    @PutMapping("/{bookingId}")
    public Booking updateBooking(@PathVariable Long bookingId, @RequestBody @Validated BookingDTO bookingDTO, BindingResult validationResult, @AuthenticationPrincipal Employee currentEmployee) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        Booking existingBooking = bookingService.findById(bookingId);

        if (currentEmployee.getRole() == Role.ADMIN || currentEmployee.getRole() == Role.SUPERADMIN) {
            return bookingService.updateBooking(bookingId, bookingDTO);

        }
        if (!Objects.equals(currentEmployee.getEmployeeId(), existingBooking.getEmployee().getEmployeeId()))
            throw new UnauthorizedException("This booking is not associated with you");
        if (!Objects.equals(bookingDTO.employeeId(), currentEmployee.getEmployeeId()))
            throw new UnauthorizedException("You cannot change the employee assigned to this booking");
        return bookingService.updateBooking(bookingId, bookingDTO);

    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public List<Booking> getBookingsByEmployeeId(@PathVariable Long employeeId) {
        return bookingService.findByEmployeeId(employeeId);
    }

    @GetMapping("/employee/me")
    public List<Booking> getBookingsByEmployeeId(@AuthenticationPrincipal Employee currentEmployee) {
        return bookingService.findByEmployeeId(currentEmployee.getEmployeeId());
    }


    @GetMapping("/trip/{tripId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public List<Booking> getBookingsByTripId(@PathVariable Long tripId) {
        return bookingService.findByTripId(tripId);
    }


    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long bookingId, @AuthenticationPrincipal Employee currentEmployee) {
        Booking existingBooking = bookingService.findById(bookingId);
        if (currentEmployee.getRole() != Role.ADMIN
                && currentEmployee.getRole() != Role.SUPERADMIN && !Objects.equals(currentEmployee.getEmployeeId(), existingBooking.getEmployee().getEmployeeId()))
            throw new UnauthorizedException("You cannot delete this booking");
        bookingService.deleteBooking(bookingId);
    }
}