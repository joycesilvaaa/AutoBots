package com.autobots.automanager;

import java.time.LocalDate;

import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class AutomanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Component
	public static class Runner implements ApplicationRunner {
		@Autowired
		public ClienteRepositorio clienteRepositorio;

		@Autowired
		public TelefoneRepositorio telefoneRepositorio;

		@Autowired
		public EnderecoRepositorio enderecoRepositorio;

		@Autowired
		public DocumentoRepositorio documentoRepositorio;

		@Override
		@Transactional
		public void run(ApplicationArguments args) throws Exception {

			LocalDate dataNascimento = LocalDate.of(2002, 6, 15);
			LocalDate dataCadastro = LocalDate.now(); // Data atual

			Cliente cliente = new Cliente();
			cliente.setNome("Pedro Alcântara de Bragança e Bourbon");
			cliente.setDataCadastro(dataCadastro);
			cliente.setDataNascimento(dataNascimento);
			cliente.setNomeSocial("Dom Pedro");
			clienteRepositorio.save(cliente);

			Telefone telefone = new Telefone();
			telefone.setDdd("21");
			telefone.setNumero("981234576");
			telefone.setCliente(cliente);
			telefoneRepositorio.save(telefone);

			Endereco endereco = new Endereco();
			endereco.setEstado("Rio de Janeiro");
			endereco.setCidade("Rio de Janeiro");
			endereco.setBairro("Copacabana");
			endereco.setRua("Avenida Atlântica");
			endereco.setNumero("1702");
			endereco.setCodigoPostal("22021001");
			endereco.setInformacoesAdicionais("Hotel Copacabana Palace");
			endereco.setCliente(cliente);
			enderecoRepositorio.save(endereco);

			Documento rg = new Documento();
			rg.setTipo("RG");
			rg.setNumero("1500");
			rg.setCliente(cliente);
			documentoRepositorio.save(rg);

			Documento cpf = new Documento();
			cpf.setTipo("CPF");
			cpf.setNumero("00000000001");
			cpf.setCliente(cliente);
			documentoRepositorio.save(cpf);


			cliente.getTelefones().add(telefone);
			cliente.getDocumentos().add(rg);
			cliente.getDocumentos().add(cpf);

			clienteRepositorio.save(cliente);
		}
	}
}