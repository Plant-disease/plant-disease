package org.example.plantdisease.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

//OBJECTLARNI KIM QO'SHGANI VA KIM UPDATE QILGANINI ANIQLASH UCHUN XIZMAT QILADI
@Configuration
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
