package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.servicos.ServicoMercadoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mercadoria")
public class ControleMercadoria {

    @Autowired
    private ServicoMercadoria servicoMercadoria;

    @PostMapping("/criar")
    public ResponseEntity<?> cadastrarMercadoria(@RequestBody Mercadoria mercadoriaNova){
        try{
            Mercadoria mercadoria = servicoMercadoria.cadastrarMercadoria(mercadoriaNova);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Mercadoria Cadastrada");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar mercadoria: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarMercadoria(@PathVariable Long id, @RequestBody Mercadoria mercadoriaUpdate){
        try{
            Mercadoria mercadoria = servicoMercadoria.editarMercadoria(id, mercadoriaUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mercadoria);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao listar mercadoria: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/todas")
    public ResponseEntity<?> listagemMercadorias(){
        try{
            List<Mercadoria> mercadorias = servicoMercadoria.listagemMercadoria();
            if (mercadorias.isEmpty()){
                return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem mercadorias cadastradas.");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mercadorias);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao listar mercadoria: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarMercadoria(@PathVariable Long id){
        try{
            Mercadoria mercadoria = servicoMercadoria.listarMercadoria(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(mercadoria);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao listar mercadoria: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMercadoria(@PathVariable Long id){
        try{
            servicoMercadoria.deletarMercadoria(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Mercadoria deletada");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao listar mercadoria: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
