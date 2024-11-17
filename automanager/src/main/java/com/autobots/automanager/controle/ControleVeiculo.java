package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.servicos.ServicoVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculo")
public class ControleVeiculo {

    @Autowired
    private ServicoVeiculo servicoVeiculo;

    @PostMapping("/criar")
    public ResponseEntity<?> cadastrarVeiculo(@RequestBody Veiculo veiculoDados){
        try{
            Veiculo veiculo = servicoVeiculo.cadastrarVeiculo(veiculoDados);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Novo Veiculo cadastrado");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar veiculo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<?> listaVeiculo(){
        try{
            List<Veiculo> veiculos = servicoVeiculo.listagemVeiculos();
            if (veiculos.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem veiculos cadastrados");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(veiculos);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao fazer listagem de veiculos: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listaVeiculo(@PathVariable Long id){
        try{
            Veiculo veiculo = servicoVeiculo.listaVeiculo(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(veiculo);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao ver veiculo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaVeiculo(@PathVariable Long id){
        try{
            boolean veiculo = servicoVeiculo.deletarVeiculo(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Veiculo Deletado");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao deletar veiculo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
