package it.epicode.Capstone_Project_Backend.repository;

import it.epicode.Capstone_Project_Backend.model.Categoria;
import it.epicode.Capstone_Project_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByUtente(User utente);
}
