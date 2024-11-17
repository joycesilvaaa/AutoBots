package com.autobots.automanager.dto.Credencial;

import java.io.Serializable;

public record CredencialUsuarioSenhaDto(String nomeUsuario, String senha) implements Serializable {
    private static final long serialVersionUID = 1L;
}
