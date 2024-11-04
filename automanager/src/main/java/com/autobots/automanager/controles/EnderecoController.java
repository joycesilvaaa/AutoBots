package com.autobots.automanager.controles;

import com.autobots.automanager.dto.endereco.CriarEnderecoDto;
import com.autobots.automanager.dto.endereco.VerEnderecoDto;

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping("/cadastrar/{clienteId}")
    public ResponseEntity<?> cadastrarEndereco(@PathVariable long clienteId, @RequestBody @Valid CriarEnderecoDto enderecoDto) {
        try {
            Endereco enderecoCadastrado = enderecoService.cadastrarEndereco(clienteId, enderecoDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Endereço Adicionado.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados ao cadastrar endereço: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterEndereco(@PathVariable Long id) {
        try {
            VerEnderecoDto enderecoDto = enderecoService.verEndereco(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(enderecoDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: " +  e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados ao cadastrar endereço: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/lista/todos")
    public ResponseEntity<?> obterEnderecos() {
        try {
            List<VerEnderecoDto> enderecos = enderecoService.listaEnderecos();
            if (enderecos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhum endereço encontrado.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(enderecos);
        }  catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados ao cadastrar endereço: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{clienteId}/{documentoId}")
    public ResponseEntity<?> excluirEndereco(@PathVariable Long clienteId, @PathVariable Long documentoId) {
        try {
            boolean enderecoExcluido = enderecoService.excluirEndereco(clienteId, documentoId);
            if (enderecoExcluido) {
                return ResponseEntity.status(HttpStatus.OK).body("Endereço excluído com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado com ID: " + documentoId);
            }
        }  catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados ao excluir Endereço: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
