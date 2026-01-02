package it.epicode.Capstone_Project_Backend.service;

import it.epicode.Capstone_Project_Backend.dto.TransazioneDto;
import it.epicode.Capstone_Project_Backend.enumeration.TipoNotifica;
import it.epicode.Capstone_Project_Backend.model.Categoria;
import it.epicode.Capstone_Project_Backend.model.Transazione;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.repository.CategoriaRepository;
import it.epicode.Capstone_Project_Backend.repository.TransazioneRepository;
import it.epicode.Capstone_Project_Backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import it.epicode.Capstone_Project_Backend.enumeration.IsExpense;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class TransazioneService {

    private final TransazioneRepository transazioneRepository;
    private final UserRepository userRepository;
    private final CategoriaRepository categoriaRepository;
    private final NotificaService notificaService;

    public Transazione createTransazione(String username, TransazioneDto dto) {
        User utente = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria non trovata"));

        Transazione transazione = Transazione.builder()
                .descrizione(dto.getDescrizione())
                .icona(dto.getIcona())
                .importo(dto.getImporto())
                .data(dto.getData())
                .categoria(categoria)
                .utente(utente)
                .ricorrente(dto.isRicorrente())
                .build();

        if (dto.getExpenses() != null) {
            transazione.setExpenses(dto.getExpenses());
        } else {
            transazione.setExpenses(new HashSet<>());
        }

        Transazione saved = transazioneRepository.save(transazione);

        try {
            boolean isExpense = saved.getExpenses().contains(IsExpense.EXPENSE);
            boolean isIncome = saved.getExpenses().contains(IsExpense.INCOME);

            if (isIncome) {
                notificaService.creaNotifica(
                        utente,
                        "Income Received",
                        "Nice! You’ve received a credit of " + saved.getImporto() + "€ for: " + saved.getDescrizione(),
                        TipoNotifica.INFO,
                        saved.getId()
                );
            }

            else if (isExpense) {
                if (saved.getImporto().compareTo(new BigDecimal("500")) >= 0) {
                    notificaService.creaNotifica(
                            utente,
                            "Large Expense",
                            "Heads up: you’ve recorded a high expense of " + saved.getImporto() + "€.",
                            TipoNotifica.AVVISO,
                            saved.getId()
                    );
                }
            }

            if (saved.isRicorrente()) {
                notificaService.creaNotifica(
                        utente,
                        "Scheduled Deadline",
                        "The transaction '" + saved.getDescrizione() + "' has been marked as recurring and will be monitored.",
                        TipoNotifica.SCADENZA,
                        saved.getId()
                );
            }

        } catch (Exception e) {
            System.err.println("Errore durante la creazione della notifica: " + e.getMessage());
        }

        return saved;
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
        t.setIcona(dto.getIcona());
        t.setImporto(dto.getImporto());
        t.setData(dto.getData());
        t.setCategoria(categoria);
        t.setRicorrente(dto.isRicorrente());

        if (dto.getExpenses() != null) {
            t.setExpenses(dto.getExpenses());
        } else {
            t.setExpenses(new HashSet<>());
        }

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