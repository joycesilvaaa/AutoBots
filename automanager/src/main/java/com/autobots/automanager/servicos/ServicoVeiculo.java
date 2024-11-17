package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkVeiculo;
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
public class ServicoVeiculo {

    private static final Logger logger = LoggerFactory.getLogger(ServicoVeiculo.class);

    @Autowired
    private RepositorioVeiculo repositorioVeiculo;

    @Autowired
    private RepositorioVenda repositorioVenda;

    @Autowired
    private AdicionadorLinkVeiculo adicionadorLinkVeiculo;

    public Veiculo cadastrarVeiculo(Veiculo veiculo){
        try{
            return repositorioVeiculo.save(veiculo);
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar novo veiculo: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar novo veiculo: {}", e.getMessage());
            throw e;
        }
    }

    public List<Veiculo> listagemVeiculos(){
        try{
            List<Veiculo> veiculos = repositorioVeiculo.findAll();
            adicionadorLinkVeiculo.adicionarLink(veiculos);
            return veiculos;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao fazer listagem de veiculos: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao fazer listagem de veiculos: {}", e.getMessage());
            throw e;
        }
    }

    public Veiculo listaVeiculo(Long id){
        try{
            Veiculo veiculo = repositorioVeiculo.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Veiculo com id {} não encontrado.", id);
                        return new EntityNotFoundException("Veiculo não encontrada");
                    });
            adicionadorLinkVeiculo.adicionarLink(veiculo);
            return veiculo;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar veiculo: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar veiculo: {}", e.getMessage());
            throw e;
        }
    }

    public boolean deletarVeiculo(Long id){
        try{
            Veiculo veiculo = repositorioVeiculo.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Veiculo com id {} não encontrado.", id);
                        return new EntityNotFoundException("Veiculo não encontrada");
                    });
            List<Venda> vendas = repositorioVenda.findAll();
            for (Venda venda : vendas){
                if (venda.getVeiculo() != null && venda.getVeiculo().getId().equals(veiculo.getId())) {
                    venda.setVeiculo(null);
                }
            }
            veiculo.getVendas().clear();
            veiculo.getProprietario().getVeiculos().clear();
            repositorioVeiculo.delete(veiculo);
            return true;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar novo veiculo: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar novo veiculo: {}", e.getMessage());
            throw e;
        }
    }
}
