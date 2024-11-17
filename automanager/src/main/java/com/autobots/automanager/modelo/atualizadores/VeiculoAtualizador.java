package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.Veiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class VeiculoAtualizador {

    @Autowired
    private StringVerificadorNulo verificador;

    public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getModelo())) {
                veiculo.setModelo(atualizacao.getModelo());
            }
            if (!verificador.verificar(atualizacao.getPlaca())) {
                veiculo.setPlaca(atualizacao.getPlaca());
            }
            if(atualizacao.getTipo() != veiculo.getTipo()){
                veiculo.setTipo(atualizacao.getTipo());
            }
        }
    }
    public void atualizar(Set<Veiculo> veiculos, Set<Veiculo> atualizacoes) {
        for (Veiculo atualizacao : atualizacoes) {
            for (Veiculo veiculo : veiculos) {
                if (atualizacao.getId() != null) {
                    if (atualizacao.getId() == veiculo.getId()) {
                        atualizar(veiculo, atualizacao);
                    }
                }
            }
        }
    }
}
