package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.CredencialCodigoBarra;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.auth.VerificadorPermissao;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.servicos.ServicoCredencial;
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
@RequestMapping("/credencial")
public class ControleCredencial {

    @Autowired
    private ServicoCredencial servicoCredencial;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private UsuarioSelecionador usuarioSelecionador;

    @Autowired
    private VerificadorPermissao verificadorPermissao;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/usuario/criar/{id}")
    public ResponseEntity<?> cadastrarCredencialUsuario(@PathVariable Long id,
                                                        @RequestBody CredencialUsuarioSenha credencialUsuarioDados,
                                                        Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            CredencialUsuarioSenha credencia= servicoCredencial.cadastrarCredencialUsuario(id, credencialUsuarioDados);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Credencial Criada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PutMapping("/usuario/{id}")
    public ResponseEntity<?> editarCredencialUsuario(@PathVariable Long id,
                                                     @RequestBody CredencialUsuarioSenha credencialUsuarioDados,
                                                     Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            CredencialUsuarioSenha credencial = servicoCredencial.editarCredencialUsuario(id, credencialUsuarioDados);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(credencial);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao editar credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/usuario/todos")
    public ResponseEntity<?> listagemCredencialUsuario(){
        try{
            List<CredencialUsuarioSenha> credenciais = servicoCredencial.listagemCredencial();
            if (credenciais.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem credenciais cadastradas");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(credenciais);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao lista credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> listaCredencialUsuario(@PathVariable Long id){
        try{
            CredencialUsuarioSenha credencial = servicoCredencial.listaCredencialUsuario(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(credencial);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao lista credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/usuario/{userId}/{credencialId}")
    public ResponseEntity<?> deletaCredencialUsuario(@PathVariable Long userId, @PathVariable Long credencialId){
        try{
            servicoCredencial.deletarCredencialUsuario(userId, credencialId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Credencial deletada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao deletar credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/codigo/criar/{id}")
    public ResponseEntity<?> cadastrarCredencialCodigo(@PathVariable Long id,
                                                       @RequestBody CredencialCodigoBarra credencialCodigoBarra,
                                                       Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            CredencialCodigoBarra credencial = servicoCredencial.cadastrarCredencialCodigo(id, credencialCodigoBarra);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Credencial Criada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/codigo/{id}")
    public ResponseEntity<?> editarCredencialCodigo(@PathVariable Long id,
                                                    @RequestBody CredencialCodigoBarra credencialCodigoBarra,
                                                    Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            String username = authentication.getName();
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            CredencialCodigoBarra credencial = servicoCredencial.editarCredencialCodigo(id, credencialCodigoBarra);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(credencial);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao editar credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/codigo/todos")
    public ResponseEntity<?> listagemCredencialCodigo(){
        try{
            List<CredencialCodigoBarra> credenciais = servicoCredencial.listagemCredencialCodigo();
            if (credenciais.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem credenciais cadastradas");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(credenciais);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao lista credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/codigo/{id}")
    public ResponseEntity<?> listaCredencialCodigo(@PathVariable Long id){
        try{
            CredencialCodigoBarra credencial = servicoCredencial.listaCredencialCodigo(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(credencial);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao lista credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/codigo/{userId}/{credencialId}")
    public ResponseEntity<?> deletaCredencialCodigo(@PathVariable Long userId, @PathVariable Long credencialId){
        try{
            servicoCredencial.deletaCredencialCodigo(userId, credencialId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Credencial deletada");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao deletar credencial: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
