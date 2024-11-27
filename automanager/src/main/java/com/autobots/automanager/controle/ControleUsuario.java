package com.autobots.automanager.controle;

import com.autobots.automanager.dto.usuario.CriarUsuarioDto;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.auth.VerificadorPermissao;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.servicos.ServicoUsuario;
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
@RequestMapping("/usuario")
public class ControleUsuario {

    @Autowired
    private ServicoUsuario servicoUsuario;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private UsuarioSelecionador usuarioSelecionador;

    @Autowired
    private VerificadorPermissao verificadorPermissao;

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @PostMapping("/criar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody CriarUsuarioDto criarUsuarioDto){
        try {
            Usuario usuarioCadastrado = servicoUsuario.cadastrarUsuario(criarUsuarioDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Cliente Cadastrado.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar cliente: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioUpdate, Authentication authentication){
        try {
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            Usuario usuarioAtualizado = servicoUsuario.editarUsuario(id, usuarioUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Usuario Atualizado");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao editar cliente: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/todos")
    public ResponseEntity<?> listagemUsuarios(){
        try {
            List<Usuario> usuarios = servicoUsuario.listagemUsuarios();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(usuarios);
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> listaUsuario(@PathVariable Long id){
        try {
            Usuario usuario = servicoUsuario.listaUsuario(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(usuario);
        }  catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao ver usuario: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletaUsuario(@PathVariable Long id, Authentication authentication){
        try {
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            servicoUsuario.deletarUsuario(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Usuario Excluido");
        }  catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao ver usuario: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
