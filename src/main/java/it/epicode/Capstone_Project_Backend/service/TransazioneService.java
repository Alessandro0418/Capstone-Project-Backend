package it.epicode.Capstone_Project_Backend.service;

import it.epicode.Capstone_Project_Backend.dto.TransazioneDto;
import it.epicode.Capstone_Project_Backend.model.Categoria;
import it.epicode.Capstone_Project_Backend.model.Transazione;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.repository.CategoriaRepository;
import it.epicode.Capstone_Project_Backend.repository.TransazioneRepository;
import it.epicode.Capstone_Project_Backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransazioneService {

    private final TransazioneRepository transazioneRepository;
    private final UserRepository userRepository;
    private final CategoriaRepository categoriaRepository;

    public Transazione createTransazione(String username, TransazioneDto dto) {
        User utente = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        Transazione transazione = Transazione.builder()
                .descrizione(dto.getDescrizione())
                .importo(dto.getImporto())
                .data(dto.getData())
                .categoria(categoria)
                .utente(utente)
                .build();

        return transazioneRepository.save(transazione);
    }

    public List<Transazione> getAllForUser(String username) {
        User utente = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
        return transazioneRepository.findByUtente(utente);
    }

    public Transazione updateTransazione(Long id, TransazioneDto dto, String username) {
        Transazione t = transazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transazione non trovata"));

        if (!t.getUtente().getUsername().equals(username)) {
            throw new SecurityException("Non autorizzato a modificare questa transazione");
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        t.setDescrizione(dto.getDescrizione());
        t.setImporto(dto.getImporto());
        t.setData(dto.getData());
        t.setCategoria(categoria);

        return transazioneRepository.save(t);
    }

    public void deleteTransazione(Long id, String username) {
        Transazione t = transazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transazione non trovata"));

        if (!t.getUtente().getUsername().equals(username)) {
            throw new SecurityException("Non autorizzato a cancellare questa transazione");
        }

        transazioneRepository.delete(t);
    }
}