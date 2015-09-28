package br.com.marquesapps.receitas.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.marquesapps.receitas.security.domain.Usuario;

public class UsuarioCustomizado implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	private Usuario usuario
	private String nome
	
	Set<GrantedAuthority> authorities=null
	
	public Usuario getUsuario() {
		return usuario
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities)
	{
		this.authorities=authorities;
	}
	
	public boolean isAccountNonExpired() {
		return true
	}

	public boolean isAccountNonLocked() {
		return true
	}

	public boolean isCredentialsNonExpired() {
		return true
	}

	public boolean isEnabled() {
		return true
	}

	public String getUsername() {
		return usuario.getUsername()
	}

	public String getPassword() {
		return usuario.getPassword()
	}
	
	public String getNome(){
		return this.nome
	}
	
	public String setNome(){
		if (usuario.getSobrenome().isEmpty()){
			this.nome = usuario.getPrimeironome()
		}else{
			this.nome = usuario.getPrimeironome() + ' ' + usuario.getSobrenome()
		}
	}
}