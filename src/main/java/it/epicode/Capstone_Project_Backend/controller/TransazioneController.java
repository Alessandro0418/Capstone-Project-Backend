package it.epicode.Capstone_Project_Backend.controller;


import it.epicode.Capstone_Project_Backend.dto.TransazioneDto;
import it.epicode.Capstone_Project_Backend.model.Transazione;
import it.epicode.Capstone_Project_Backend.service.TransazioneService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/transazioni")
@RequiredArgsConstructor
public class TransazioneController {

    private final TransazioneService transazioneService;

    @PostMapping
    public ResponseEntity<Transazione> create(@RequestBody @Valid TransazioneDto dto,
                                              Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(transazioneService.createTransazione(username, dto));
    }

    @GetMapping
    public ResponseEntity<List<Transazione>> getAll(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(transazioneService.getAllForUser(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transazione> update(@PathVariable Long id,
                                              @RequestBody @Valid TransazioneDto dto,
                                              Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(transazioneService.updateTransazione(id, dto, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        transazioneService.deleteTransazione(id, username);
        return ResponseEntity.noContent().build();
    }
}