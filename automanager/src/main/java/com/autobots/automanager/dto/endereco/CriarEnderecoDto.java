package com.autobots.automanager.dto.endereco;

import javax.validation.constraints.NotBlank;

public record CriarEnderecoDto(
        @NotBlank String estado,
        @NotBlank String cidade,
        @NotBlank String bairro,
        @NotBlank String rua,
        @NotBlank String numero,
        @NotBlank String codigoPostal,
        @NotBlank String informacoesAdicionais
) {
}
