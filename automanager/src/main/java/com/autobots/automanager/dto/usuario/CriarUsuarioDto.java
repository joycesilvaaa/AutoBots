package com.autobots.automanager.dto.usuario;

import com.autobots.automanager.entitades.*;
import com.autobots.automanager.enumeracoes.PerfilUsuario;

import java.util.Set;

public record CriarUsuarioDto(
        String nome,
        String nomeSocial,
        Set<PerfilUsuario> perfils,
        Set<Telefone> telefones,
        Endereco endereco,
        Set<Documento> documentos,
        Set<Email> emails,
        Set<CredencialUsuarioSenha> credenciais,
        Set<Mercadoria> mercadorias,
        Set<Venda> vendas,
        Set<Veiculo> veiculos
) {
}
