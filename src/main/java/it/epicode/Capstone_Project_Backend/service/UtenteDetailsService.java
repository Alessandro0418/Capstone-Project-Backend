package it.epicode.Capstone_Project_Backend.service;

import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtenteDetailsService implements UserDetailsService {

    private final UserRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User non trovato: " + username));

        return new org.springframework.security.core.userdetails.User(
                utente.getUsername(),
                utente.getPassword(),
                utente.getRuoli().stream()
                        .map(ruolo -> new SimpleGrantedAuthority("ROLE_" + ruolo.name()))
                        .collect(Collectors.toList())
        );
    }
}
