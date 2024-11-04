package com.autobots.automanager.modelo;

import com.autobots.automanager.controles.DocumentoController;
import com.autobots.automanager.controles.EnderecoController;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class AdicionadorLinkEndereco implements AdicionadorLink<Endereco> {

    @Override
    public void adicionarLink(List<Endereco> lista) {
        for (Endereco endereco : lista) {
            adicionarLink(endereco);
        }
    }

    @Override
    public void adicionarLink(Endereco endereco) {
        long id = endereco.getId();

        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EnderecoController.class).obterEndereco(id))
                .withSelfRel();
        endereco.add(linkProprio);

        Link linkEnderecos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EnderecoController.class).obterEnderecos())
                .withRel("enderecos");
        endereco.add(linkEnderecos);
    }
}
