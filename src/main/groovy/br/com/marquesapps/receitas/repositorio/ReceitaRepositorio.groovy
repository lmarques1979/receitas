package br.com.marquesapps.receitas.repositorio;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

import br.com.marquesapps.receitas.domain.Receita
import br.com.marquesapps.receitas.domain.TipoReceita
import br.com.marquesapps.receitas.security.domain.Usuario

public interface ReceitaRepositorio extends PagingAndSortingRepository<Receita, Long> {
	
	Page<Receita> findAll(Pageable pageable);
	Page<Receita> findByTiporeceitaAndUsuario(TipoReceita tiporeceita, Usuario usuario, Pageable pageable);
	Receita findByDescricao(String descricao); 
}