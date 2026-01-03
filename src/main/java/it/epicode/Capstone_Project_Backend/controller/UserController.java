package it.epicode.Capstone_Project_Backend.controller;

import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // PUT /users/me – Modifica dati profilo
    @PutMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody @Valid User updatedUser, Authentication authentication) {
        String username = authentication.getName(); // Ottiene username dall'utente loggato
        User user = userService.updateUser(username, updatedUser);
        return ResponseEntity.ok(user);
    }

    // DELETE /users/me – Elimina profilo
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        String username = authentication.getName(); // Ottiene username dall'utente loggato
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    // POST /users/me/avatar – Caricamento immagine profilo
    @PostMapping("/me/avatar")
    public ResponseEntity<User> uploadAvatar(@RequestParam("file") MultipartFile file, Authentication authentication) {
        String username = authentication.getName();
        User updatedUser = userService.uploadAvatar(username, file);
        return ResponseEntity.ok(updatedUser);
    }
}
