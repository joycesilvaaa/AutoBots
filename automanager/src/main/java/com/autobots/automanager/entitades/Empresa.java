package com.autobots.automanager.entitades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Entity
public class Empresa extends RepresentationModel<Empresa> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String razaoSocial;
	@Column
	private String nomeFantasia;
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Telefone> telefones = new HashSet<>();
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Endereco endereco;
	@Column(nullable = false)
	private Date cadastro;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Usuario> usuarios = new HashSet<>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Mercadoria> mercadorias = new HashSet<>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Servico> servicos = new HashSet<>();
	@JsonIgnoreProperties(value = {"proprietario", "veiculo"})
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Venda> vendas = new HashSet<>();
}