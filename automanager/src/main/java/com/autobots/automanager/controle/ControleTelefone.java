package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.servicos.ServicoTelefone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/telefone")
public class ControleTelefone {


    @Autowired
    private ServicoTelefone servicoTelefone;
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/usuario/criar/{id}")
    public ResponseEntity<?> cadastroTelefoneUsuario(@RequestBody Telefone telefoneDados, @PathVariable Long id){
        try{
            Telefone telefone = servicoTelefone.cadastrarTelefoneUsuario(id, telefoneDados);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Telefone Cadastrada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar Telefone: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PostMapping("/empresa/criar/{id}")
    public ResponseEntity<?> cadastroTelefoneEmpresa(@RequestBody Telefone telefoneDados, @PathVariable Long id){
        try{
            Telefone telefone = servicoTelefone.cadastrarTelefoneEmpresa(id, telefoneDados);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Telefone Cadastrada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar Telefone: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editaTelefone(@PathVariable Long id,@RequestBody Telefone telefoneUpdate){
        try{
            Telefone telefone = servicoTelefone.editarTelefone(id,telefoneUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Telefone Atualizado.");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar telefone: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/todos")
    public ResponseEntity<?> listagemTelefones(){
        try{
            List<Telefone> telefones = servicoTelefone.listagemTelefones();
            if(telefones.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem telefones cadastrados");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(telefones);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar telefone: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaTelefone(@PathVariable Long id){
        try{
            Telefone telefone = servicoTelefone.listaTelefone(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(telefone);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar telefone: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaTelefone(@PathVariable Long id){
        try{
            boolean telefone = servicoTelefone.deletaTelefone(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Telefone deletado");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao deletar telefone: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

}
