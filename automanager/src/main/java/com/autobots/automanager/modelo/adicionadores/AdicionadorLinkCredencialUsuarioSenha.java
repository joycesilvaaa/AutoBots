package com.autobots.automanager.modelo.adicionadores;

import com.autobots.automanager.controle.ControleCredencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkCredencialUsuarioSenha implements AdicionadorLink<CredencialUsuarioSenha> {

    @Override
    public void adicionarLink(List<CredencialUsuarioSenha> lista) {
        for (CredencialUsuarioSenha credencial : lista) {
            long id = credencial.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleCredencial.class)
                            .listaCredencialUsuario(id))
                    .withSelfRel();
            credencial.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(CredencialUsuarioSenha objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleCredencial.class)
                        .listaCredencialUsuario())
                .withRel("credenciais");
        objeto.add(linkProprio);
    }
}
