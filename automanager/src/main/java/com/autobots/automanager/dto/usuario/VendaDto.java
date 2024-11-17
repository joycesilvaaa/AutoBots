package com.autobots.automanager.dto.usuario;

import java.util.List;

public record VendaDto(
        Long id,
        String identificacao,
        Long cliente,
        Long funcionario,
        List<Long> mercadorias,
        List<Long> servicos,
        Long veiculo
) {}
