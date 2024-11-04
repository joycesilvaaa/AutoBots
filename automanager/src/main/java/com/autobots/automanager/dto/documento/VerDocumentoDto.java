package com.autobots.automanager.dto.documento;

import org.springframework.hateoas.Link;

import java.util.List;

public record VerDocumentoDto(
        Long id,
        String tipo,
        String numero,
        List<Link> links
) {
}
