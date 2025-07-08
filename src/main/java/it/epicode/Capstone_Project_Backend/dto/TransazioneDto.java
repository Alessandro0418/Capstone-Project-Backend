package it.epicode.Capstone_Project_Backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransazioneDto {

    @NotBlank
    private String descrizione;

    @NotNull
    @DecimalMin(value = "0.01", message = "L'importo deve essere maggiore di zero")
    private BigDecimal importo;

    @NotNull
    private LocalDate data;

    @NotNull
    private Long categoriaId;
}
