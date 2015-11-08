package br.com.marquesapps.receitas.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.marquesapps.receitas.security.UsuarioCustomizado
import br.com.marquesapps.receitas.security.repositorio.UsuarioRepositorio;

import org.springframework.stereotype.Component;
import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio;

@Component
class Configuracoes {	
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio
	
	@Autowired
	private ConfiguracaoRepositorio configuracaoRepositorio
	
	def getConfiguracoesUsuario(){
		
		def usuario
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		def principal=auth.getPrincipal()
		
        if (principal!='anonymousUser'){
			UsuarioCustomizado userDetail = (UsuarioCustomizado) auth.getPrincipal();
			usuario = userDetail.getUsuario()
        }else{
			usuario = usuarioRepositorio.findByUsername("admin")
        }
		def configuracao = configuracaoRepositorio.findByUsuario(usuario);		
		return configuracao
	}	
} 
