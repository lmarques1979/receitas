package br.com.marquesapps.receitas.repositorio;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.receitas.domain.TipoReceita

public interface TipoReceitaRepositorio extends PagingAndSortingRepository<TipoReceita, Long> {
	
	Page<TipoReceita> findAll(Pageable pageable);
	TipoReceita findByDescricao(String descricao);
	TipoReceita findById(Long id); 
	
}