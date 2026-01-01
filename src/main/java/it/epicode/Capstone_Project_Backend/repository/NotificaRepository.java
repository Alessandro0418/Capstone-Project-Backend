package it.epicode.Capstone_Project_Backend.repository;

import it.epicode.Capstone_Project_Backend.model.Notifica;
import it.epicode.Capstone_Project_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificaRepository extends JpaRepository<Notifica, Long> {
    List<Notifica> findByUtenteOrderByDataCreazioneDesc(User utente);

    long countByUtenteAndLettaFalse(User utente);
}
