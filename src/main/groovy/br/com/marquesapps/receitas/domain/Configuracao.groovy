package br.com.marquesapps.receitas.domain;

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

import br.com.marquesapps.receitas.security.domain.Usuario

@Entity
@Table(name="tb_configuracao")
public class Configuracao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "itensporpagina", nullable = true)
	private int itensporpagina;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="usuario_id")
	private Usuario usuario;

	protected Configuracao() {}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getItensporpagina() {
		return itensporpagina;
	}

	public void setItensporpagina(int itensporpagina) {
		this.itensporpagina = itensporpagina;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
