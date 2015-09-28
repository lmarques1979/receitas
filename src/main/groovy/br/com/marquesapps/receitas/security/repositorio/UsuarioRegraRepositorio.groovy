package br.com.marquesapps.receitas.security.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.marquesapps.receitas.security.domain.Regra;
import br.com.marquesapps.receitas.security.domain.Usuario;
import br.com.marquesapps.receitas.security.domain.UsuarioRegra;

public interface UsuarioRegraRepositorio extends PagingAndSortingRepository<UsuarioRegra, Long> {
	
	Page<UsuarioRegra> findAll(Pageable pageable);
	UsuarioRegra findByUsuario(Usuario usuario);
	UsuarioRegra findByRegra(Regra regra);
	UsuarioRegra findByUsuarioAndRegra(Usuario usuario , Regra regra);
}