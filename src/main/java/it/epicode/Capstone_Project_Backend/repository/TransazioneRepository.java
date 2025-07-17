package it.epicode.Capstone_Project_Backend.repository;

import it.epicode.Capstone_Project_Backend.dto.CategoryTotalDTO;
import it.epicode.Capstone_Project_Backend.enumeration.IsExpense;
import it.epicode.Capstone_Project_Backend.model.Transazione;
import it.epicode.Capstone_Project_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransazioneRepository extends JpaRepository<Transazione, Long> {
    List<Transazione> findByUtente(User utente);

    List<Transazione> findByUtenteAndDataBetween(User user, LocalDate inizioMese, LocalDate fineMese);

    @Query("SELECT new it.epicode.Capstone_Project_Backend.dto.CategoryTotalDTO(t.categoria.name, SUM(t.importo)) " +
            "FROM Transazione t " +
            "JOIN t.expenses e " +
            "WHERE t.utente.id = :userId " +
            "AND MONTH(t.data) = :month " +
            "AND YEAR(t.data) = :year " +
            "AND e = :expenseType " +
            "GROUP BY t.categoria.name")
    List<CategoryTotalDTO> findTotalsByCategoryAndMonthAndYearForExpenses(
            @Param("userId") Long userId,
            @Param("month") int month,
            @Param("year") int year,
            IsExpense expenseType);
}
