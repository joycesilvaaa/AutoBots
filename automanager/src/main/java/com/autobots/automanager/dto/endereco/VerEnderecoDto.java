package com.autobots.automanager.dto.endereco;

import org.springframework.hateoas.Link;

import java.util.List;

public record VerEnderecoDto(
        Long id,
        String estado,
        String cidade,
        String bairro,
        String rua,
        String numero,
        String codigoPostal,
        String informacoesAdicionais,
        List<Link> links
) {
}
