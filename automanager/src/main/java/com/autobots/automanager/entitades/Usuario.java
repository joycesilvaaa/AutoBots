package com.autobots.automanager.entitades;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.autobots.automanager.enumeracoes.PerfilUsuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(exclude = { "mercadorias", "vendas", "veiculos" })
@Entity
public class Usuario extends RepresentationModel<Usuario> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Column
	private String nomeSocial;
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<PerfilUsuario> perfis = new HashSet<>();
	@OneToMany(orphanRemoval = true,fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Set<Telefone> telefones = new HashSet<>();
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Endereco endereco;
	@OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Set<Documento> documentos = new HashSet<>();
	@OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Set<Email> emails = new HashSet<>();
	@OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Set<Credencial> credenciais = new HashSet<>();
	@OneToMany(orphanRemoval = true,fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Set<Mercadoria> mercadorias = new HashSet<>();
	@OneToMany(orphanRemoval = true,fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Set<Venda> vendas = new HashSet<>();
	@JsonIgnoreProperties(value = {"proprietario", "vendas"})
	@OneToMany(orphanRemoval = true,fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Set<Veiculo> veiculos = new HashSet<>();

	public Set<CredencialUsuarioSenha> getCredenciaisUsuarioSenha() {
		Set<CredencialUsuarioSenha> credenciaisUsuarioSenha = new HashSet<>();
		for (Credencial credencial : credenciais) {
			if (credencial instanceof CredencialUsuarioSenha) {
				credenciaisUsuarioSenha.add((CredencialUsuarioSenha) credencial);
			}
		}
		return credenciaisUsuarioSenha;
	}

	public Set<CredencialCodigoBarra> getCredenciaisCodigoBarra() {
		Set<CredencialCodigoBarra> credenciais = new HashSet<>();
		for (Credencial credencial : credenciais) {
			if (credencial instanceof CredencialCodigoBarra) {
				credenciais.add((CredencialCodigoBarra) credencial);
			}
		}
		return credenciais;
	}

	public CredencialUsuarioSenha getCredencialUsuarioSenha() {
		for (Credencial credencial : credenciais) {
			if (credencial instanceof CredencialUsuarioSenha) {
				return (CredencialUsuarioSenha) credencial;
			}
		}
		return null;
	}

	public CredencialCodigoBarra getCredencialCodigoBarra() {
		for (Credencial credencial : credenciais) {
			if (credencial instanceof CredencialCodigoBarra) {
				return (CredencialCodigoBarra) credencial;
			}
		}
		return null;
	}
}