package samuelvalentini.u5d11ex.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "bookings", uniqueConstraints = @UniqueConstraint(columnNames = {"trip_id", "employee_id"}))
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", nullable = false, updatable = false)
    private Long bookingId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Size(max = 255, message = "Field must be at most 255 characters long")
    @Column(name = "notes_preferences")
    private String notesPreferences;

    @NotNull(message = "Field is required")
    @PastOrPresent
    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    public Booking(Trip trip, Employee employee, String notesPreferences) {
        this.trip = trip;
        this.employee = employee;
        this.notesPreferences = notesPreferences;
        this.submissionDate = LocalDate.now();
    }

    protected Booking() {
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getNotesPreferences() {
        return notesPreferences;
    }

    public void setNotesPreferences(String notesPreferences) {
        this.notesPreferences = notesPreferences;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

}
