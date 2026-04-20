package samuelvalentini.u5d11ex.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TripDTO(

        @NotBlank(message = "Destination is required")
        String destination,

        @NotBlank(message = "Status is required")
        String status,

        @NotNull(message = "Trip date is required")
        LocalDate tripDate

) {
}