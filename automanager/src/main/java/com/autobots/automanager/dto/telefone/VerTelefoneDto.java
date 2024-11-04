package com.autobots.automanager.dto.telefone;

import org.springframework.hateoas.Link;

import java.util.List;

public record VerTelefoneDto(
        Long id,
        String tipo,
        String numero,
        List<Link> links
) {
}
