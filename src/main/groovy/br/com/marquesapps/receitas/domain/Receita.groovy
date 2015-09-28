package br.com.marquesapps.receitas.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tb_receita")
public class Receita {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "descricao", nullable = false, length=255)
	private String descricao;
	
	@Column(name = "ingredientes", nullable = true, length=255)
	private String ingredientes;
	
	@Column(name = "modopreparo", nullable = true, length=1000)
	private String modopreparo;
	
	@Column(name = "imagem", nullable = true, length=50)
	private String imagem;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "tiporeceita_id")
	private TipoReceita tiporeceita;	

	protected Receita() {}
	
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

	public String getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(String ingredientes) {
		this.ingredientes = ingredientes;
	}

	public String getModopreparo() {
		return modopreparo;
	}

	public void setModopreparo(String modopreparo) {
		this.modopreparo = modopreparo;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public TipoReceita getTiporeceita() {
		return tiporeceita;
	}

	public void setTiporeceita(TipoReceita tiporeceita) {
		this.tiporeceita = tiporeceita;
	}
	
}
