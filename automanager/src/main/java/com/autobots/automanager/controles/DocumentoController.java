package com.autobots.automanager.controles;

import com.autobots.automanager.dto.documento.CriarDocumentoDto;
import com.autobots.automanager.dto.documento.VerDocumentoDto;

import com.autobots.automanager.entidades.Documento;

import com.autobots.automanager.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@RestController
@RequestMapping("/documento")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping("/cadastrar/{id}")
    public ResponseEntity<?> cadastrarDocumento(@PathVariable long id, @RequestBody CriarDocumentoDto novoDocumento) {
        try {
            Documento documentoCadastrado = documentoService.cadastrarDocumento(id, new CriarDocumentoDto(novoDocumento.tipo(), novoDocumento.numero()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(documentoCadastrado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: Cliente não encontrado. " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados ao cadastrar documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterDocumento(@PathVariable Long id) {
        try {
            VerDocumentoDto documentoDto = documentoService.verDocumento(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(documentoDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Documento não encontrado: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados ao cadastrar documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/lista/todos")
    public ResponseEntity<?> obterDocumentos() {
        try {
            List<VerDocumentoDto> documentos = documentoService.listaDocumentos();
            if (documentos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Nenhum documento encontrado.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(documentos);
        }  catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados ao cadastrar documento: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter documentos: " + e.getMessage());
        }
    }



    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirDocumento(@PathVariable Long id) {
        try {
            boolean documentoExcluido = documentoService.excluirDocumento(id);
            if (documentoExcluido) {
                return ResponseEntity.status(HttpStatus.OK).body("Documento excluído com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado com ID: " + id);
            }
        }  catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro de integridade de dados ao cadastrar documento: " + e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir documento: " + e.getMessage());
        }
    }
}
