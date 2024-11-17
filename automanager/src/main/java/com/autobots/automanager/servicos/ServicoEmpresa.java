package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelo.atualizadores.EmpresaAtualizador;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ServicoEmpresa {

    private static final Logger logger = LoggerFactory.getLogger(ServicoEmpresa.class);

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private AdicionadorLinkEmpresa adicionadorLinkEmpresa;

    @Autowired
    private EmpresaAtualizador empresaAtualizador;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public  Empresa cadastraEmpresa(Empresa empresa) {
        try {
            return repositorioEmpresa.save(empresa);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar empresa: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar empresa: {}", e.getMessage());
            throw e;
        }
    }

    public Empresa editarEmpresa(Long id, Empresa empresaUpdate) {
        try {
            Empresa empresa = repositorioEmpresa.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Empresa com id {} não encontrada.", id);
                        return new EntityNotFoundException("Empresa não encontrada");
                    });
            empresaAtualizador.atualizar(empresa, empresaUpdate);
            repositorioEmpresa.save(empresa);
            return empresa;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao editar empresa: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar empresa: {}", e.getMessage());
            throw e;
        }
    }


    public List<Empresa> listagemEmpresa(){
        try{
            List<Empresa> empresas = repositorioEmpresa.findAll();
            adicionadorLinkEmpresa.adicionarLink(empresas);
            return empresas;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao fazer listagem de empresas: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao fazer listagem de empresas: {}", e.getMessage());
            throw e;
        }
    }

    public Empresa listaEmpresa(Long id){
         try{
             Empresa empresa = repositorioEmpresa.findById(id)
                     .orElseThrow(() -> {
                         logger.error("Empresa com id {} não encontrado.", id);
                         return new EntityNotFoundException("Empresa não encontrada");
                     });
             adicionadorLinkEmpresa.adicionarLink(empresa);
             return empresa;
         }catch (DataIntegrityViolationException e) {
             logger.error("Erro de integridade de dados ao listar empresa: {}", e.getMessage());
             throw e;
         } catch (Exception e) {
             logger.error("Erro ao listar empresa: {}", e.getMessage());
             throw e;
         }
    }

    public void deletarEmpresa(Long id){
        try{
            Empresa empresa = repositorioEmpresa.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Empresa com id {} não encontrado.", id);
                        return new EntityNotFoundException("Empresa não encontrada");
                    });
            empresa.getServicos().clear();
            empresa.getUsuarios().clear();
            empresa.getVendas().clear();
            empresa.getMercadorias().clear();
            empresa.getTelefones().clear();
            repositorioEmpresa.delete(empresa);
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar empresa: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar empresa: {}", e.getMessage());
            throw e;
        }
    }

    public void adicionarUsuario(Long userId, Long empresaId){
        try {
            Usuario usuario = repositorioUsuario.findById(userId)
                    .orElseThrow(() -> {
                        logger.error("Usuário com id {} não encontrado.", userId);
                        return new EntityNotFoundException("Usuário não encontrado");
                    });
            Empresa empresa = repositorioEmpresa.findById(empresaId)
                    .orElseThrow(() -> {
                        logger.error("Empresa com id {} não encontrado.", empresaId);
                        return new EntityNotFoundException("Empresa não encontrada");
                    });
            empresa.getUsuarios().add(usuario);
            repositorioEmpresa.save(empresa);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao realizar associação: {}", e.getMessage());
            throw e;
        }
    }

}
