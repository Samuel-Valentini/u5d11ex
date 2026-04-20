package samuelvalentini.u5d11ex.service;

import org.springframework.stereotype.Service;
import samuelvalentini.u5d11ex.dto.TripDTO;
import samuelvalentini.u5d11ex.entity.Trip;
import samuelvalentini.u5d11ex.enumeration.TripStatus;
import samuelvalentini.u5d11ex.exception.BadRequestException;
import samuelvalentini.u5d11ex.exception.NotFoundException;
import samuelvalentini.u5d11ex.repository.TripRepository;

import java.util.List;

@Service
public class TripService {
    private final TripRepository tripRepository;


    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip saveTrip(TripDTO tripDTO) {
        TripStatus parsedStatus;

        try {
            parsedStatus = TripStatus.valueOf(tripDTO.status().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status. Allowed values are: SCHEDULED, COMPLETED");
        }

        Trip trip = new Trip(tripDTO.destination(), parsedStatus, tripDTO.tripDate());
        return tripRepository.save(trip);
    }

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public Trip findById(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException(String.valueOf(tripId)));
    }

    public Trip updateTrip(Long tripId, TripDTO tripDTO) {
        Trip found = findById(tripId);

        TripStatus parsedStatus;

        try {
            parsedStatus = TripStatus.valueOf(tripDTO.status().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status. Allowed values are: SCHEDULED, COMPLETED");
        }

        found.setDestination(tripDTO.destination());
        found.setTripDate(tripDTO.tripDate());
        found.setStatus(parsedStatus);

        return tripRepository.save(found);
    }

    public Trip updateTripStatus(Long tripId, String status) {
        Trip found = findById(tripId);
        TripStatus parsedStatus;

        try {
            parsedStatus = TripStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status. Allowed values are: SCHEDULED, COMPLETED");
        }

        found.setStatus(parsedStatus);

        return tripRepository.save(found);
    }


    public void deleteTrip(Long tripId) {
        Trip found = findById(tripId);
        tripRepository.delete(found);
    }
}
