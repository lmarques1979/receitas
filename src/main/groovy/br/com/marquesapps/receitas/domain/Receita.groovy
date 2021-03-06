package br.com.marquesapps.receitas.domain;

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

import br.com.marquesapps.receitas.security.domain.Usuario

@Entity
@Table(name="tb_receita")
public class Receita {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "descricao", nullable = false, length=255)
	private String descricao;
	
	@Column(name = "ingredientes", nullable = true , columnDefinition="TEXT")
	private String ingredientes;
	
	@Column(name = "modopreparo", nullable = true, columnDefinition="TEXT")
	private String modopreparo;
	
	@Column(name = "imagem", nullable = true, length=255) 
	private String imagem;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tiporeceita_id")
	private TipoReceita tiporeceita;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="usuario_id")
	private Usuario usuario;

	@Column(name = "tempopreparo", nullable = true, length=50)
	private String tempopreparo;
	
	@Column(name = "rendimento", nullable = true, length=100)
	private String rendimento;
	
	@Column(name="publico", nullable=false)
	private boolean publico=true;
	
	@Column(name="autorizada", nullable=false)
	private boolean autorizada=false;
	
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTempopreparo() {
		return tempopreparo;
	}

	public void setTempopreparo(String tempopreparo) {
		this.tempopreparo = tempopreparo;
	}

	public String getRendimento() {
		return rendimento;
	}

	public void setRendimento(String rendimento) {
		this.rendimento = rendimento;
	}
	
	public boolean isPublico() {
		return publico;
	}

	public void setPublico(boolean publico) {
		this.publico = publico;
	}

	public boolean isAutorizada() {
		return autorizada;
	}

	public void setAutorizada(boolean autorizada) {
		this.autorizada = autorizada;
	}
}
