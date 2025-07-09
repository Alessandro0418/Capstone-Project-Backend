package it.epicode.Capstone_Project_Backend.repository;

import it.epicode.Capstone_Project_Backend.model.Transazione;
import it.epicode.Capstone_Project_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransazioneRepository extends JpaRepository<Transazione, Long> {
    List<Transazione> findByUtente(User utente);

    List<Transazione> findByUtenteAndDataBetween(User user, LocalDate inizioMese, LocalDate fineMese);
}
