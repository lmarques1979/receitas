package br.com.marquesapps.receitas.security.domain;

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import org.hibernate.validator.constraints.Email

import br.com.marquesapps.receitas.domain.Configuracao
import br.com.marquesapps.receitas.domain.Receita

@Entity
@Table(name="tb_usuario")
public class Usuario{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "primeironome", nullable = false)
	@NotNull
	@Size(min=2, max=50)
	private String primeironome;
	
	@Column(name = "sobrenome", nullable = true,length=30)
	private String sobrenome;
	
	@Column(name = "username", nullable = false, unique = true)
	@NotNull
	@Size(min=5, max=20)
	private String username;
	
	@Column(name = "password", nullable = false , length=255)
	@NotNull
	private String password;
	
	@Column(name = "email",nullable=false,unique = true)
	@NotNull
	@Size(min=5, max=70)
	@Email(message="{emailinvalido}")
	private String email;
	
	@Column(name = "imagem", nullable = true, length=50)
	private String imagem;
	
	@Column(name="ativo", nullable=false)
	private boolean ativo;
	
	@OneToMany(mappedBy="usuario",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private Set<UsuarioRegra> regras;
	
	@OneToMany(mappedBy="usuario",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private Set<Receita> receitas; 
	
	@OneToOne(mappedBy="usuario",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private Configuracao configuracao;
	
	public Long getId() {
		return id;
	}

	public String getNome(){
		def nome
		if (getSobrenome().isEmpty()){
			nome = getPrimeironome()
		}else{
			nome = getPrimeironome() + ' ' + getSobrenome()
		}
		return nome
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getPrimeironome() {
		return primeironome;
	}
	
	public void setPrimeironome(String primeironome) {
		this.primeironome = primeironome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	
	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Set<UsuarioRegra> getRegras() {
		return regras;
	}

	public void setRegras(Set<UsuarioRegra> regras) {
		this.regras = regras;
	}

	public Set<Receita> getReceitas() {
		return receitas;
	}

	public void setReceitas(Set<Receita> receitas) {
		this.receitas = receitas;
	}
}