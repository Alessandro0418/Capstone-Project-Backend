package it.epicode.Capstone_Project_Backend.service;

import it.epicode.Capstone_Project_Backend.dto.CategoriaDto;
import it.epicode.Capstone_Project_Backend.model.Categoria;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.repository.CategoriaRepository;
import it.epicode.Capstone_Project_Backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import it.epicode.Capstone_Project_Backend.repository.TransazioneRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UserRepository userRepository;
    private final TransazioneRepository transazioneRepository;

    public Categoria creaCategoria(CategoriaDto dto, Authentication authentication) {
        User utente = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Categoria categoria = Categoria.builder()
                .name(dto.getName())
                .color(dto.getColor())
                .utente(utente)
                .build();
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> getCategoriePerUtente(Authentication authentication) {
        User utente = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return categoriaRepository.findByUtente(utente);
    }

    public Optional<Categoria> getCategoriaById(Long id, Authentication authentication) {
        User utente = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return categoriaRepository.findById(id)
                .filter(cat -> cat.getUtente().getId().equals(utente.getId()));
    }

    public Categoria aggiornaCategoria(Long id, CategoriaDto dto, Authentication authentication) {
        Categoria categoria = getCategoriaById(id, authentication)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        categoria.setName(dto.getName());
        categoria.setColor(dto.getColor());
        return categoriaRepository.save(categoria);
    }

    public void eliminaCategoria(Long id, Authentication authentication) {
        Categoria categoria = getCategoriaById(id, authentication)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));
        long numeroTransazioniAssociate = transazioneRepository.countByCategoria(categoria);
        if (numeroTransazioniAssociate > 0) {
            throw new IllegalStateException("Impossibile eliminare la categoria '" + categoria.getName() +
                    "' perché ci sono " + numeroTransazioniAssociate + " transazioni ad essa associate. " +
                    "Rimuovi prima le transazioni.");
        }
        categoriaRepository.delete(categoria);
    }
}
