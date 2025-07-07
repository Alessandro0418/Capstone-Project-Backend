package it.epicode.Capstone_Project_Backend;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("application.properties")
public class AppConfing {

    @Bean
    /*
    Questo metodo serve per creare un oggetto Cloudinary, che quindi ci permetta di uploadare file.
    Va creata una mappa con all'interno le proprietà per accedere al servizio e poi questa mappa va passata al costruttore di Clodinary per istanziarlo.
    */
    public Cloudinary getCloudinary(@Value("${cloudinary.cloud_name}") String cloudName,
                                    @Value("${cloudinary.api_key}") String apiKey,
                                    @Value("${cloudinary.api_secret}") String apiSecret){
        Map<String, String> configCloudinary = new HashMap<>();

        configCloudinary.put("cloud_name", cloudName);
        configCloudinary.put("api_key", apiKey);
        configCloudinary.put("api_secret", apiSecret);

        return new Cloudinary(configCloudinary);
    }
}
