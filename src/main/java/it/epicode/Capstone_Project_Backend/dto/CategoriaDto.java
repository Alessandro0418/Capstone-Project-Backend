package it.epicode.Capstone_Project_Backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaDto {

    @NotBlank
    private String name;

    @NotBlank
    private String color;
}
