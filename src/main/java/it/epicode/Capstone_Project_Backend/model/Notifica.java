package it.epicode.Capstone_Project_Backend.model;

import it.epicode.Capstone_Project_Backend.enumeration.TipoNotifica;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifiche")
public class Notifica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private String messaggio;

    @Enumerated(EnumType.STRING)
    private TipoNotifica tipo;

    private LocalDateTime dataCreazione;
    private boolean letta;

    private Long transazioneId;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private User utente;
}
