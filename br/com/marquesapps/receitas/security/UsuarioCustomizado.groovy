package br.com.marquesapps.receitas.security

import br.com.marquesapps.receitas.domain.Usuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

public class UsuarioCustomizado implements UserDetails{ 
	
	private static final long serialVersionUID = 1L;
	private Usuario usuario
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
	
	public String getNome() {
		
		if (usuario.getSobrenome().isEmpty()){
			nome = usuario.getPrimeironome()
		}else{
			nome = usuario.getPrimeironome() + ' ' + usuario.getSobrenome()
		}	
		return nome
	}
}
