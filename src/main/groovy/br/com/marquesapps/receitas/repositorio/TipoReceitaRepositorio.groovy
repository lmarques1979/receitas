package br.com.marquesapps.receitas.repositorio;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

import br.com.marquesapps.receitas.domain.TipoReceita
import br.com.marquesapps.receitas.security.domain.Usuario

public interface TipoReceitaRepositorio extends PagingAndSortingRepository<TipoReceita, Long> {
	
	Page<TipoReceita> findAll(Pageable pageable);	
	Iterable<TipoReceita> findAll(Sort sort);
	TipoReceita findByDescricao(String descricao);	
} 