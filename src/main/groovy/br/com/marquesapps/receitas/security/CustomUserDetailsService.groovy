package br.com.marquesapps.receitas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.marquesapps.receitas.security.domain.Usuario
import br.com.marquesapps.receitas.security.domain.UsuarioRegra
import br.com.marquesapps.receitas.security.repositorio.UsuarioRepositorio
 
@Service
@Qualifier("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Transactional(readOnly=true)
	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {

		Usuario usuario = usuarioRepositorio.findByUsername(username);
		def regras = usuario.getRegras();
		
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for(UsuarioRegra usuarioregra: regras){
			authorities.add(new SimpleGrantedAuthority(usuarioregra.getRegra().getDescricao()));
		}
		
		UsuarioCustomizado usuariocustomizado=new UsuarioCustomizado()
        usuariocustomizado.setUsuario(usuario)
        usuariocustomizado.setAuthorities(authorities)
		usuariocustomizado.setNome()
		return usuariocustomizado

	}
			
}