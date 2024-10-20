package com.autobots.automanager.modelo;

import com.autobots.automanager.dto.AtualizarClienteDto;
import com.autobots.automanager.entidades.Cliente;

public class ClienteAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
	private DocumentoAtualizador documentoAtualizador = new DocumentoAtualizador();
	private TelefoneAtualizador telefoneAtualizador = new TelefoneAtualizador();

	private void atualizarDados(Cliente cliente, AtualizarClienteDto atualizacao) {
		if (!verificador.verificar(atualizacao.nome())) {
			cliente.setNome(atualizacao.nome());
		}
		if (!verificador.verificar(atualizacao.nomeSocial())) {
			cliente.setNomeSocial(atualizacao.nomeSocial());
		}
		if (!(atualizacao.dataNascimento() == null)) {
			cliente.setDataNascimento(atualizacao.dataNascimento());
		}
	}

	public void atualizar(Cliente cliente, AtualizarClienteDto atualizacao) {
		atualizarDados(cliente, atualizacao);
		enderecoAtualizador.atualizar(cliente.getEndereco(), atualizacao.endereco());
		documentoAtualizador.atualizar(cliente.getDocumentos(), atualizacao.documentos());
		telefoneAtualizador.atualizar(cliente.getTelefones(), atualizacao.telefones());
	}
}
