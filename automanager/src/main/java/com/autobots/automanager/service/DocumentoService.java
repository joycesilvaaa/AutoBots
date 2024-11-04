package com.autobots.automanager.service;

import com.autobots.automanager.dto.documento.CriarDocumentoDto;
import com.autobots.automanager.dto.documento.VerDocumentoDto;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
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
    private DocumentoSelecionador documentoSelecionador;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private ClienteSelecionador clienteSelecionador;

    @Autowired
    private AdicionadorLinkDocumento adicionadorLink;


    public Documento cadastrarDocumento(long id, CriarDocumentoDto documentoDto){
        try {
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,id);
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + id);
            }
            Documento documento = new Documento();
            documento.setNumero(documentoDto.numero());
            documento.setTipo(documentoDto.tipo());
            cliente.getDocumentos().add(documento);
            clienteRepositorio.save(cliente);
            return documento;
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
            List<Documento> documentos = documentoRepositorio.findAll();
            Documento documento = documentoSelecionador.selecionar(documentos, id);
            if (documento == null){
                throw new EntityNotFoundException("Documento não encontrado com ID: " + id);
            }
            adicionadorLink.adicionarLink(documento);
            VerDocumentoDto documentoDto = new VerDocumentoDto(
                    documento.getId(),
                    documento.getTipo(),
                    documento.getNumero(),
                    documento.getLinks().toList()
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
            documentos.forEach(documento -> adicionadorLink.adicionarLink(documento));
            return documentos.stream()
                    .map(documento -> new VerDocumentoDto(
                            documento.getId(),
                            documento.getTipo(),
                            documento.getNumero(),
                            documento.getLinks().toList()
                    ))
                    .collect(Collectors.toList());
        }catch (DataIntegrityViolationException e){
            throw e;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean excluirDocumento(long clienteId, Long documentoId){
        try {
            List<Cliente> clientes = clienteRepositorio.findAll();
            Cliente cliente = clienteSelecionador.selecionar(clientes,clienteId);
            if (cliente == null){
                throw new EntityNotFoundException("Cliente não encontrado com ID: " + clienteId);
            }
            Documento documento = documentoSelecionador.selecionar(cliente.getDocumentos(), documentoId);
            if (documento == null){
                throw new EntityNotFoundException("Documento não encontrado com ID: " + documentoId);
            }
            cliente.getDocumentos().remove(documento);
            clienteRepositorio.save(cliente);
            return true;
        }catch (DataIntegrityViolationException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
