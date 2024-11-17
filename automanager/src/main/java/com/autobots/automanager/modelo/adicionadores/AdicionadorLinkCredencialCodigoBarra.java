package com.autobots.automanager.modelo.adicionadores;

import com.autobots.automanager.controle.ControleCredencial;
import com.autobots.automanager.entitades.CredencialCodigoBarra;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkCredencialCodigoBarra implements AdicionadorLink<CredencialCodigoBarra> {

    @Override
    public void adicionarLink(List<CredencialCodigoBarra> lista) {
        for (CredencialCodigoBarra credencial : lista) {
            long id = credencial.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleCredencial.class)
                            .listaCredencialCodigo(id))
                    .withSelfRel();
            credencial.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(CredencialCodigoBarra objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleCredencial.class)
                        .listagemCredencialCodigo())
                .withRel("credenciais");
        objeto.add(linkProprio);
    }
}
