package com.autobots.automanager.servicos;

import com.autobots.automanager.dto.usuario.CriarUsuarioDto;
import com.autobots.automanager.entitades.*;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.atualizadores.UsuarioAtualizador;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServicoUsuario {

    private static final Logger logger = LoggerFactory.getLogger(ServicoUsuario.class);

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private RepositorioVenda repositorioVenda;

    @Autowired
    private AdicionadorLinkUsuario adicionadorLinkUsuario;

    @Autowired
    private UsuarioAtualizador usuarioAtualizador;

    public Usuario cadastrarUsuario(CriarUsuarioDto criarUsuarioDto){
        try{
            Usuario usuario = new Usuario();
            usuario.setNome(criarUsuarioDto.nome());
            usuario.setNomeSocial(criarUsuarioDto.nomeSocial());
            usuario.setPerfis(criarUsuarioDto.perfils());
            usuario.setTelefones(criarUsuarioDto.telefones());
            usuario.setEndereco(criarUsuarioDto.endereco());
            usuario.setDocumentos(criarUsuarioDto.documentos());
            usuario.setEmails(criarUsuarioDto.emails());
            Set<CredencialUsuarioSenha> credenciaisUsuarioSenha = criarUsuarioDto.credenciais();

            for (CredencialUsuarioSenha credencialUsuarioSenha : credenciaisUsuarioSenha) {
                CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
                credencial.setNomeUsuario(credencialUsuarioSenha.getNomeUsuario());
                credencial.setSenha(credencialUsuarioSenha.getSenha());
                credencial.setInativo(credencialUsuarioSenha.isInativo());
                credencial.setCriacao(credencialUsuarioSenha.getCriacao());
                credencial.setUltimoAcesso(credencialUsuarioSenha.getUltimoAcesso());
                usuario.getCredenciais().add(credencial);
            }

            if (criarUsuarioDto.mercadorias() != null){
                usuario.setMercadorias(criarUsuarioDto.mercadorias());
            }
            if (criarUsuarioDto.vendas() != null){
                usuario.setVendas(criarUsuarioDto.vendas());
            }
            if (criarUsuarioDto.veiculos() != null) {
                for (Veiculo veiculo : criarUsuarioDto.veiculos()) {
                    veiculo.setProprietario(usuario);
                    usuario.getVeiculos().add(veiculo);
                }
            }

            return  repositorioUsuario.save(usuario);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar cliente: {}", e.getMessage());
            throw e;
        }

    }

    public Usuario editarUsuario(Long id, Usuario usuarioUpdate){
        try {
            Usuario usuario = repositorioUsuario.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Usuário com id {} não encontrado.", id);
                        return new EntityNotFoundException("Usuário não encontrado");
                    });
            usuarioAtualizador.atualizar(usuario, usuarioUpdate);
            repositorioUsuario.save(usuario);
            return  usuario;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao editar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar cliente: {}", e.getMessage());
            throw e;
        }
    }

    public List<Usuario> listagemUsuarios(){
        try {
            List<Usuario> usuarios = repositorioUsuario.findAll();
            adicionadorLinkUsuario.adicionarLink(usuarios);
            return usuarios;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar cliente: {}", e.getMessage());
            throw e;
        }
    }

    public Usuario listaUsuario(Long id){
        try {
            Usuario usuario = repositorioUsuario.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Usuário com id {} não encontrado.", id);
                        return new EntityNotFoundException("Usuário não encontrado");
                    });
            adicionadorLinkUsuario.adicionarLink(usuario);
            return  usuario;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar cliente: {}", e.getMessage());
            throw e;
        }
    }

    public void deletarUsuario(Long id){
        try {
            Usuario usuario = repositorioUsuario.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Usuário com id {} não encontrado.", id);
                        return new EntityNotFoundException("Usuário não encontrado");
                    });
            List<Empresa> empresas = repositorioEmpresa.findAll();
            List<Venda> vendas = repositorioVenda.findAll();
            for (Empresa empresa : empresas) {
                if (empresa.getUsuarios().contains(usuario)) {
                    empresa.getUsuarios().remove(usuario);
                    repositorioEmpresa.save(empresa);
                }
            }
            for (Venda venda : vendas){
                if (venda.getCliente() != null && venda.getCliente().getId().equals(usuario.getId())) {
                    venda.setCliente(null);
                }
                if (venda.getFuncionario() != null && venda.getFuncionario().getId().equals(usuario.getId())) {
                    venda.setFuncionario(null);
                }
            }
            usuario.getPerfis().clear();
            usuario.getTelefones().clear();
            usuario.getMercadorias().clear();
            usuario.getVendas().clear();
            usuario.getVeiculos().clear();
            usuario.getEmails().clear();
            usuario.getCredenciais().clear();
            usuario.getDocumentos().clear();
            usuario.setEndereco(null);

            repositorioUsuario.delete(usuario);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar cliente: {}", e.getMessage());
            throw e;
        }
    }
}
