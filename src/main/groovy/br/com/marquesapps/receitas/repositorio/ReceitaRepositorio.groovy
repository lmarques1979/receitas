package br.com.marquesapps.receitas.repositorio;

import br.com.marquesapps.receitas.domain.Receita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReceitaRepositorio extends PagingAndSortingRepository<Receita, Long> {
	
	Page<Receita> findAll(Pageable pageable);
	Receita findByDescricao(String descricao); 
}