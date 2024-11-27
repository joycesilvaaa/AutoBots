package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelo.atualizadores.MercadoriaAtualizador;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ServicoMercadoria {

    private static final Logger logger = LoggerFactory.getLogger(ServicoMercadoria.class);

    @Autowired
    private RepositorioMercadoria repositorioMercadoria;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private RepositorioVenda repositorioVenda;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorLinkMercadoria;

    @Autowired
    private MercadoriaAtualizador mercadoriaAtualizador;

    public Mercadoria cadastrarMercadoria(Mercadoria mercadoria){
        try {
            return repositorioMercadoria.save(mercadoria);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar mercadoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar mercadoria: {}", e.getMessage());
            throw e;
        }
    }

    public Mercadoria editarMercadoria(Long id, Mercadoria mercadoriaUpdate){
        try {
            Mercadoria mercadoria = repositorioMercadoria.findById(mercadoriaUpdate.getId())
                    .orElseThrow(() ->{
                        logger.error("Mercadoria com id {} não encontrado.", id);
                        return new EntityNotFoundException("Mercadoria não encontrada");
                    });
            mercadoriaAtualizador.atualizar(mercadoria, mercadoriaUpdate);
            return repositorioMercadoria.save(mercadoria);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao editar mercadoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar mercadoria: {}", e.getMessage());
            throw e;
        }
    }

    public List<Mercadoria> listagemMercadoria(){
        try {
            List<Mercadoria> mercadorias = repositorioMercadoria.findAll();
            adicionadorLinkMercadoria.adicionarLink(mercadorias);
            return mercadorias;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar mercadorias: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar mercadorias: {}", e.getMessage());
            throw e;
        }
    }

    public Mercadoria listarMercadoria(Long id){
        try {
            Mercadoria mercadoria = repositorioMercadoria.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Mercadoria com id {} não encontrado.", id);
                        return new EntityNotFoundException("Mercadoria não encontrada");
                    });
            adicionadorLinkMercadoria.adicionarLink(mercadoria);
            return mercadoria;
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar mercadoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar mercadoria: {}", e.getMessage());
            throw e;
        }
    }

    public void deletarMercadoria(Long id){
        try {
            Mercadoria mercadoria = repositorioMercadoria.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Mercadoria com id {} não encontrado.", id);
                        return new EntityNotFoundException("Mercadoria não encontrada");
                    });
            List<Empresa> empresas = repositorioEmpresa.findAll();
            for (Empresa empresa : empresas) {
                if (empresa.getMercadorias().contains(mercadoria)) {
                    empresa.getMercadorias().remove(mercadoria);
                    repositorioEmpresa.save(empresa);
                }
            }
            List<Venda> vendas = repositorioVenda.findAll();
            for (Venda venda : vendas) {
                if (venda.getMercadorias().contains(mercadoria)) {
                    venda.getMercadorias().remove(mercadoria);
                    repositorioVenda.save(venda);
                }
            }
            List<Usuario> usuarios = repositorioUsuario.findAll();
            for (Usuario usuario : usuarios){
                if(usuario.getMercadorias().contains(mercadoria)){
                    usuario.getMercadorias().remove(mercadoria);
                    repositorioUsuario.save(usuario);
                }
            }
            repositorioMercadoria.delete(mercadoria);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar mercadoria: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar mercadoria: {}", e.getMessage());
            throw e;
        }
    }
}
