package samuelvalentini.u5d11ex.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import samuelvalentini.u5d11ex.enumeration.TripStatus;

import java.time.LocalDate;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id", nullable = false, updatable = false)
    private Long tripId;

    @NotBlank(message = "Field is required")
    @Size(max = 255, message = "Field must be at most 255 characters long")
    @Column(name = "destination", nullable = false)
    private String destination;

    @NotNull(message = "Field is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TripStatus status;

    @NotNull(message = "Field is required")
    @Column(name = "trip_date", nullable = false)
    private LocalDate tripDate;

    public Trip(String destination, TripStatus status, LocalDate tripDate) {
        this.destination = destination;
        this.status = status;
        this.tripDate = tripDate;
    }

    protected Trip() {
    }

    public Long getTripId() {
        return tripId;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }
}
