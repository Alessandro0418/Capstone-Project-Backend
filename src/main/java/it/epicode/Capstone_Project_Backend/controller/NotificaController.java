package it.epicode.Capstone_Project_Backend.controller;

import it.epicode.Capstone_Project_Backend.dto.NotificaDto;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.repository.UserRepository;
import it.epicode.Capstone_Project_Backend.service.NotificaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifiche")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificaController {

    private final NotificaService notificaService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<NotificaDto>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return ResponseEntity.ok(notificaService.getNotificheUtente(user));
    }

    @PatchMapping("/{id}/letta")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificaService.segnaComeLetta(id);
        return ResponseEntity.ok().build();
    }
}