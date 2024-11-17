package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkEmpresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
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

            if (empresaUpdate.getRazaoSocial() != null) {
                empresa.setRazaoSocial(empresaUpdate.getRazaoSocial());
            }
            if (empresaUpdate.getNomeFantasia() != null) {
                empresa.setNomeFantasia(empresaUpdate.getNomeFantasia());
            }
            if (empresaUpdate.getTelefones() != null) {
                empresa.getTelefones().removeIf(telefone -> !empresaUpdate.getTelefones().contains(telefone));
                for (Telefone novoTelefone : empresaUpdate.getTelefones()) {
                    if (!empresa.getTelefones().contains(novoTelefone)) {
                        empresa.getTelefones().add(novoTelefone);
                    }
                }}
            if (empresaUpdate.getEndereco() != null) {
                Endereco enderecoUpdate = empresaUpdate.getEndereco();
                Endereco enderecoAtual = empresa.getEndereco();

                if (enderecoUpdate.getEstado() != null) {
                    enderecoAtual.setEstado(enderecoUpdate.getEstado());
                }
                if (enderecoUpdate.getCidade() != null) {
                    enderecoAtual.setCidade(enderecoUpdate.getCidade());
                }
                if (enderecoUpdate.getBairro() != null) {
                    enderecoAtual.setBairro(enderecoUpdate.getBairro());
                }
                if (enderecoUpdate.getRua() != null) {
                    enderecoAtual.setRua(enderecoUpdate.getRua());
                }
                if (enderecoUpdate.getNumero() != null) {
                    enderecoAtual.setNumero(enderecoUpdate.getNumero());
                }
                if (enderecoUpdate.getCodigoPostal() != null) {
                    enderecoAtual.setCodigoPostal(enderecoUpdate.getCodigoPostal());
                }
                if (enderecoUpdate.getInformacoesAdicionais() != null) {
                    enderecoAtual.setInformacoesAdicionais(enderecoUpdate.getInformacoesAdicionais());
                }

                empresa.setEndereco(enderecoAtual);
            }
            if (empresaUpdate.getCadastro() != null) {
                empresa.setCadastro(empresaUpdate.getCadastro());
            }
            if (empresaUpdate.getUsuarios() != null) {
                empresa.setUsuarios(empresaUpdate.getUsuarios());
            }
            if (empresaUpdate.getVendas() != null) {
                empresa.setVendas(empresaUpdate.getVendas());
            }
            if (empresaUpdate.getMercadorias() != null) {
                empresa.setMercadorias(empresaUpdate.getMercadorias());
            }
            if(empresaUpdate.getServicos() != null){
                empresa.setServicos(empresaUpdate.getServicos());
            }

            return repositorioEmpresa.save(empresa);
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

    public boolean deletarEmpresa(Long id){
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
            return true;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar empresa: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar empresa: {}", e.getMessage());
            throw e;
        }
    }

}
