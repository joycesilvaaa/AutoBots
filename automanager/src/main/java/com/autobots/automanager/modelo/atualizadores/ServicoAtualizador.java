package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ServicoAtualizador {

    @Autowired
    private StringVerificadorNulo verificador;

    public void atualizar(Servico servico, Servico atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getNome())) {
                servico.setNome(atualizacao.getNome());
            }
            if(atualizacao.getValor() != servico.getValor()){
                servico.setValor(atualizacao.getValor());
            }
            if(!verificador.verificar(atualizacao.getDescricao())){
                servico.setDescricao(atualizacao.getDescricao());
            }
        }
    }

    public void atualizar(Set<Servico> servicos,Set<Servico> atualizacoes) {
        for (Servico atualizacao : atualizacoes) {
            for (Servico servico : servicos) {
                if (servico.getId() != null) {
                    if (atualizacao.getId() == servico.getId()) {
                        atualizar(servico, atualizacao);
                    }
                }
            }
        }
    }
}
