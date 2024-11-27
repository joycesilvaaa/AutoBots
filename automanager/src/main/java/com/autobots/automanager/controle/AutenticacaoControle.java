package com.autobots.automanager.controle;

import com.autobots.automanager.dto.usuario.Login;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.jwt.ProvedorJwt;
import com.autobots.automanager.servicos.ServicoAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoControle {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProvedorJwt provedorJwt;

    @Autowired
    private ServicoAutenticacao autenticacaoServico;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.nomeUsuario(), login.senha())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = provedorJwt.proverJwt(userDetails.getUsername());
            return ResponseEntity.ok().header("Authorization", "Bearer " + jwt).body("Login realizado com sucesso");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Nome ou senhas inválidos");
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> register(@RequestBody CredencialUsuarioSenha credencial) {
        try {
            Usuario usuario = autenticacaoServico.registrarUsuario(credencial);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao registrar usuário");
        }
    }
}
