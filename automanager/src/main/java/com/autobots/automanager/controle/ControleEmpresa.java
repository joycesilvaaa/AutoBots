package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.servicos.ServicoEmpresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresa")
public class ControleEmpresa {

    @Autowired
    private ServicoEmpresa servicoEmpresa;

    @PostMapping("/criar")
    public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresaDados){
        try{
            Empresa empresa = servicoEmpresa.cadastraEmpresa(empresaDados);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Empresa Cadastrada");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar empresa: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarEmpresa(@PathVariable Long id, @RequestBody Empresa empresaUpdate){
        try{
            Empresa empresa = servicoEmpresa.editarEmpresa(id, empresaUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(empresa);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao listar empresa: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/todas")
    public ResponseEntity<?> listagemEmpresas(){
        try{
            List<Empresa> empresas = servicoEmpresa.listagemEmpresa();
            if (empresas.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem empresas cadastradas");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(empresas);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao fazer listagem de empresas: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listaEmpresa(@PathVariable Long id){
        try{
            Empresa empresa = servicoEmpresa.listaEmpresa(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(empresa);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao listar empresa: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaEmpresa(@PathVariable Long id){
        try{
            boolean empresaDeletada = servicoEmpresa.deletarEmpresa(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Empresa Deletada");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao deletar empresa: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
