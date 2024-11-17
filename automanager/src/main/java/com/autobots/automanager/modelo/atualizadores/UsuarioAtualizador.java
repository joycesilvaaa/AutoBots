package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.entitades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAtualizador {

    @Autowired
    private StringVerificadorNulo verificador;

    @Autowired
    private TelefoneAtualizador telefoneAtualizador;

    @Autowired
    private EnderecoAtualizador enderecoAtualizador;

    @Autowired
    private DocumentoAtualizador documentoAtualizador;

    @Autowired
    private EmailAtualizador emailAtualizador;

    private void atualizarUser(Usuario usuario, Usuario atualizacao) {
        if (!verificador.verificar(atualizacao.getNome())) {
            usuario.setNome(atualizacao.getNome());
        }
        if (!verificador.verificar(atualizacao.getNomeSocial())) {
            usuario.setNomeSocial(atualizacao.getNomeSocial());
        }
        if (atualizacao.getPerfis() != null) {
            usuario.setPerfis(atualizacao.getPerfis());
        }
        if(atualizacao.getEndereco() != null){
            enderecoAtualizador.atualizar(usuario.getEndereco(), atualizacao.getEndereco());
        }
        if(atualizacao.getTelefones() != null){
            telefoneAtualizador.atualizar(usuario.getTelefones(), atualizacao.getTelefones());
        }
        if(atualizacao.getDocumentos() != null){
            documentoAtualizador.atualizar(usuario.getDocumentos(), atualizacao.getDocumentos());
        }
        if(atualizacao.getEmails() != null){
            emailAtualizador.atualizar(usuario.getEmails(), atualizacao.getEmails());
        }
    }

    public void atualizar(Usuario usuario, Usuario atualizacao){
        atualizarUser(usuario, atualizacao);
    }
}
