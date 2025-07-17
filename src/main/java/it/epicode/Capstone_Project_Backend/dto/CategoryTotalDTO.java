package it.epicode.Capstone_Project_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTotalDTO {
    private String categoria;
    private BigDecimal totale;
}