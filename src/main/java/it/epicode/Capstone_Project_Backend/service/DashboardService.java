package it.epicode.Capstone_Project_Backend.service;

import it.epicode.Capstone_Project_Backend.model.Transazione;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.repository.TransazioneRepository;
import it.epicode.Capstone_Project_Backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransazioneRepository transazioneRepository;
    private final UserRepository userRepository;

    public Map<String, Object> getRiepilogoMensile(String username, int mese, int anno) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        LocalDate inizioMese = LocalDate.of(anno, mese, 1);
        LocalDate fineMese = inizioMese.withDayOfMonth(inizioMese.lengthOfMonth());

        List<Transazione> transazioni = transazioneRepository
                .findByUtenteAndDataBetween(user, inizioMese, fineMese);

        BigDecimal entrate = transazioni.stream()
                .filter(t -> t.getImporto().compareTo(BigDecimal.ZERO) > 0)
                .map(Transazione::getImporto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal uscite = transazioni.stream()
                .filter(t -> t.getImporto().compareTo(BigDecimal.ZERO) < 0)
                .map(Transazione::getImporto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .abs();

        BigDecimal saldo = entrate.subtract(uscite);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("mese", inizioMese.getMonth().toString());
        response.put("anno", anno);
        response.put("entrateTotali", entrate);
        response.put("usciteTotali", uscite);
        response.put("saldo", saldo);

        return response;
    }

    public List<Map<String, Object>> getTotaliPerCategoria(String username, int mese, int anno) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        LocalDate inizioMese = LocalDate.of(anno, mese, 1);
        LocalDate fineMese = inizioMese.withDayOfMonth(inizioMese.lengthOfMonth());

        List<Transazione> transazioni = transazioneRepository
                .findByUtenteAndDataBetween(user, inizioMese, fineMese);

        return transazioni.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategoria().getName(),
                        Collectors.mapping(Transazione::getImporto,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Object> mappa = new HashMap<>();
                    mappa.put("categoria", entry.getKey());
                    mappa.put("totale", entry.getValue());
                    return mappa;
                })
                .collect(Collectors.toList());
    }
}
