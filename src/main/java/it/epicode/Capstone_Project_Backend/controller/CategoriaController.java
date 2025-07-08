package it.epicode.Capstone_Project_Backend.controller;

import it.epicode.Capstone_Project_Backend.service.CategoriaService;
import it.epicode.Capstone_Project_Backend.dto.CategoriaDto;
import it.epicode.Capstone_Project_Backend.model.Categoria;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/categorie")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Categoria> crea(@RequestBody @Valid CategoriaDto dto, Authentication auth) {
        return ResponseEntity.ok(categoriaService.creaCategoria(dto, auth));
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> getAll(Authentication auth) {
        return ResponseEntity.ok(categoriaService.getCategoriePerUtente(auth));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Long id, Authentication auth) {
        return categoriaService.getCategoriaById(id, auth)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> aggiorna(@PathVariable Long id, @RequestBody @Valid CategoriaDto dto, Authentication auth) {
        return ResponseEntity.ok(categoriaService.aggiornaCategoria(id, dto, auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> elimina(@PathVariable Long id, Authentication auth) {
        categoriaService.eliminaCategoria(id, auth);
        return ResponseEntity.noContent().build();
    }
}