package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkTelefone;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ServicoTelefone {

    private static final Logger logger = LoggerFactory.getLogger(ServicoVenda.class);

    @Autowired
    private RepositorioTelefone repositorioTelefone;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private AdicionadorLinkTelefone adicionadorLinkTelefone;

    public Telefone cadastrarTelefoneUsuario(Long id, Telefone telefone){
        try{
            Usuario usuario = repositorioUsuario.findById(id)
                    .orElseThrow(()->{
                        logger.error("Usuario com id {} não encontrado.", id);
                        return new EntityNotFoundException("Usuario não encontrada");
                    });
            Telefone novoTelefone = new Telefone();
            novoTelefone.setDdd(telefone.getDdd());
            novoTelefone.setNumero(telefone.getNumero());
            usuario.getTelefones().add(novoTelefone);
            repositorioUsuario.save(usuario);
            return novoTelefone;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar novo telefone: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar novo telefone: {}", e.getMessage());
            throw e;
        }
    }

    public Telefone cadastrarTelefoneEmpresa(Long id, Telefone telefone){
        try{
            Empresa empresa = repositorioEmpresa.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Empresa com id {} não encontrado.", id);
                        return new EntityNotFoundException("Empresa não encontrada");
                    });
            Telefone novoTelefone = new Telefone();
            empresa.getTelefones().add(novoTelefone);
            repositorioEmpresa.save(empresa);
            return novoTelefone;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar novo telefone: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar novo telefone: {}", e.getMessage());
            throw e;
        }
    }

    public List<Telefone> listagemTelefones(){
        try{
            List<Telefone> telefones = repositorioTelefone.findAll();
            adicionadorLinkTelefone.adicionarLink(telefones);
            return telefones;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao fazer listagem de telefone: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao fazer listagem de telefones: {}", e.getMessage());
            throw e;
        }
    }

    public Telefone listaTelefone(Long id){
        try{
            Telefone telefone = repositorioTelefone.findById(id)
                    .orElseThrow(()->{
                        logger.error("Telefone com id {} não encontrado.", id);
                        return new EntityNotFoundException("Telefone não encontrada");
                    });
            adicionadorLinkTelefone.adicionarLink(telefone);
            return telefone;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao lista um telefone: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar telefone: {}", e.getMessage());
            throw e;
        }
    }

    public boolean deletaTelefone(Long id){
        try{
            Telefone telefone = repositorioTelefone.findById(id)
                    .orElseThrow(()->{
                        logger.error("Telefone com id {} não encontrado.", id);
                        return new EntityNotFoundException("Telefone não encontrada");
                    });
            List<Empresa> empresas = repositorioEmpresa.findAll();
            for (Empresa empresa : empresas) {
                if (empresa.getTelefones().contains(telefone)) {
                    empresa.getTelefones().remove(telefone);
                    repositorioEmpresa.save(empresa);
                }
            }
            List<Usuario> usuarios = repositorioUsuario.findAll();
            for (Usuario usuario : usuarios){
                if(usuario.getTelefones().contains(telefone)){
                    usuario.getTelefones().remove(telefone);
                    repositorioUsuario.save(usuario);
                }
            }
            repositorioTelefone.delete(telefone);
            return true;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar telefone: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar telefone: {}", e.getMessage());
            throw e;
        }
    }
}
