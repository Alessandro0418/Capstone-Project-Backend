package it.epicode.Capstone_Project_Backend.controller;

import it.epicode.Capstone_Project_Backend.dto.NotificaDto;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.service.NotificaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifiche")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificaController {
    private final NotificaService notificaService;

    @GetMapping
    public ResponseEntity<List<NotificaDto>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(notificaService.getNotificheUtente(user));
    }

    @PatchMapping("/{id}/letta")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificaService.segnaComeLetta(id);
        return ResponseEntity.ok().build();
    }
}
