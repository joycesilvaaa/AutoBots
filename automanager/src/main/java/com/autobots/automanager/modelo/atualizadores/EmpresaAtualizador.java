package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmpresaAtualizador {

    @Autowired
    private StringVerificadorNulo verificador;

    @Autowired
    private TelefoneAtualizador telefoneAtualizador;

    @Autowired
    private EnderecoAtualizador enderecoAtualizador;

    private void atualizarInfo(Empresa empresa, Empresa atualizacao) {
        if (!verificador.verificar(atualizacao.getNomeFantasia())) {
            empresa.setNomeFantasia(atualizacao.getNomeFantasia());
        }
        if (!verificador.verificar(atualizacao.getRazaoSocial())) {
            empresa.setRazaoSocial(atualizacao.getRazaoSocial());
        }
        if (atualizacao.getEndereco() != null && !atualizacao.getEndereco().equals(empresa.getEndereco())) {
            enderecoAtualizador.atualizar(empresa.getEndereco(), atualizacao.getEndereco());
        }
        if (atualizacao.getTelefones() != null && !atualizacao.getTelefones().equals(empresa.getTelefones())) {
            telefoneAtualizador.atualizar(empresa.getTelefones(), atualizacao.getTelefones());
        }
    }

    public void atualizar(Empresa empresa, Empresa atualizacao) {
        atualizarInfo(empresa, atualizacao);
    }
}

