package it.epicode.Capstone_Project_Backend.service;

import it.epicode.Capstone_Project_Backend.dto.CategoryTotalDTO;
import it.epicode.Capstone_Project_Backend.enumeration.IsExpense;
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

        BigDecimal entrate = BigDecimal.ZERO;
        BigDecimal uscite = BigDecimal.ZERO;
        BigDecimal saldo = BigDecimal.ZERO;

        for (Transazione t : transazioni) {
            if (t.getExpenses() != null && t.getExpenses().contains(IsExpense.INCOME)) {
                entrate = entrate.add(t.getImporto());
            } else if (t.getExpenses() != null && t.getExpenses().contains(IsExpense.EXPENSE)) {
                uscite = uscite.add(t.getImporto());
            }
        }

        saldo = entrate.subtract(uscite);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("mese", inizioMese.getMonth().toString());
        response.put("anno", anno);
        response.put("entrateTotali", entrate);
        response.put("usciteTotali", uscite);
        response.put("saldo", saldo);

        return response;
    }

    public List<CategoryTotalDTO> getTotaliPerCategoria(String username, int mese, int anno) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return transazioneRepository.findTotalsByCategoryAndMonthAndYearForExpenses(
                user.getId(),
                mese,
                anno,
                IsExpense.EXPENSE
        );
    }
}
