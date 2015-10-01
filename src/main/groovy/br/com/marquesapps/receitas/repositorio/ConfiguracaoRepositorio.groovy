package br.com.marquesapps.receitas.repositorio;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import br.com.marquesapps.receitas.domain.Configuracao
import br.com.marquesapps.receitas.security.domain.Usuario

public interface ConfiguracaoRepositorio extends CrudRepository<Configuracao, Long> {
	
	Configuracao findByUsuario(Usuario usuario);
	
}