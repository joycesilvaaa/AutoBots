package com.autobots.automanager.utils;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.enumeracoes.PerfilUsuario;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioSelecionador {
    public Usuario selecionar(List<Usuario> usuarios, long id) {
        Usuario selecionado = null;
        for (Usuario usuario : usuarios) {
            if (usuario.getId() == id) {
                selecionado = usuario;
            }
        }
        return selecionado;
    }

    public Usuario selecionadorPorUsername(List<Usuario> usuarios, String username) {
        Usuario usuario = null;
        for(Usuario user : usuarios) {
            if (user.getCredencialUsuarioSenha().getNomeUsuario() == username) {
                return user;
            }
        }
        return usuario;
    }

    public List<Usuario> selecionarPorCargo(List<Usuario> usuarios, PerfilUsuario cargo){
        List<Usuario> filtrados = new ArrayList<Usuario>();
        for(Usuario usuario : usuarios) {
            if(usuario.getPerfis().contains(cargo)) {
                filtrados.add(usuario);
            }
        }
        return filtrados;
    }
}
