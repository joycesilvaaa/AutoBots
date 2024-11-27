package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServicoAutenticacao {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(CredencialUsuarioSenha credencial) {
        Usuario usuario = new Usuario();
        usuario.setNome(credencial.getNomeUsuario());
        credencial.setSenha(passwordEncoder.encode(credencial.getSenha()));
        return repositorioUsuario.save(usuario);
    }
}
