package br.com.marquesapps.receitas.utils;

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

import br.com.marquesapps.receitas.security.UsuarioCustomizado

class Util {	
	
	def getUsuarioLogado(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UsuarioCustomizado userDetail = (UsuarioCustomizado) auth.getPrincipal();
		def usuario = userDetail.getUsuario()
		
		return usuario
	}	
} 
