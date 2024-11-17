package com.autobots.automanager.repositorios;

import com.autobots.automanager.entitades.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioCredencial extends JpaRepository<Credencial, Long> {
}
