package com.autobots.automanager.service;

import com.autobots.automanager.dto.documento.CriarDocumentoDto;
import com.autobots.automanager.dto.documento.VerDocumentoDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class DocumentoService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentoService.class);

    @Autowired
    private DocumentoRepositorio documentoRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ClienteSelecionador clienteSelecionador;

    public Documento cadastrarDocumento(long id, CriarDocumentoDto documentoDto){
        try {
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,id);
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + id);
            }
            Documento documento = new Documento();
            documento.setCliente(cliente);
            documento.setNumero(documentoDto.numero());
            documento.setTipo(documentoDto.tipo());
            documentoRepositorio.save(documento);
            return documentoRepositorio.save(documento);
        } catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar documento: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar documento: {}", e.getMessage());
            throw e;
        }
    }

    public VerDocumentoDto verDocumento(long id){
        try {
            Documento documento = documentoRepositorio.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado com ID: " + id));
            VerDocumentoDto documentoDto = new VerDocumentoDto(
                    documento.getId(),
                    documento.getTipo(),
                    documento.getNumero()
            );
            return documentoDto;
        }catch (DataIntegrityViolationException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<VerDocumentoDto> listaDocumentos(){
        try{
            List<Documento> documentos = documentoRepositorio.findAll();
            return documentos.stream()
                    .map(documento -> new VerDocumentoDto(
                            documento.getId(),
                            documento.getTipo(),
                            documento.getNumero()
                    ))
                    .collect(Collectors.toList());
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean excluirDocumento(long id){
        try {
            Documento documento = documentoRepositorio.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado com ID: " + id));
            documentoRepositorio.delete(documento);
            return true;
        }catch (DataIntegrityViolationException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
