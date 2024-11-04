package com.autobots.automanager.service;
import com.autobots.automanager.dto.cliente.AtualizarClienteDto;
import com.autobots.automanager.dto.cliente.VerClienteDto;
import com.autobots.automanager.modelo.AdicionadorLinkCliente;
import com.autobots.automanager.modelo.ClienteAtualizador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autobots.automanager.dto.cliente.CriarClienteDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private ClienteSelecionador clienteSelecionador;
    @Autowired
    private AdicionadorLinkCliente adicionadorLink;

    public Cliente cadastrarCliente(CriarClienteDto clienteDto){
        try{
            Cliente cliente = new Cliente();
            cliente.setNome(clienteDto.nome());
            cliente.setNome(clienteDto.nome());
            cliente.setNomeSocial(clienteDto.nomeSocial());
            cliente.setDataNascimento(clienteDto.dataNascimento());
            cliente.setDataCadastro(LocalDate.now());
            cliente.setDocumentos(clienteDto.documentos());
            cliente.setEndereco(clienteDto.endereco());
            cliente.setTelefones(clienteDto.telefones());
            return clienteRepositorio.save(cliente);
        }  catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar cliente: {}", e.getMessage());
            throw e;
        }
    }

    public Cliente atualizarCliente(AtualizarClienteDto atualizarClienteDto){
        try{
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,atualizarClienteDto.id());
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + atualizarClienteDto.id());
            }
            ClienteAtualizador  atualizador = new ClienteAtualizador();
            atualizador.atualizar(cliente, atualizarClienteDto);
            return clienteRepositorio.save(cliente);
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao editar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao editar cliente: {}", e.getMessage());
            throw e;
        }
    }

    public List<VerClienteDto> listaClientes(){
        try{
            List<Cliente> clientes = clienteRepositorio.findAll();
            clientes.forEach(cliente -> adicionadorLink.adicionarLink(cliente));
            return clientes.stream()
                    .map(cliente -> new VerClienteDto(
                            cliente.getId(),
                            cliente.getNome(),
                            cliente.getNomeSocial(),
                            cliente.getDataNascimento(),
                            cliente.getDocumentos(),
                            cliente.getEndereco(),
                            cliente.getTelefones(),
                            cliente.getLinks().toList()
                    ))
                    .collect(Collectors.toList());
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar clientes: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar clientes: {}", e.getMessage());
            throw e;
        }
    }

    public VerClienteDto verCliente(long id){
        try{
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,id);
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + id);
            }

            adicionadorLink.adicionarLink(cliente);

            return new VerClienteDto(
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getNomeSocial(),
                    cliente.getDataNascimento(),
                    cliente.getDocumentos(),
                    cliente.getEndereco(),
                    cliente.getTelefones(),
                    cliente.getLinks().toList()
            );
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao visualizar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao visualizar cliente: {}", e.getMessage());
            throw e;
        }
    }

    public boolean excluirCliente(long id){
        try{
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,id);
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + id);
            }
           clienteRepositorio.delete(cliente);
            return true;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao deletar cliente: {}", e.getMessage());
            throw e;
        }
    }

}