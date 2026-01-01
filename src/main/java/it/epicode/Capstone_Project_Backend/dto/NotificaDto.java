package it.epicode.Capstone_Project_Backend.dto;

import it.epicode.Capstone_Project_Backend.enumeration.TipoNotifica;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificaDto {
    private Long id;
    private String titolo;
    private String messaggio;
    private TipoNotifica tipo;
    private LocalDateTime dataCreazione;
    private boolean letta;
    private Long transazioneId;
}
