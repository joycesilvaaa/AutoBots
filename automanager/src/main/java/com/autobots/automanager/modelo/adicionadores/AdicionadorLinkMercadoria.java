package com.autobots.automanager.modelo.adicionadores;

import com.autobots.automanager.controle.ControleMercadoria;
import com.autobots.automanager.entitades.Mercadoria;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria> {

    @Override
    public void adicionarLink(List<Mercadoria> lista) {
        for (Mercadoria mercadoria : lista) {
            long id = mercadoria.getId();
            if (!mercadoria.hasLink("self")) {
                Link linkProprio = WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(ControleMercadoria.class)
                                .listarMercadoria(id))
                        .withSelfRel();
                mercadoria.add(linkProprio);
            }
        }
    }

    @Override
    public void adicionarLink(Mercadoria mercadoria) {
        if (!mercadoria.hasLink("mercadorias")) {
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleMercadoria.class)
                            .listagemMercadorias())
                    .withRel("mercadorias");
            mercadoria.add(linkProprio);
        }
    }
}
