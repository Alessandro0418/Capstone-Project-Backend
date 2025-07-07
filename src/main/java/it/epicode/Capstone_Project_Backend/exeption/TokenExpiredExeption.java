package it.epicode.Capstone_Project_Backend.exeption;

public class TokenExpiredExeption extends RuntimeException {
    public TokenExpiredExeption(String message) {
        super(message);
    }
}
