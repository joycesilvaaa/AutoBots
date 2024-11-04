package com.autobots.automanager.dto.telefone;

import javax.validation.constraints.NotBlank;

public record CriarTelefoneDto(
        @NotBlank String ddd,
        @NotBlank String numero
) {
}
