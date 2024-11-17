package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnderecoAtualizador {
    @Autowired
    private StringVerificadorNulo verificador;

    public void atualizar( Endereco endereco, Endereco atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getEstado())) {
                endereco.setEstado(atualizacao.getEstado());
            }
            if (!verificador.verificar(atualizacao.getCidade())) {
                endereco.setCidade(atualizacao.getCidade());
            }
            if (!verificador.verificar(atualizacao.getBairro())) {
                endereco.setBairro(atualizacao.getBairro());
            }
            if (!verificador.verificar(atualizacao.getRua())) {
                endereco.setRua(atualizacao.getRua());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                endereco.setNumero(atualizacao.getNumero());
            }
            if (!verificador.verificar(atualizacao.getInformacoesAdicionais())) {
                endereco.setInformacoesAdicionais(atualizacao.getInformacoesAdicionais());
            }
        }
    }
}