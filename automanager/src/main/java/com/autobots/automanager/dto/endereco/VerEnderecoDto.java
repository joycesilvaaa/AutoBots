package com.autobots.automanager.dto.endereco;

public record VerEnderecoDto(
        Long id,
        String estado,
        String cidade,
        String bairro,
        String rua,
        String numero,
        String codigoPostal,
        String informacoesAdicionais
) {
}
