package br.com.marquesapps.receitas.repositorio;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.receitas.domain.TipoReceita
import br.com.marquesapps.receitas.security.domain.Usuario

public interface TipoReceitaRepositorio extends PagingAndSortingRepository<TipoReceita, Long> {
	
	Page<TipoReceita> findAll(Pageable pageable);
	Page<TipoReceita> findByUsuario(Usuario usuario, Pageable pageable);
	List<TipoReceita> findByUsuario(Usuario usuario, Sort sort);
	TipoReceita findByUsuarioAndDescricao(Usuario usuario, String descricao);    
} 