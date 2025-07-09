package it.epicode.Capstone_Project_Backend.controller;

import it.epicode.Capstone_Project_Backend.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/riepilogo-mensile")
    public ResponseEntity<?> riepilogoMensile(
            Authentication authentication,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().monthValue}") int mese,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().year}") int anno
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(dashboardService.getRiepilogoMensile(username, mese, anno));
    }

    @GetMapping("/per-categoria")
    public ResponseEntity<?> perCategoria(
            Authentication authentication,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().monthValue}") int mese,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().year}") int anno
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(dashboardService.getTotaliPerCategoria(username, mese, anno));
    }
}