package com.autobots.automanager.controles;

import com.autobots.automanager.dto.telefone.CriarTelefoneDto;
import com.autobots.automanager.dto.telefone.VerTelefoneDto;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import com.autobots.automanager.service.TelefoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/telefone")
public class TelefoneController {

    @Autowired
    private TelefoneService telefoneService;

    @GetMapping("/{id}")
    public ResponseEntity<?> obterTelefone(@PathVariable long id){
        try {
            VerTelefoneDto telefone = telefoneService.verTelefone(id);
            return  ResponseEntity.status(HttpStatus.OK)
                    .body(telefone);
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: " +  e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/lista/todos")
    public ResponseEntity<?> obterTelefones(){
        try{
            List<VerTelefoneDto> telefones = telefoneService.listarTelefones();
            if (telefones.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhum telefone encontrado.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(telefones);
        }  catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: "+ e.getMessage());
        }
    }

    @PostMapping("/cadastro/{id}")
    public ResponseEntity<?> cadastrarTelefone(@PathVariable long id, @RequestBody CriarTelefoneDto criarTelefoneDto){
        try {
            Telefone telefone = telefoneService.cadastrarTelefone(id, criarTelefoneDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Telefone Adicionado");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{clienteId}/{telefoneId}")
    public ResponseEntity<?> deletarTelefone(@PathVariable long clienteId, @PathVariable long telefoneId){
        try {
            boolean telefoneExcluido = telefoneService.excluirTelefone(clienteId, telefoneId);
            if(telefoneExcluido){
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Telefone Excluido");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao deletar telefone.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
