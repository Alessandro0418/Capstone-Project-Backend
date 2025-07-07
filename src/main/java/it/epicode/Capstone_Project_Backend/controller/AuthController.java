package it.epicode.Capstone_Project_Backend.controller;

import it.epicode.Capstone_Project_Backend.dto.UserDto;
import it.epicode.Capstone_Project_Backend.enumeration.Role;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.security.JwtUtil;
import it.epicode.Capstone_Project_Backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService utenteService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDto dto) {
        User nuovoUser = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getNome())
                .cognome(dto.getCognome())
                .avatar(dto.getAvatar())
                .build();


        Set<Role> ruoli = dto.getRuoli() != null && !dto.getRuoli().isEmpty()
                ? dto.getRuoli()
                : Collections.singleton(Role.USER);

        User salvato = utenteService.registraUser(nuovoUser, ruoli);
        return ResponseEntity.ok(salvato);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginData.get("username"),
                        loginData.get("password")
                )
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String roles = String.join(",", userDetails.getAuthorities().stream().map(a -> a.getAuthority()).toList());
        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);

        return ResponseEntity.ok(Map.of("token", token));
    }
    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        String username = authentication.getName();

        return utenteService.findByUsername(username)
                .map(utente -> ResponseEntity.ok(Map.of(
                        "username", utente.getUsername(),
                        "nome", utente.getName(),
                        "cognome", utente.getCognome(),
                        "email", utente.getEmail(),
                        "avatar", utente.getAvatar(),
                        "ruoli", utente.getRuoli().stream().map(Enum::name).toList()
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}