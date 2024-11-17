package com.autobots.automanager.modelo.adicionadores;

import com.autobots.automanager.controle.ControleServico;
import com.autobots.automanager.entitades.Servico;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico> {
    @Override
    public void adicionarLink(List<Servico> lista) {
        for (Servico servico : lista) {
            long id = servico.getId();
            if (!servico.hasLink("self")) {
                Link linkProprio = WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(ControleServico.class)
                                .listarServico(id))
                        .withSelfRel();
                servico.add(linkProprio);
            }
        }
    }

    @Override
    public void adicionarLink(Servico servico) {
        if (!servico.hasLink("servicos")) {
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleServico.class)
                            .listagemServicos())
                    .withRel("servicos");
            servico.add(linkProprio);
        }
    }
}
