package samuelvalentini.u5d11ex.dto;

import jakarta.validation.constraints.NotBlank;

public record TripStatusDTO(
        @NotBlank(message = "Status is required")
        String status
) {
}
