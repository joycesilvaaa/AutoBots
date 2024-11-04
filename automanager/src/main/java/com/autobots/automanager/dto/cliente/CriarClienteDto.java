package com.autobots.automanager.dto.cliente;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record CriarClienteDto(
        @NotBlank String nome,
        @NotBlank String nomeSocial,
        @NotNull LocalDate dataNascimento,
        @NotNull List<Documento> documentos,
        @NotNull Endereco endereco,
        @NotNull List<Telefone> telefones
) {
}
