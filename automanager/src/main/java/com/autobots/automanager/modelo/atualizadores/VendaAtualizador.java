package com.autobots.automanager.modelo.atualizadores;

import com.autobots.automanager.dto.venda.VendaDto;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class VendaAtualizador {

    @Autowired
    private StringVerificadorNulo verificador;

    @Autowired
    private RepositorioVenda repositorioVenda;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioVeiculo repositorioVeiculo;

    @Autowired
    private RepositorioServico repositorioServico;

    @Autowired
    private RepositorioMercadoria repositorioMercadoria;

    public void atualizar(Venda venda, VendaDto atualizacao) {
        if (!verificador.verificar(atualizacao.identificacao())) {
            venda.setIdentificacao(atualizacao.identificacao());
        }

        Optional<Usuario> clienteOpt = repositorioUsuario.findById(atualizacao.cliente());
        clienteOpt.ifPresent(venda::setCliente);

        Optional<Usuario> funcionarioOpt = repositorioUsuario.findById(atualizacao.funcionario());
        funcionarioOpt.ifPresent(venda::setFuncionario);

        Optional<Veiculo> veiculoOpt = repositorioVeiculo.findById(atualizacao.veiculo());
        veiculoOpt.ifPresent(veiculo -> {
            if (venda.getVeiculo() != null) {
                venda.getVeiculo().getVendas().remove(venda);
            }
            veiculo.getVendas().add(venda);
            venda.setVeiculo(veiculo);
        });

        Set<Servico> servicos = new HashSet<>();
        for (Long servicoId : atualizacao.servicos()) {
            repositorioServico.findById(servicoId).ifPresent(servicos::add);
        }
        venda.setServicos(servicos);

        Set<Mercadoria> mercadorias = new HashSet<>();
        for (Long mercadoriaId : atualizacao.mercadorias()) {
            repositorioMercadoria.findById(mercadoriaId).ifPresent(mercadorias::add);
        }
        venda.setMercadorias(mercadorias);
    }

    public void atualizar(Set<Venda> vendas, Set<VendaDto> atualizacoes) {
        for (VendaDto atualizacao : atualizacoes) {
            for (Venda venda : vendas) {
                if (atualizacao.id().equals(venda.getId())) {
                    atualizar(venda, atualizacao);
                }
            }
        }
    }
}

