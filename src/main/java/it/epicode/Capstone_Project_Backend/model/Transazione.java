package it.epicode.Capstone_Project_Backend.model;

import it.epicode.Capstone_Project_Backend.enumeration.IsExpense;
import it.epicode.Capstone_Project_Backend.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transazioni")
public class Transazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descrizione;

    private String icona;

    private BigDecimal importo;

    private LocalDate data;

    @Column(nullable = false)
    private boolean ricorrente = false;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private User utente;

    @ElementCollection(targetClass = IsExpense.class)
    @CollectionTable(name = "transazione_expenses", joinColumns = @JoinColumn(name = "transazione_id"))
    @Column(name = "expense_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<IsExpense> expenses = new HashSet<>();
}
