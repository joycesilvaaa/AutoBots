package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.servicos.ServicoDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documento")
public class ControleDocumento {

    @Autowired
    private ServicoDocumento servicoDocumento;
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @PostMapping("/criar/{id}")
    public ResponseEntity<?> cadastrarDocumento(@PathVariable Long id, @RequestBody Documento documentoDados){
        try{
            Documento documento = servicoDocumento.cadastrarDocumento(id, documentoDados);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Documento criado com sucesso");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editarDocumento(@PathVariable Long id, @RequestBody Documento documentoUpdate){
        try{
            Documento documento = servicoDocumento.editarDocumento(id, documentoUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Documento editado com sucesso");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao editado documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/todos")
    public ResponseEntity<?> listagemDocumentos(){
        try{
            List<Documento> documentos = servicoDocumento.listagemDocumentos();
            if (documentos.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem documentos cadastrados");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(documentos);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao ver documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<?> listarDocumento(@PathVariable Long id){
        try{
            Documento documento = servicoDocumento.listarDocumento(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(documento);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao ver documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @DeleteMapping("/{userId}/{documentoId}")
    public ResponseEntity<?> deletarDocumento(@PathVariable Long userId, @PathVariable Long documentoId){
        try{
            servicoDocumento.deleteDocumento(userId, documentoId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Documento deletado");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao ver documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
