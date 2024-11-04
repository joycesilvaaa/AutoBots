package com.autobots.automanager.modelo;

import com.autobots.automanager.controles.EnderecoController;
import com.autobots.automanager.controles.TelefoneController;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone> {

    @Override
    public void adicionarLink(List<Telefone> lista) {
        for (Telefone telefone : lista) {
            adicionarLink(telefone);
        }
    }

    @Override
    public void adicionarLink(Telefone telefone) {
        long id = telefone.getId();

        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneController.class).obterTelefone(id))
                .withSelfRel();
        telefone.add(linkProprio);

        Link linkTelefones = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TelefoneController.class).obterTelefones())
                .withRel("telefones");
        telefone.add(linkTelefones);
    }
}
