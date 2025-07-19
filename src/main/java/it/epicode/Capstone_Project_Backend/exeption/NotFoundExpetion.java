package it.epicode.Capstone_Project_Backend.exeption;

public class NotFoundExpetion extends RuntimeException {
    public NotFoundExpetion(String message) {
        super(message);
    }
}
