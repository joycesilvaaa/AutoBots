package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.modelo.auth.VerificadorPermissao;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.servicos.ServicoVeiculo;
import com.autobots.automanager.utils.UsuarioSelecionador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculo")
public class ControleVeiculo {

    @Autowired
    private ServicoVeiculo servicoVeiculo;
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private UsuarioSelecionador usuarioSelecionador;

    @Autowired
    private VerificadorPermissao verificadorPermissao;

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @PostMapping("/criar/{id}")
    public ResponseEntity<?> cadastrarVeiculo(@PathVariable Long id, @RequestBody Veiculo veiculoDados, Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            Veiculo veiculo = servicoVeiculo.cadastrarVeiculo(id,veiculoDados);
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

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editarVeiculo(@PathVariable Long id, @RequestBody Veiculo veiculoUpdate, Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            Veiculo veiculo = servicoVeiculo.editarVeiculo(id, veiculoUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Veiculo Atualizado");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao deletar veiculo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @GetMapping("/todos")
    public ResponseEntity<?> listagemVeiculo(){
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
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
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


    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
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
