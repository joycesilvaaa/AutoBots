package com.autobots.automanager.modelo.adicionadores;

import com.autobots.automanager.controle.ControleVeiculo;
import com.autobots.automanager.entitades.Veiculo;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo> {

    @Override
    public void adicionarLink(List<Veiculo> lista) {
        for (Veiculo veiculo : lista) {
            long id = veiculo.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleVeiculo.class)
                            .listaVeiculo(id))
                    .withSelfRel();
            veiculo.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(Veiculo objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleVeiculo.class)
                        .listagemVeiculo())
                .withRel("veiculos");
        objeto.add(linkProprio);
    }
}
