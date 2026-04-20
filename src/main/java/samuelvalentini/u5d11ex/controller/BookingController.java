package samuelvalentini.u5d11ex.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import samuelvalentini.u5d11ex.dto.BookingDTO;
import samuelvalentini.u5d11ex.entity.Booking;
import samuelvalentini.u5d11ex.exception.BadRequestException;
import samuelvalentini.u5d11ex.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public Booking saveBooking(@RequestBody @Validated BookingDTO bookingDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        return bookingService.saveBooking(bookingDTO);
    }

    @GetMapping({"", "/"})
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
    public Booking getBookingById(@PathVariable Long bookingId) {
        return bookingService.findById(bookingId);
    }

    @PutMapping("/{bookingId}")
    public Booking updateBooking(@PathVariable Long bookingId, @RequestBody @Validated BookingDTO bookingDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        return bookingService.updateBooking(bookingId, bookingDTO);
    }

    @GetMapping("/employee/{employeeId}")
    public List<Booking> getBookingsByEmployeeId(@PathVariable Long employeeId) {
        return bookingService.findByEmployeeId(employeeId);
    }

    @GetMapping("/trip/{tripId}")
    public List<Booking> getBookingsByTripId(@PathVariable Long tripId) {
        return bookingService.findByTripId(tripId);
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
    }
}