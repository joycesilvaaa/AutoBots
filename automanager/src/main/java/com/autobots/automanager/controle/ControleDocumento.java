package com.autobots.automanager.controle;

import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.auth.VerificadorPermissao;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.servicos.ServicoDocumento;
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
@RequestMapping("/documento")
public class ControleDocumento {

    @Autowired
    private ServicoDocumento servicoDocumento;
    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private UsuarioSelecionador usuarioSelecionador;

    @Autowired
    private VerificadorPermissao verificadorPermissao;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PostMapping("/criar/{id}")
    public ResponseEntity<?> cadastrarDocumento(@PathVariable Long id, @RequestBody Documento documentoDados, Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            Documento documento = servicoDocumento.cadastrarDocumento(id, documentoDados);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Documento criado com sucesso");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao cadastrar documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editarDocumento(@PathVariable Long id, @RequestBody Documento documentoUpdate, Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            String username = authentication.getName();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, id);
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            Documento documento = servicoDocumento.editarDocumento(id, documentoUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Documento editado com sucesso");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao editado documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/todos")
    public ResponseEntity<?> listagemDocumentos(){
        try{
            List<Documento> documentos = servicoDocumento.listagemDocumentos();
            if (documentos.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Sem documentos cadastrados");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(documentos);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao ver documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> listarDocumento(@PathVariable Long id){
        try{
            Documento documento = servicoDocumento.listarDocumento(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(documento);
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao ver documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    @DeleteMapping("/{userId}/{documentoId}")
    public ResponseEntity<?> deletarDocumento(@PathVariable Long userId, @PathVariable Long documentoId, Authentication authentication){
        try{
            List<Usuario> usuarios = repositorioUsuario.findAll();
            Usuario usuario = usuarioSelecionador.selecionar(usuarios, userId);
            String username = authentication.getName();
            Usuario usuarioLogado = usuarioSelecionador.selecionadorPorUsername(usuarios, username);
            boolean permissao = verificadorPermissao.verificar(usuarioLogado.getPerfis(),usuario.getPerfis());
            if (permissao == false) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não permitido");
            }
            servicoDocumento.deleteDocumento(userId, documentoId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Documento deletado");
        }catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ao ver documento: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado: " + e.getMessage());
        }
    }
}
