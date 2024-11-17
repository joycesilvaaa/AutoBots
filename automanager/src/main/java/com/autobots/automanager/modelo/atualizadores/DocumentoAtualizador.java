package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.Documento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
public class DocumentoAtualizador {
    @Autowired
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Documento documento, Documento atualizacao) {
        if (atualizacao != null) {
            if (atualizacao.getTipo() != null) {
                documento.setTipo(atualizacao.getTipo());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                documento.setNumero(atualizacao.getNumero());
            }
        }
    }

    public void atualizar(Set<Documento> documentos, Set<Documento> atualizacoes) {
        for (Documento atualizacao : atualizacoes) {
            for (Documento documento : documentos) {
                if (atualizacao.getId() != null) {
                    if (atualizacao.getId() == documento.getId()) {
                        atualizar(documento, atualizacao);
                    }
                }
            }
        }
    }
}