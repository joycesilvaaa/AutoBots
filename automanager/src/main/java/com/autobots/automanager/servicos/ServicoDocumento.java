package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelo.adicionadores.AdicionadorLinkDocumento;
import com.autobots.automanager.modelo.atualizadores.DocumentoAtualizador;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ServicoDocumento {

    private static final Logger logger = LoggerFactory.getLogger(ServicoDocumento.class);

    @Autowired
    private RepositorioDocumento repositorioDocumento;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private AdicionadorLinkDocumento adicionadorLinkDocumento;

    @Autowired
    private DocumentoAtualizador documentoAtualizador;

    public Documento cadastrarDocumento(Long id, Documento documento){
        try{
            Usuario usuario = repositorioUsuario.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Usuario com id {} não encontrado.", id);
                        return new EntityNotFoundException("Usuario não encontrado");
                    });

            Documento novoDocumento = new Documento();
            novoDocumento.setDataEmissao(documento.getDataEmissao());
            novoDocumento.setNumero(documento.getNumero());
            novoDocumento.setTipo(documento.getTipo());
            usuario.getDocumentos().add(novoDocumento);
            repositorioUsuario.save(usuario);
            return novoDocumento;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar documento: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar documento: {}", e.getMessage());
            throw e;
        }
    }

    public Documento editarDocumento(Long id, Documento documentoUpdate){
        try{
            Documento documento = repositorioDocumento.findById(documentoUpdate.getId())
                    .orElseThrow(() ->{
                        logger.error("Documento com id {} não encontrado.", documentoUpdate.getId());
                        return new EntityNotFoundException("Documento não encontrado");
                    });
            documentoAtualizador.atualizar(documento, documentoUpdate);
            repositorioDocumento.save(documento);
            return documento;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao cadastrar documento: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar documento: {}", e.getMessage());
            throw e;
        }
    }

    public List<Documento> listagemDocumentos(){
        try{
            List<Documento> documentos = repositorioDocumento.findAll();
            adicionadorLinkDocumento.adicionarLink(documentos);
            return documentos;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar todos documentos: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar todos documentos: {}", e.getMessage());
            throw e;
        }
    }

    public Documento listarDocumento(Long id){
        try{
            Documento documento = repositorioDocumento.findById(id)
                    .orElseThrow(() ->{
                        logger.error("Documento com id {} não encontrado.", id);
                        return new EntityNotFoundException("Documento não encontrado");
                    });
            adicionadorLinkDocumento.adicionarLink(documento);
            return documento;
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao listar documento: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao listar documento: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteDocumento(Long userId, Long documentoId){
        try{
            Usuario usuario = repositorioUsuario.findById(userId)
                    .orElseThrow(() ->{
                        logger.error("Usuario com id {} não encontrado.", userId);
                        return new EntityNotFoundException("Usuario não encontrado");
                    });

            Documento documento = repositorioDocumento.findById(documentoId)
                    .orElseThrow(() ->{
                        logger.error("Documento com id {} não encontrado.", documentoId);
                        return new EntityNotFoundException("Documento não encontrado");
                    });
            boolean removido = usuario.getDocumentos().removeIf(documentoUser -> documentoUser.equals(documento));
            if (removido) {
                repositorioUsuario.save(usuario);
                logger.info("E-mail com id {} removido do usuário com id {}.", documentoId, userId);
            } else {
                logger.warn("O e-mail com id {} não está associado ao usuário com id {}.", userId, userId);
            }
            repositorioDocumento.delete(documento);
        }catch (DataIntegrityViolationException e) {
            logger.error("Erro de integridade de dados ao deletar e-mail: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao delete e-mail: {}", e.getMessage());
            throw e;
        }
    }

}
