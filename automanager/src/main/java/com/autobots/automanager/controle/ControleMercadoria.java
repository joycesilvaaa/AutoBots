package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.auth.VerificadorPermissao;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.servicos.ServicoMercadoria;
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
@RequestMapping("/mercadoria")
public class ControleMercadoria {

    @Autowired
    private ServicoMercadoria servicoMercadoria;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private UsuarioSelecionador usuarioSelecionador;

    @Autowired
    private VerificadorPermissao verificadorPermissao;

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping("/criar")
    public ResponseEntity<?> cadastrarMercadoria(@RequestBody Mercadoria mercadoriaNova, Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
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
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editarMercadoria(@PathVariable Long id, @RequestBody Mercadoria mercadoriaUpdate, Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
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
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE', 'VENDEDOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE', 'VENDEDOR')")
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
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarMercadoria(@PathVariable Long id, Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
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
