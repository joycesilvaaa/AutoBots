package com.autobots.automanager.service;

import com.autobots.automanager.dto.telefone.CriarTelefoneDto;
import com.autobots.automanager.dto.telefone.VerTelefoneDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.AdicionadorLinkTelefone;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelefoneService {
    private static final Logger logger = LoggerFactory.getLogger(TelefoneService.class);
    @Autowired
    private TelefoneRepositorio telefoneRepositorio;

    @Autowired
    private TelefoneSelecionador telefoneSelecionador;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ClienteSelecionador clienteSelecionador;

    @Autowired
    private AdicionadorLinkTelefone adicionadorLink;

    public Telefone cadastrarTelefone(long id,CriarTelefoneDto telefoneDto){
        try{
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,id);
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + id);
            }
            Telefone telefone = new Telefone();
            telefone.setDdd(telefoneDto.ddd());
            telefone.setNumero(telefoneDto.numero());
            cliente.getTelefones().add(telefone);
            clienteRepositorio.save(cliente);
            return telefone;
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public VerTelefoneDto verTelefone(long id){
        try{
            List<Telefone> telefones = telefoneRepositorio.findAll();
            Telefone telefone = telefoneSelecionador.selecionar(telefones,id);
            if (telefone == null){
                throw new EntityNotFoundException("Telefone não encontrado com ID: " + id);
            }
            adicionadorLink.adicionarLink(telefone);
            return new VerTelefoneDto(
                    telefone.getId(),
                    telefone.getDdd(),
                    telefone.getNumero(),
                    telefone.getLinks().toList()
            );
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public List<VerTelefoneDto> listarTelefones(){
        try{
            List<Telefone> telefones = telefoneRepositorio.findAll();
            telefones.forEach(telefone -> adicionadorLink.adicionarLink(telefone));
            return telefones.stream()
                    .map(telefone -> new VerTelefoneDto(
                            telefone.getId(),
                            telefone.getDdd(),
                            telefone.getNumero(),
                            telefone.getLinks().toList()
                    ))
                    .collect(Collectors.toList());
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public boolean excluirTelefone(long clienteId, long telefoneId){
        try{
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,clienteId);
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + clienteId);
            }
            Telefone telefone = telefoneSelecionador.selecionar(cliente.getTelefones(), telefoneId);
            if (telefone == null){
                throw new EntityNotFoundException("Telefone não encontrado com ID: " + telefoneId);
            }
            cliente.getTelefones().remove(telefone);
            clienteRepositorio.save(cliente);
            return true;
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }


}
