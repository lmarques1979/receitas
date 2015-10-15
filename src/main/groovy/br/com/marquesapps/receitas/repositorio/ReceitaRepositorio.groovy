package br.com.marquesapps.receitas.repositorio;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

import br.com.marquesapps.receitas.domain.Receita
import br.com.marquesapps.receitas.domain.TipoReceita
import br.com.marquesapps.receitas.security.domain.Usuario

public interface ReceitaRepositorio extends PagingAndSortingRepository<Receita, Long> {
	
	Page<Receita> findAll(Pageable pageable);
	Page<Receita> findByTiporeceitaAndUsuario(TipoReceita tiporeceita, Usuario usuario,Pageable pageable);
	@Query(value="SELECT r FROM Receita r WHERE LOWER(r.descricao) LIKE LOWER(CONCAT('%', :descricao , '%')) and r.usuario=:usuario")
	Page<Receita> findByDescricaoAndUsuario(@Param("descricao") String descricao , @Param("usuario") Usuario usuario, Pageable pageable);
}