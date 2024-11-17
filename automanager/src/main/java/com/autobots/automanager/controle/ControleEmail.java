package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.servicos.ServicoEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email")
public class ControleEmail {

    @Autowired
    private ServicoEmail servicoEmail;
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/criar/{id}")
    public ResponseEntity<?> cadastrarEmail(@PathVariable Long id, @RequestBody Email emailDados){
        try{
            Email email = servicoEmail.cadastrarEmail(id, emailDados);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("E-mail criado com sucesso");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar e-mail: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editarEmail(@PathVariable Long id, @RequestBody Email emailUpdate){
        try{
            Email email = servicoEmail.editarEmail(id, emailUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(email);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao editar e-mail: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> listarEmail(@PathVariable Long id){
        try{
            Email email = servicoEmail.listarEmail(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(email);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao listar e-mail: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/todos")
    public ResponseEntity<?> listagemEmails(){
        try{
            List<Email> emails = servicoEmail.listagemEmails();
            if(emails.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem e-mail cadastrados");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(emails);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao listagem de e-mail: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @DeleteMapping("/{userId}/{emailId}")
    public ResponseEntity<?> deleteEmail(@PathVariable Long userId, @PathVariable Long emailId){
        try{
            servicoEmail.deleteEmail(userId, emailId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("E-mail deletado");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao deletar e-mail: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
