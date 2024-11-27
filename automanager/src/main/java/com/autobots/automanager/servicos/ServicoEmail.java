package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkEmail;
import com.autobots.automanager.modelo.atualizadores.EmailAtualizador;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ServicoEmail {

    private static final Logger logger = LoggerFactory.getLogger(ServicoEmail.class);

    @Autowired
    private RepositorioEmail repositorioEmail;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private AdicionadorLinkEmail adicionadorLinkEmail;

    @Autowired
    private EmailAtualizador emailAtualizador;

    public Email cadastrarEmail(Long id ,Email email){
        try {
            Usuario usuario = repositorioUsuario.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Usuario com id {} não encontrado.", id);
                        return new EntityNotFoundException("Usuario não encontrado");
                    });
            Email novoEmail = new Email();
            novoEmail.setEndereco(email.getEndereco());
            usuario.getEmails().add(novoEmail);
            repositorioUsuario.save(usuario);
            return novoEmail;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar mercadoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar mercadoria: {}", e.getMessage());
            throw e;
        }
    }

    public Email editarEmail(Long id ,Email emailUpdate){
        try {
            Email email = repositorioEmail.findById(emailUpdate.getId())
                    .orElseThrow(() ->{
                        logger.error("E-mail com id {} não encontrado.", emailUpdate.getId());
                        return new EntityNotFoundException("E-mail não encontrado");
                    });
            emailAtualizador.atualizar(email, emailUpdate);
            repositorioEmail.save(email);
            return email;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao editar E-mail: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar E-mail: {}", e.getMessage());
            throw e;
        }
    }

    public List<Email> listagemEmails(){
        try {
            List<Email> email = repositorioEmail.findAll();
            adicionadorLinkEmail.adicionarLink(email);
            return email;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao fazer listagem de todos E-mail: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao fazer listagem de todos E-mail: {}", e.getMessage());
            throw e;
        }
    }

    public Email listarEmail(Long id){
        try {
            Email email = repositorioEmail.findById(id)
                    .orElseThrow(() ->{
                        logger.error("E-mail com id {} não encontrado.", id);
                        return new EntityNotFoundException("E-mail não encontrado");
                    });
            adicionadorLinkEmail.adicionarLink(email);
            return email;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar E-mail: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar E-mail: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteEmail(Long userId ,Long emailId){
        try {
            Usuario usuario = repositorioUsuario.findById(userId)
                    .orElseThrow(() ->{
                        logger.error("Usuario com id {} não encontrado.", userId);
                        return new EntityNotFoundException("Usuario não encontrado");
                    });
            Email email = repositorioEmail.findById(emailId)
                    .orElseThrow(() ->{
                        logger.error("E-mail com id {} não encontrado.", emailId);
                        return new EntityNotFoundException("E-mail não encontrado");
                    });
            boolean removido = usuario.getEmails().removeIf(emailUser -> emailUser.equals(email));
            if (removido) {
                repositorioUsuario.save(usuario);
                logger.info("E-mail com id {} removido do usuário com id {}.", emailId, userId);
            } else {
                logger.warn("O e-mail com id {} não está associado ao usuário com id {}.", emailId, userId);
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar o e-mail: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar o e-mail: {}", e.getMessage());
            throw e;
        }
    }

}
