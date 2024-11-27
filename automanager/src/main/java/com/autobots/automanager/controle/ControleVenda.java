package com.autobots.automanager.controle;

import com.autobots.automanager.dto.venda.VendaDto;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelo.auth.VerificadorPermissao;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.servicos.ServicoVenda;
import com.autobots.automanager.utils.UsuarioSelecionador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venda")
public class ControleVenda {

    @Autowired
    private ServicoVenda servicoVenda;
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private UsuarioSelecionador usuarioSelecionador;

    @Autowired
    private VerificadorPermissao verificadorPermissao;
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/criar")
    public ResponseEntity<?> cadastrarVenda(@RequestBody Venda vendaDados){
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
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editarVenda(@PathVariable Long id,@RequestBody VendaDto vendaUpdate){
        try{
            Venda venda = servicoVenda.editarVenda(id, vendaUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Venda Atualizada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao editar venda: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
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
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaVenda(@PathVariable Long id){
        try{
            servicoVenda.deletaVenda(id);
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
