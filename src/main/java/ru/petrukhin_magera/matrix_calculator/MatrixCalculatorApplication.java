package ru.petrukhin_magera.matrix_calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Главный класс приложения Matrix Calculator.
 * <p>
 * Запускает Spring Boot приложение и настраивает CORS политику
 * для взаимодействия с фронтендом.
 * </p>
 *
 * @author Petrukhin Magera Team
 * @version 1.0
 */
@SpringBootApplication
public class MatrixCalculatorApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(MatrixCalculatorApplication.class, args);
    }

    /**
     * Настраивает CORS (Cross-Origin Resource Sharing) для всего приложения.
     * <p>
     * Разрешает запросы с локальных адресов http://localhost:8080, http://127.0.0.1:8080
     * и из локальных файлов (null). Поддерживаются методы GET, POST, PUT, DELETE, OPTIONS.
     * </p>
     *
     * @return конфигуратор CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080", "http://127.0.0.1:8080", "null")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}