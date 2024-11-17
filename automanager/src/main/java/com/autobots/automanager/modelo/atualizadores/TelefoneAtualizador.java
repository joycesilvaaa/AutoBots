package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.Telefone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class TelefoneAtualizador {
    @Autowired
        private StringVerificadorNulo verificador = new StringVerificadorNulo();

        public void atualizar(Telefone telefone, Telefone atualizacao) {
            if (atualizacao != null) {
                if (!verificador.verificar(atualizacao.getDdd())) {
                    telefone.setDdd(atualizacao.getDdd());
                }
                if (!verificador.verificar(atualizacao.getNumero())) {
                    telefone.setNumero(atualizacao.getNumero());
                }
            }
        }

        public void atualizar(Set<Telefone> telefones, Set<Telefone> atualizacoes) {
            for (Telefone atualizacao : atualizacoes) {
                for (Telefone telefone : telefones) {
                    if (atualizacao.getId() != null) {
                        if (atualizacao.getId() == telefone.getId()) {
                            atualizar(telefone, atualizacao);
                        }
                    }
                }
            }
        }
    }
