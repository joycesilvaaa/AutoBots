package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.Mercadoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class MercadoriaAtualizador {

    @Autowired
    private  StringVerificadorNulo verificador;

    public void atualizar(Mercadoria mercadoria, Mercadoria  atualizacao) {
        if (atualizacao != null) {
            if (atualizacao.getFabricao() != null) {
                mercadoria.setFabricao(atualizacao.getFabricao());
            }
            if(atualizacao.getValidade() != null){
                mercadoria.setValidade(atualizacao.getValidade());
            }
            if(atualizacao.getCadastro() != null){
                mercadoria.setCadastro(atualizacao.getCadastro());
            }
            if(!verificador.verificar(atualizacao.getNome())){
                mercadoria.setNome(atualizacao.getNome());
            }
            if(atualizacao.getQuantidade() != mercadoria.getQuantidade()){
                mercadoria.setQuantidade(atualizacao.getQuantidade());
            }
            if(atualizacao.getValor() != mercadoria.getValor()){
                mercadoria.setValor(atualizacao.getValor());
            }
        }
    }

    public void atualizar(Set<Mercadoria> mercadorias, Set<Mercadoria> atualizacoes) {
        for (Mercadoria atualizacao : atualizacoes) {
            for (Mercadoria mercadoria :mercadorias) {
                if (mercadoria.getId() != null) {
                    if (atualizacao.getId() == mercadoria.getId()) {
                        atualizar(mercadoria, atualizacao);
                    }
                }
            }
        }
    }
}
