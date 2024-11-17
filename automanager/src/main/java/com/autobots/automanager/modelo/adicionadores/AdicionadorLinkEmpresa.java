package com.autobots.automanager.modelo.adicionadores;

import com.autobots.automanager.controle.ControleEmpresa;
import com.autobots.automanager.entitades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa> {

    @Autowired
    private AdicionadorLinkTelefone adicionadorLinkTelefone;

    @Autowired
    private AdicionadorLinkServico adicionadorLinkServico;

    @Autowired
    private AdicionadorLinkVenda adicionadorLinkVenda;

    @Override
    public void adicionarLink(List<Empresa> lista) {
        for (Empresa empresa : lista) {
            long id = empresa.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleEmpresa.class)
                            .listaEmpresa(id))
                    .withSelfRel();
            empresa.add(linkProprio);
            List<Telefone> telefones = empresa.getTelefones().stream().collect(Collectors.toList());
            adicionadorLinkTelefone.adicionarLink(telefones);
            List<Servico> servicos = empresa.getServicos().stream().collect(Collectors.toList());
            adicionadorLinkServico.adicionarLink(servicos);
        }
    }

    @Override
    public void adicionarLink(Empresa empresa) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleEmpresa.class)
                        .listagemEmpresas())
                .withRel("empresas");
        empresa.add(linkProprio);
        List<Telefone> telefones = empresa.getTelefones().stream().collect(Collectors.toList());
        adicionadorLinkTelefone.adicionarLink(telefones);
        List<Servico> servicos = empresa.getServicos().stream().collect(Collectors.toList());
        adicionadorLinkServico.adicionarLink(servicos);
    }
}
