package br.com.marquesapps.receitas.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.marquesapps.receitas.security.UsuarioCustomizado;
import org.springframework.stereotype.Component;
import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio;

@Component
class Configuracoes {	
	
	@Autowired
	private ConfiguracaoRepositorio configuracaoRepositorio
	
	def getConfiguracoesUsuario(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioCustomizado userDetail = (UsuarioCustomizado) auth.getPrincipal();
		def usuario = userDetail.getUsuario()
		def configuracao = configuracaoRepositorio.findByUsuario(usuario);
		
		return configuracao
	}	
} 
