package it.epicode.Capstone_Project_Backend.service;

import it.epicode.Capstone_Project_Backend.repository.UserRepository;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.enumeration.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public User registraUser(User utente, Set<Role> ruoliTipi) {
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utente.setRuoli(ruoliTipi);
        return utenteRepository.save(utente);
    }

    public Optional<User> findByEmail(String email) {
        return utenteRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }
}
