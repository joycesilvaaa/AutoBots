package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.servicos.ServicoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servico")
public class ControleServico {

    @Autowired
    private ServicoServico servicoServico;

    @PostMapping("/criar")
    public ResponseEntity<?> cadastrarServico(@RequestBody Servico servicoNovo){
        try{
            Servico servico = servicoServico.cadastrarServico(servicoNovo);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Serviço Cadastrado");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar serviço: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarServico(@PathVariable Long id, @RequestBody Servico servicoUpdate){
        try{
            Servico servico = servicoServico.editarServico(id, servicoUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Serviço Atualizado");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao fazer listar de serviço: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }


    @GetMapping("/todos")
    public ResponseEntity<?> listagemServicos(){
        try{
            List<Servico> servicos = servicoServico.listagemServicos();
            if (servicos.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem servicos cadastrados.");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(servicos);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao fazer listagem de serviços: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> listarServico(@PathVariable Long id){
        try{
            Servico servico = servicoServico.listaServico(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(servico);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao fazer listar de serviço: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarServico(@PathVariable Long id){
        try{
            servicoServico.deleteServico(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Serviço Deletado");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao fazer listar de serviço: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
