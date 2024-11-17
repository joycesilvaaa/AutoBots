package com.autobots.automanager.servicos;

import antlr.collections.impl.LList;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkServico;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ServicoServico {

    private static final Logger logger = LoggerFactory.getLogger(ServicoServico.class);

    @Autowired
    private RepositorioServico repositorioServico;

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private RepositorioVenda repositorioVenda;

    @Autowired
    private AdicionadorLinkServico adicionadorLinkServico;

     public Servico cadastrarServico(Servico servico){
         try{
             return repositorioServico.save(servico);
         }catch (DataIntegrityViolationException e) {
             logger.error("Erro de integridade de dados ao cadastrar servico: {}", e.getMessage());
             throw e;
         } catch (Exception e) {
             logger.error("Erro ao cadastrar servico: {}", e.getMessage());
             throw e;
         }
     }

    public List<Servico> listagemServicos(){
        try{
            List<Servico> servicos = repositorioServico.findAll();
            adicionadorLinkServico.adicionarLink(servicos);
            return servicos;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar servicos: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar servico: {}", e.getMessage());
            throw e;
        }
    }

    public Servico listaServico(Long id){
        try{
            Servico servico = repositorioServico.findById(id)
                    .orElseThrow(()->{
                        logger.error("Servico com id {} não encontrado.", id);
                        return new EntityNotFoundException("Servico não encontrada");
                    });
            adicionadorLinkServico.adicionarLink(servico);
            return servico;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar servicos: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar listar servico: {}", e.getMessage());
            throw e;
        }
    }

    public boolean deleteServico(Long id){
        try{
            Servico servico = repositorioServico.findById(id)
                    .orElseThrow(()->{
                        logger.error("Servico com id {} não encontrado.", id);
                        return new EntityNotFoundException("Servico não encontrada");
                    });
            List<Empresa> empresas = repositorioEmpresa.findAll();
            for (Empresa empresa : empresas) {
                if (empresa.getServicos().contains(servico)) {
                    empresa.getServicos().remove(servico);
                    repositorioEmpresa.save(empresa);
                }
            }
            List<Venda> vendas = repositorioVenda.findAll();
            for (Venda venda : vendas) {
                if (venda.getServicos().contains(servico)) {
                    venda.getServicos().remove(servico);
                    repositorioVenda.save(venda);
                }
            }
            repositorioServico.delete(servico);
            return true;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar servicos: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar listar servico: {}", e.getMessage());
            throw e;
        }
    }
}
