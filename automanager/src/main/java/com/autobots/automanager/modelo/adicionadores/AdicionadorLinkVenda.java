package com.autobots.automanager.modelo.adicionadores;

import com.autobots.automanager.controle.ControleVenda;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<Venda> {

    @Autowired
    private  AdicionadorLinkMercadoria adicionadorLinkMercadoria;

    @Autowired
    private AdicionadorLinkServico adicionadorLinkServico;

    @Autowired
    private AdicionadorLinkUsuario adicionadorLinkUsuario;

    @Override
    public void adicionarLink(List<Venda> lista) {
        for (Venda venda : lista) {
            if (!venda.hasLink("self")) {
                long id = venda.getId();
                Link linkProprio = WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(ControleVenda.class)
                                .listaVenda(id))
                        .withSelfRel();
                venda.add(linkProprio);
                List<Mercadoria> mercadorias = venda.getMercadorias().stream().collect(Collectors.toList());
                adicionadorLinkMercadoria.adicionarLink(mercadorias);
                List<Servico> servicos = venda.getServicos().stream().collect(Collectors.toList());
                adicionadorLinkServico.adicionarLink(servicos);
            }
        }
    }

    @Override
    public void adicionarLink(Venda venda) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleVenda.class)
                        .listagemVendas())
                .withRel("vendas");
        venda.add(linkProprio);
        adicionadorLinkUsuario.adicionarLink(venda.getFuncionario());
        adicionadorLinkUsuario.adicionarLink(venda.getCliente());
    }
}
