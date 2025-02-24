package com.example.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Execute the user creation and grants
            jdbcTemplate.execute("ALTER SESSION SET CONTAINER=XEPDB1");
            jdbcTemplate.execute("CREATE USER db_itsupptickets IDENTIFIED BY itsupportpass");
            jdbcTemplate.execute("GRANT CONNECT, RESOURCE, DBA TO db_itsupptickets");
            jdbcTemplate.execute("ALTER USER db_itsupptickets QUOTA UNLIMITED ON USERS");
        } catch (Exception e) {
            // If user already exists, just log it
            System.out.println("User initialization error (might already exist): " + e.getMessage());
        }
    }
}
