package br.com.marquesapps.receitas.domain;

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name="tb_tipo_receita")
public class TipoReceita {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "descricao", nullable = false, length=50)
	private String descricao;
	
	@Column(name = "imagem", nullable = true, length=50)
	private String imagem;
	
	@OneToMany(mappedBy="tiporeceita",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private Set<Receita> receitas;
	
	protected TipoReceita() {}
	
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

	public Set<Receita> getReceitas() {
		return receitas;
	}

	public void setReceitas(Set<Receita> receitas) {
		this.receitas = receitas;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
}
