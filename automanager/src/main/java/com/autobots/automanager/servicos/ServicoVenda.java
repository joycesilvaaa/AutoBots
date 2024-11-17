package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ServicoVenda {

    private static final Logger logger = LoggerFactory.getLogger(ServicoVenda.class);

    @Autowired
    private RepositorioVenda repositorioVenda;

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    @Autowired
    private RepositorioVeiculo repositorioVeiculo;

    @Autowired
    private AdicionadorLinkVenda adicionadorLinkVenda;

    public Venda cadastrarVenda(Venda venda){
        try{
            return repositorioVenda.save(venda);
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar nova venda: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar nova venda: {}", e.getMessage());
            throw e;
        }
    }

    public List<Venda> listagemVendas(){
        try{
            List<Venda> vendas = repositorioVenda.findAll();
            adicionadorLinkVenda.adicionarLink(vendas);
            return vendas;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar todas venda: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar listar todas vendas: {}", e.getMessage());
            throw e;
        }
    }

    public Venda listaVenda(Long id){
        try{
            Venda venda = repositorioVenda.findById(id)
                    .orElseThrow(()->{
                        logger.error("Venda com id {} não encontrado.", id);
                        return new EntityNotFoundException("Venda não encontrada");
                    });
            adicionadorLinkVenda.adicionarLink(venda);
            return venda;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar venda: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar ao listar venda: {}", e.getMessage());
            throw e;
        }
    }

    public boolean deletaVenda(Long id){
        try{
            Venda venda = repositorioVenda.findById(id)
                    .orElseThrow(()->{
                        logger.error("Venda com id {} não encontrado.", id);
                        return new EntityNotFoundException("Venda não encontradada");
                    });
            List<Empresa> empresas = repositorioEmpresa.findAll();
            for (Empresa empresa : empresas) {
                if (empresa.getVendas().contains(venda)) {
                    empresa.getVendas().remove(venda);
                    repositorioEmpresa.save(empresa);
                }
            }

            List<Veiculo> veiculos = repositorioVeiculo.findAll();
            for (Veiculo veiculo : veiculos) {
                if (veiculo.getVendas().contains(venda)) {
                    veiculo.getVendas().remove(venda);
                    repositorioVeiculo.save(veiculo);
                }
            }
            venda.getMercadorias().clear();
            venda.getServicos().clear();
            repositorioVenda.delete(venda);
            return true;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar veiculo: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar veiculo: {}", e.getMessage());
            throw e;
        }
    }

}
