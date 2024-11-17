package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.servicos.ServicoVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venda")
public class ControleVenda {

    @Autowired
    private ServicoVenda servicoVenda;

    public ResponseEntity<?> cadastrarVenda(Venda vendaDados){
        try{
            Venda venda = servicoVenda.cadastrarVenda(vendaDados);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Venda Cadastrada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar venda: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/todas")
    public ResponseEntity<?> listagemVendas(){
        try{
            List<Venda> vendas = servicoVenda.listagemVendas();
            if (vendas.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem vendas registradas");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(vendas);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao fazer listagem de vendas: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listaVenda(@PathVariable Long id){
        try{
            Venda venda = servicoVenda.listaVenda(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(venda);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao listar venda: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaVenda(@PathVariable Long id){
        try{
            boolean venda = servicoVenda.deletaVenda(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Venda Deletada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao deletar venda: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }


}
