package com.autobots.automanager.service;

import com.autobots.automanager.dto.endereco.CriarEnderecoDto;
import com.autobots.automanager.dto.endereco.VerEnderecoDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class EnderecoService {

    @Autowired
    private EnderecoRepositorio enderecoRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ClienteSelecionador clienteSelecionador;

    public Endereco cadastrarEndereco(long id, CriarEnderecoDto enderecoDto){
        try {
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,id);
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + id);
            }
            Endereco endereco = new Endereco();
            endereco.setEstado(enderecoDto.estado());
            endereco.setCidade(enderecoDto.cidade());
            endereco.setBairro(enderecoDto.bairro());
            endereco.setRua(enderecoDto.rua());
            endereco.setNumero(enderecoDto.numero());
            endereco.setCodigoPostal(enderecoDto.codigoPostal());
            endereco.setInformacoesAdicionais(enderecoDto.informacoesAdicionais());
            endereco.setCliente(cliente);
            return enderecoRepositorio.save(endereco);
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public VerEnderecoDto verEndereco(long id){
        try {
            Endereco endereco = enderecoRepositorio.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + id));
            return new VerEnderecoDto(
                    endereco.getId(),
                    endereco.getEstado(),
                    endereco.getCidade(),
                    endereco.getBairro(),
                    endereco.getNumero(),
                    endereco.getRua(),
                    endereco.getCodigoPostal(),
                    endereco.getInformacoesAdicionais()
            );
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    public boolean excluirEndereco(long id){
        try {
            Endereco endereco = enderecoRepositorio.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + id));
            enderecoRepositorio.delete(endereco);
            return true;
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e){
            throw e;
        }
    }
}
