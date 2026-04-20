package samuelvalentini.u5d11ex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import samuelvalentini.u5d11ex.dto.TripDTO;
import samuelvalentini.u5d11ex.dto.TripStatusDTO;
import samuelvalentini.u5d11ex.entity.Trip;
import samuelvalentini.u5d11ex.exception.BadRequestException;
import samuelvalentini.u5d11ex.service.TripService;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)

    public Trip saveTrip(@RequestBody @Validated TripDTO tripDTO, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new BadRequestException(errors);
        }

        return tripService.saveTrip(tripDTO);
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripService.findAll();
    }

    @GetMapping("/{tripId}")
    public Trip getTripById(@PathVariable Long tripId) {
        return tripService.findById(tripId);
    }

    @PutMapping("/{tripId}")
    public Trip updateTrip(@PathVariable Long tripId, @RequestBody @Validated TripDTO tripDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new BadRequestException(errors);
        }
        return tripService.updateTrip(tripId, tripDTO);
    }

    @PatchMapping("/{tripId}/status")
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
    public void deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
    }
}