package com.autobots.automanager.dto;

import java.time.LocalDate;

public record CriarClienteDto(
        String nome,
        String nomeSocial,
        LocalDate dataNascimento
) {
}
