package com.autobots.automanager.dto.endereco;

public record CriarEnderecoDto(
        String estado,
        String cidade,
        String bairro,
        String rua,
        String numero,
        String codigoPostal,
        String informacoesAdicionais
) {
}
