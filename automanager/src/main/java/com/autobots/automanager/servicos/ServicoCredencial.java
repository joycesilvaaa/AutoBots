package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.CredencialCodigoBarra;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkCredencialCodigoBarra;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkCredencialUsuarioSenha;
import com.autobots.automanager.modelo.atualizadores.CredencialCodigoBarraAtualizador;
import com.autobots.automanager.modelo.atualizadores.CredencialUsuarioSenhaAtualizador;
import com.autobots.automanager.repositorios.RepositorioCodigoBarra;
import com.autobots.automanager.repositorios.RepositorioCredencialUsuarioSenha;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ServicoCredencial {

    private static final Logger logger = LoggerFactory.getLogger(ServicoCredencial.class);

    @Autowired
    private RepositorioCredencialUsuarioSenha repositorioCredencialUsuarioSenha;

    @Autowired
    private RepositorioCodigoBarra repositorioCodigoBarra;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private AdicionadorLinkCredencialUsuarioSenha adicionadorLinkCredencialUsuarioSenha;

    @Autowired
    private AdicionadorLinkCredencialCodigoBarra adicionadorLinkCredencialCodigoBarra;

    @Autowired
    private CredencialUsuarioSenhaAtualizador credencialUsuarioSenhaAtualizador;

    @Autowired
    private CredencialCodigoBarraAtualizador credencialCodigoBarraAtualizador;

    public CredencialUsuarioSenha cadastrarCredencialUsuario(Long id, CredencialUsuarioSenha credencialUsuarioSenha){
        try {
            Usuario usuario = repositorioUsuario.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Usuario com id {} não encontrado.", id);
                        return new EntityNotFoundException("Usuario não encontrado");
                    });

            CredencialUsuarioSenha novaCredencialUsuario = new CredencialUsuarioSenha();
            novaCredencialUsuario.setCriacao(credencialUsuarioSenha.getCriacao());
            novaCredencialUsuario.setUltimoAcesso(credencialUsuarioSenha.getUltimoAcesso());
            novaCredencialUsuario.setInativo(credencialUsuarioSenha.isInativo());
            novaCredencialUsuario.setNomeUsuario(usuario.getNome());
            novaCredencialUsuario.setSenha(credencialUsuarioSenha.getSenha());
            usuario.getCredenciais().add(novaCredencialUsuario);
            repositorioUsuario.save(usuario);
            return novaCredencialUsuario;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar credencial do usuario: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar credencial: {}", e.getMessage());
            throw e;

        }
    }

    public CredencialUsuarioSenha editarCredencialUsuario(Long id, CredencialUsuarioSenha credencialUpdate){
        try {
            CredencialUsuarioSenha credencial = repositorioCredencialUsuarioSenha.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Credencial com id {} não encontrado.", id);
                        return new EntityNotFoundException("Credencial não encontrado");
                    });
           credencialUsuarioSenhaAtualizador.atualizar(credencial, credencialUpdate);
           repositorioCredencialUsuarioSenha.save(credencial);
            return credencial;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao editar credencial do usuario: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar credencial: {}", e.getMessage());
            throw e;

        }
    }

    public List<CredencialUsuarioSenha> listagemCredencial(){
        try{
            List<CredencialUsuarioSenha> credenciaisUsuario = repositorioCredencialUsuarioSenha.findAll();
            adicionadorLinkCredencialUsuarioSenha.adicionarLink(credenciaisUsuario);
            return credenciaisUsuario;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao ver todas credenciais: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao fazer listagem de credencial: {}", e.getMessage());
            throw e;

        }
    }

    public CredencialUsuarioSenha listaCredencialUsuario(Long id){
        try {
            CredencialUsuarioSenha credencialUsuarioSenha = repositorioCredencialUsuarioSenha.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Credencial com id {} não encontrado.", id);
                        return new EntityNotFoundException("Credencial não encontrado");
                    });
            adicionadorLinkCredencialUsuarioSenha.adicionarLink(credencialUsuarioSenha);
            return credencialUsuarioSenha;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao ver credencial : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao ver credencial: {}", e.getMessage());
            throw e;

        }
    }

    public void deletarCredencialUsuario(Long userId, Long credencialId){
        try {
            Usuario usuario = repositorioUsuario.findById(userId)
                    .orElseThrow(() ->{
                        logger.error("Usuario com id {} não encontrado.", userId);
                        return new EntityNotFoundException("Usuario não encontrado");
                    });
            CredencialUsuarioSenha credencialUsuarioSenha = repositorioCredencialUsuarioSenha.findById(credencialId)
                    .orElseThrow(() ->{
                        logger.error("Credencial com id {} não encontrado.", credencialId);
                        return new EntityNotFoundException("Credencial não encontrado");
                    });
            boolean removido = usuario.getCredenciais().removeIf(credencialUser -> credencialUser.equals(credencialUsuarioSenha));
            if (removido) {
                repositorioUsuario.save(usuario);
                logger.info("Credencial com id {} removido do usuário com id {}.", credencialId, userId);
            } else {
                logger.warn("O credencial com id {} não está associado ao usuário com id {}.", credencialId, userId);
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar credencial : {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar credencial: {}", e.getMessage());
            throw e;

        }
    }


    public CredencialCodigoBarra cadastrarCredencialCodigo(Long id, CredencialCodigoBarra codigoBarra){
        try {
            Usuario usuario = repositorioUsuario.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Usuario com id {} não encontrado.", id);
                        return new EntityNotFoundException("Usuario não encontrado");
                    });
            CredencialCodigoBarra novaCredencialCodigo = new CredencialCodigoBarra();
            novaCredencialCodigo.setCriacao(codigoBarra.getCriacao());
            novaCredencialCodigo.setUltimoAcesso(codigoBarra.getUltimoAcesso());
            novaCredencialCodigo.setCodigo(codigoBarra.getCodigo());
            novaCredencialCodigo.setInativo(codigoBarra.isInativo());
            usuario.getCredenciais().add(novaCredencialCodigo);
            repositorioUsuario.save(usuario);
            return novaCredencialCodigo;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar credencial: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar credencial: {}", e.getMessage());
            throw e;

        }
    }

    public CredencialCodigoBarra editarCredencialCodigo(Long id, CredencialCodigoBarra credencialUpdate){
        try {
            CredencialCodigoBarra credencial = repositorioCodigoBarra.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Credencial com id {} não encontrado.", id);
                        return new EntityNotFoundException("Credencial não encontrado");
                    });
            credencialCodigoBarraAtualizador.atualizar(credencial, credencialUpdate);
            repositorioCodigoBarra.save(credencial);
            return credencial;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar credencial: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar credencial: {}", e.getMessage());
            throw e;

        }
    }

    public List<CredencialCodigoBarra> listagemCredencialCodigo(){
        try {
            List<CredencialCodigoBarra> credenciais = repositorioCodigoBarra.findAll();
            adicionadorLinkCredencialCodigoBarra.adicionarLink(credenciais);
            return credenciais;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao fazer listagem de credencial: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao fazer listagem credencial: {}", e.getMessage());
            throw e;
        }
    }

    public CredencialCodigoBarra listaCredencialCodigo(Long id){
        try {
            CredencialCodigoBarra credencialCodigoBarra = repositorioCodigoBarra.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Credencial com id {} não encontrado.", id);
                        return new EntityNotFoundException("Credencial não encontrado");
                    });
            adicionadorLinkCredencialCodigoBarra.adicionarLink(credencialCodigoBarra);
            return credencialCodigoBarra;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar credencial: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar credencial: {}", e.getMessage());
            throw e;

        }
    }

    public void deletaCredencialCodigo(Long userId,Long credencialId){
        try {
            Usuario usuario = repositorioUsuario.findById(userId)
                    .orElseThrow(() ->{
                        logger.error("Usuario com id {} não encontrado.", userId);
                        return new EntityNotFoundException("Usuario não encontrado");
                    });
            CredencialCodigoBarra credencialCodigoBarra = repositorioCodigoBarra.findById(credencialId)
                    .orElseThrow(() ->{
                        logger.error("Credencial com id {} não encontrado.", credencialId);
                        return new EntityNotFoundException("Credencial não encontrado");
                    });
            boolean removido = usuario.getCredenciais().removeIf(credencialUser -> credencialUser.equals(credencialCodigoBarra));
            if (removido) {
                repositorioUsuario.save(usuario);
                logger.info("Credencial com id {} removido do usuário com id {}.", credencialId, userId);
            } else {
                logger.warn("O credencial com id {} não está associado ao usuário com id {}.", credencialId, userId);
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar credencial: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar credencial: {}", e.getMessage());
            throw e;

        }
    }

}
