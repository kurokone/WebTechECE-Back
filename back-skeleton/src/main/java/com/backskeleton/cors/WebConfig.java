package com.backskeleton.cors;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Configurez le chemin de votre API
            .allowedOrigins("http://localhost:4200") // Autorisez les requêtes depuis ce domaine
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Autorisez les méthodes HTTP
            .allowCredentials(true); // Autorisez les cookies, si nécessaire
    }
}
