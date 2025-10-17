package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoomRequest {
    @NotBlank
    private String name;
    private String location;
    private Integer capacity;
    private String equipment;
}
