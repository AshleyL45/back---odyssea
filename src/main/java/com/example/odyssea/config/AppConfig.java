package com.example.odyssea.config;

import com.example.odyssea.daos.CityDistanceDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // Adapté à MySQL
        dataSource.setUrl("jdbc:mysql://localhost:3306/odyssea_db"); // 🔹 Remplace par ta base de données
        dataSource.setUsername("root"); // 🔹 Remplace par ton utilisateur MySQL
        dataSource.setPassword("password"); // 🔹 Remplace par ton mot de passe MySQL
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public CityDistanceDao distanceBetweenCitiesDao(JdbcTemplate jdbcTemplate) {
        return new CityDistanceDao(jdbcTemplate);
    }
}
