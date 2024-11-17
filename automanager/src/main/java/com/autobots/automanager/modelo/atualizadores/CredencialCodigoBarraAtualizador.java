package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.CredencialCodigoBarra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CredencialCodigoBarraAtualizador {

    @Autowired
    private StringVerificadorNulo verificador;

    public void atualizar(CredencialCodigoBarra credencial, CredencialCodigoBarra atualizacao) {
        if(atualizacao.getUltimoAcesso() != null){
            credencial.setUltimoAcesso(atualizacao.getUltimoAcesso());
        }
        if (atualizacao.getCodigo() != credencial.getCodigo()) {
            credencial.setCodigo(atualizacao.getCodigo());
        }
    }

    public void atualizar(Set<CredencialCodigoBarra> credenciais, List<CredencialCodigoBarra> atualizacoes) {
        for (CredencialCodigoBarra atualizacao : atualizacoes) {
            for (CredencialCodigoBarra credencial : credenciais) {
                if (atualizacao.getId() != null) {
                    if (atualizacao.getId() == credencial.getId()) {
                        atualizar(credencial, atualizacao);
                    }
                }
            }
        }
    }
}
