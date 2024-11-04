package com.autobots.automanager.dto.documento;

import javax.validation.constraints.NotBlank;

public record CriarDocumentoDto(
        @NotBlank String tipo,
        @NotBlank String numero
) {
}
