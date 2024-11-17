package com.autobots.automanager.repositorios;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioCredencialUsuarioSenha extends JpaRepository<CredencialUsuarioSenha, Long> {
}
