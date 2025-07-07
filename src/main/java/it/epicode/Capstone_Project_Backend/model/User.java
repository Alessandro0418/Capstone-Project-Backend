package it.epicode.Capstone_Project_Backend.model;

import it.epicode.Capstone_Project_Backend.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Utenti")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String cognome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "utente_ruoli", joinColumns = @JoinColumn(name = "utente_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo")
    private Set<Role> ruoli = new HashSet<>();
}