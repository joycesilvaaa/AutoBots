package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteController;
import com.autobots.automanager.entidades.Cliente;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Cliente> {

    @Override
    public void adicionarLink(List<Cliente> lista) {
        for (Cliente cliente : lista) {
            adicionarLink(cliente);
        }
    }

    @Override
    public void adicionarLink(Cliente cliente) {
        long id = cliente.getId();

        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).obterCliente(id))
                .withSelfRel();
        cliente.add(linkProprio);

        Link linkClientes = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).obterClientes())
                .withRel("clientes");
        cliente.add(linkClientes);
    }
}
