package com.autobots.automanager.controle;

import com.autobots.automanager.dto.usuario.CriarUsuarioDto;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.servicos.ServicoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class ControleUsuario {

    @Autowired
    private ServicoUsuario servicoUsuario;

    @PostMapping("/criar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody CriarUsuarioDto criarUsuarioDto){
        try {
            System.out.println(criarUsuarioDto);
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

    @GetMapping("/{usuario_id}")
    public ResponseEntity<?> listaUsuario(@PathVariable Long usuario_id){
        try {
            Usuario usuario = servicoUsuario.listaUsuario(usuario_id);
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
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletaUsuario(@PathVariable Long id){
        try {
            System.out.println(id);
            boolean usuario = servicoUsuario.deletarUsuario(id);
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
