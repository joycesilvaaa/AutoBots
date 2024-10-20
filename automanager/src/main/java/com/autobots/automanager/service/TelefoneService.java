package com.autobots.automanager.service;

import com.autobots.automanager.dto.telefone.CriarTelefoneDto;
import com.autobots.automanager.dto.telefone.VerTelefoneDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class TelefoneService {
    @Autowired
    private TelefoneRepositorio telefoneRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ClienteSelecionador clienteSelecionador;

    public Telefone cadastrarTelefone(long id,CriarTelefoneDto telefoneDto){
        try{
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,id);
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + id);
            }
            Telefone telefone = new Telefone();
            telefone.setCliente(cliente);
            telefone.setDdd(telefoneDto.ddd());
            telefone.setNumero(telefoneDto.numero());
            return telefoneRepositorio.save(telefone);
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public VerTelefoneDto verTelefone(long id){
        try{
            Telefone telefone = telefoneRepositorio.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Telefone não encontrado com ID: " + id));
            return new VerTelefoneDto(
                    telefone.getId(),
                    telefone.getDdd(),
                    telefone.getNumero()
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
            return telefones.stream()
                    .map(telefone -> new VerTelefoneDto(
                            telefone.getId(),
                            telefone.getDdd(),
                            telefone.getNumero()
                    ))
                    .collect(Collectors.toList());
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public boolean excluirTelefone(long id){
        try{
            Telefone telefone = telefoneRepositorio.getById(id);
            telefoneRepositorio.delete(telefone);
            return true;
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }


}
