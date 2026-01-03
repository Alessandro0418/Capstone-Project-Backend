package it.epicode.Capstone_Project_Backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.Capstone_Project_Backend.repository.UserRepository;
import it.epicode.Capstone_Project_Backend.model.User;
import it.epicode.Capstone_Project_Backend.enumeration.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final Cloudinary cloudinary;

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


    public User uploadAvatar(String username, MultipartFile file) {
        User user = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User non trovato: " + username));

        try {

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = uploadResult.get("url").toString();

            user.setAvatar(url);
            return utenteRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento dell'immagine", e);
        }
    }


    public User updateUser(String username, User updatedData) {
        User user = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User non trovato"));

        // Aggiorna solo i campi consentiti
        user.setName(updatedData.getName());
        user.setCognome(updatedData.getCognome());
        user.setAvatar(updatedData.getAvatar());

        return utenteRepository.save(user);
    }

    public void deleteUser(String username) {
        User user = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User non trovato"));

        utenteRepository.delete(user);
    }
}
