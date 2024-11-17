package com.autobots.automanager.modelo.adicionadores;

import com.autobots.automanager.controle.ControleUsuario;
import com.autobots.automanager.entitades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario> {

    @Autowired
    private AdicionadorLinkTelefone adicionadorLinkTelefone;

    @Autowired
    private AdicionadorLinkDocumento adicionadorLinkDocumento;

    @Autowired
    private AdicionadorLinkEmail adicionadorLinkEmail;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorLinkMercadoria;

    @Override
    public void adicionarLink(List<Usuario> lista) {
        for (Usuario usuario : lista) {
            if (!usuario.hasLink("self")) {
                long id = usuario.getId();
                Link linkProprio = WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(ControleUsuario.class)
                                .listaUsuario(id))
                        .withSelfRel();
                usuario.add(linkProprio);
                List<Telefone> telefones = usuario.getTelefones().stream().collect(Collectors.toList());
                adicionadorLinkTelefone.adicionarLink(telefones);
                List<Documento> documentos = usuario.getDocumentos().stream().collect(Collectors.toList());
                adicionadorLinkDocumento.adicionarLink(documentos);
                List<Email> emails = usuario.getEmails().stream().collect(Collectors.toList());
                adicionadorLinkEmail.adicionarLink(emails);
                List<Mercadoria> mercadorias = usuario.getMercadorias().stream().collect(Collectors.toList());
                adicionadorLinkMercadoria.adicionarLink(mercadorias);
            }
        }
    }

    @Override
    public void adicionarLink(Usuario usuario) {
        if(!usuario.hasLink("usuarios")) {
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .listagemUsuarios())
                    .withRel("usuarios");
            usuario.add(linkProprio);
            List<Telefone> telefones = usuario.getTelefones().stream().collect(Collectors.toList());
            adicionadorLinkTelefone.adicionarLink(telefones);
            List<Documento> documentos = usuario.getDocumentos().stream().collect(Collectors.toList());
            adicionadorLinkDocumento.adicionarLink(documentos);
            List<Email> emails = usuario.getEmails().stream().collect(Collectors.toList());
            adicionadorLinkEmail.adicionarLink(emails);
            List<Mercadoria> mercadorias = usuario.getMercadorias().stream().collect(Collectors.toList());
            adicionadorLinkMercadoria.adicionarLink(mercadorias);
        }
    }

}
