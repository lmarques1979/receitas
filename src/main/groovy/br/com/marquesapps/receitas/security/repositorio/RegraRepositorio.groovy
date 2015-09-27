package br.com.marquesapps.receitas.security.repositorio;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.receitas.security.domain.Regra

public interface RegraRepositorio extends PagingAndSortingRepository<Regra, Long> {
	
	Page<Regra> findAll(Pageable pageable);
	List<Regra> findByAtivoTrue();
	Regra findByDescricao(String descricao);
	Regra findById(Long id); 
	
}