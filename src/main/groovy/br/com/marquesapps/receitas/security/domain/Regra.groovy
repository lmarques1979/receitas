package br.com.marquesapps.receitas.security.domain;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="tb_regra")
public class Regra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "descricao", nullable = false)
	@NotNull
	@Size(min=2, max=50)
	private String descricao;
	
	@Column(name="ativo", nullable=false)
	private boolean ativo;
	
	@OneToMany(mappedBy="regra",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<UsuarioRegra> usuarioregras;
	
	public Regra() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Set<UsuarioRegra> getUsuarioregras() {
		return usuarioregras;
	}

	public void setUsuarioregras(Set<UsuarioRegra> usuarioregras) {
		this.usuarioregras = usuarioregras;
	}
}