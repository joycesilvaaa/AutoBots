package com.autobots.automanager.dto;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.sun.istack.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AtualizarClienteDto(
        @NotNull Long id,
        String nome,
        String nomeSocial,
        LocalDate dataNascimento,
        List<Documento> documentos,
        Endereco endereco,
        List<Telefone> telefones
) {
}
