package it.epicode.Capstone_Project_Backend.dto;

import it.epicode.Capstone_Project_Backend.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    @NotBlank(message = "Username obbligatorio")
    private String username;

    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Email non valida")
    private String email;

    @NotBlank(message = "Password obbligatoria")
    @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
    private String password;

    @NotBlank(message = "Nome obbligatorio")
    private String nome;

    @NotBlank(message = "Cognome obbligatorio")
    private String cognome;

    private String avatar;

    @NotEmpty(message = "È necessario assegnare almeno un ruolo")
    private Set<Role> ruoli;
}
