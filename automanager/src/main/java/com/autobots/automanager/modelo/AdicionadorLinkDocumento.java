package com.autobots.automanager.modelo;

import com.autobots.automanager.controles.DocumentoController;
import com.autobots.automanager.entidades.Documento;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<Documento> {

    @Override
    public void adicionarLink(List<Documento> lista) {
        for (Documento documento : lista) {
            adicionarLink(documento);
        }
    }

    @Override
    public void adicionarLink(Documento documento) {
        long id = documento.getId();

        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoController.class).obterDocumento(id))
                .withSelfRel();
        documento.add(linkProprio);

        Link linkDocumentos = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DocumentoController.class).obterDocumentos())
                .withRel("documentos");
        documento.add(linkDocumentos);
    }
}
