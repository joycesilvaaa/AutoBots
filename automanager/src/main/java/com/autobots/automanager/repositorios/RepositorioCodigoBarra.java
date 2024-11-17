package com.autobots.automanager.repositorios;

import com.autobots.automanager.entitades.CredencialCodigoBarra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioCodigoBarra extends JpaRepository<CredencialCodigoBarra, Long> {
}
