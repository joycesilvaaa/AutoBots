package com.autobots.automanager.controles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.autobots.automanager.dto.AtualizarClienteDto;
import com.autobots.automanager.dto.CriarClienteDto;
import com.autobots.automanager.dto.VerClienteDto;
import com.autobots.automanager.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/cliente")
public class ClienteControle {

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/cliente/{id}")
	public ResponseEntity<?> obterCliente(@PathVariable long id) {
		try {
			VerClienteDto cliente = clienteService.verCliente(id);
			return ResponseEntity.status(HttpStatus.OK).body(cliente);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Cliente não encontrado: " + e.getMessage());
		}catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Dados inválidos ao ver cliente: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro inesperado: " + e.getMessage());
		}
	}

	@GetMapping("/clientes")
	public ResponseEntity<?> obterClientes() {
		try {
			List<VerClienteDto> clienteDtos = clienteService.listaClientes();
			return ResponseEntity.status(HttpStatus.OK).body(clienteDtos);
		}catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Dados inválidos ao ver cliente: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro inesperado: " + e.getMessage());
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarCliente(@RequestBody CriarClienteDto dadosNovoCliente) {
		try {
			CriarClienteDto clienteDto = new CriarClienteDto(
					dadosNovoCliente.nome(),
					dadosNovoCliente.nomeSocial(),
					dadosNovoCliente.dataNascimento()
			);

			Cliente clienteCadastrado = clienteService.cadastrarCliente(clienteDto);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(clienteCadastrado);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Dados inválidos ao cadastrar cliente: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro inesperado: " + e.getMessage());
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarCliente(@RequestBody AtualizarClienteDto dadosAtualizados) {
		try{
			AtualizarClienteDto atualizarClienteDto = new AtualizarClienteDto(
					dadosAtualizados.id(),
					dadosAtualizados.nome(),
					dadosAtualizados.nomeSocial(),
					dadosAtualizados.dataNascimento(),
					dadosAtualizados.documentos(),
					dadosAtualizados.endereco(),
					dadosAtualizados.telefones()
			);
			Cliente clienteAtualizado = clienteService.atualizarCliente(dadosAtualizados); // Retorna o cliente atualizado
			return ResponseEntity.status(HttpStatus.OK)
					.body(clienteAtualizado);
		}catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Cliente não encontrado: " + e.getMessage());
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Dados inválidos ao editar cliente: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro inesperado: " + e.getMessage());
		}
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?>excluirCliente(@PathVariable long id) {
		try{
			boolean clienteExcluido = clienteService.excluirCliente(id);
			if (clienteExcluido) {
				return ResponseEntity.status(HttpStatus.OK)
						.body("Cliente excluído com sucesso.");
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Cliente não encontrado");
		}catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Cliente não encontrado: " + e.getMessage());
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Dados inválidos ao editar cliente: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro inesperado: " + e.getMessage());
		}
	}

}
