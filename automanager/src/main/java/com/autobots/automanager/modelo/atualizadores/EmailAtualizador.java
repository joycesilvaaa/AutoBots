package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Telefone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailAtualizador {

    @Autowired
    private StringVerificadorNulo verificador;

    public void atualizar(Email email, Email atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getEndereco())) {
                email.setEndereco(atualizacao.getEndereco());
            }
        }
    }

    public void atualizar(List<Email> emails, List<Email> atualizacoes) {
        for (Email atualizacao : atualizacoes) {
            for (Email email : emails) {
                if (email.getId() != null) {
                    if (atualizacao.getId() == email.getId()) {
                        atualizar(email, atualizacao);
                    }
                }
            }
        }
    }
}
