package it.epicode.Capstone_Project_Backend.service;

import it.epicode.Capstone_Project_Backend.dto.NotificaDto;
import it.epicode.Capstone_Project_Backend.enumeration.TipoNotifica;
import it.epicode.Capstone_Project_Backend.model.Notifica;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.repository.NotificaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificaService {

    private final NotificaRepository notificaRepository;

    public List<NotificaDto> getNotificheUtente(User utente) {
        return notificaRepository.findByUtenteOrderByDataCreazioneDesc(utente)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void segnaComeLetta(Long id) {
        Notifica n = notificaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notifica non trovata"));
        n.setLetta(true);
        notificaRepository.save(n);
    }

    private NotificaDto convertToDto(Notifica n) {
        NotificaDto dto = new NotificaDto();
        dto.setId(n.getId());
        dto.setTitolo(n.getTitolo());
        dto.setMessaggio(n.getMessaggio());
        dto.setTipo(n.getTipo());
        dto.setDataCreazione(n.getDataCreazione());
        dto.setLetta(n.isLetta());
        dto.setTransazioneId(n.getTransazioneId());
        return dto;
    }

    public void creaNotifica(User utente, String titolo, String messaggio, TipoNotifica tipo, Long transazioneId) {
        Notifica n = Notifica.builder()
                .utente(utente)
                .titolo(titolo)
                .messaggio(messaggio)
                .tipo(tipo)
                .dataCreazione(LocalDateTime.now())
                .letta(false)
                .transazioneId(transazioneId)
                .build();
        notificaRepository.save(n);
    }
}
