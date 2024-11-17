package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CredencialUsuarioSenhaAtualizador {

    @Autowired
    private StringVerificadorNulo verificador;

    public void atualizar(CredencialUsuarioSenha credencial, CredencialUsuarioSenha atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getNomeUsuario())) {
                credencial.setNomeUsuario(atualizacao.getNomeUsuario());
            }
            if (!verificador.verificar(atualizacao.getSenha())){
                credencial.setSenha(atualizacao.getSenha());
            }
        }
    }

    public void atualizar(Set<CredencialUsuarioSenha> credenciais, List<CredencialUsuarioSenha> atualizacoes) {
        for (CredencialUsuarioSenha atualizacao : atualizacoes) {
            for (CredencialUsuarioSenha credencial : credenciais) {
                if (atualizacao.getId() != null) {
                    if (atualizacao.getId() == credencial.getId()) {
                        atualizar(credencial, atualizacao);
                    }
                }
            }
        }
    }
}
