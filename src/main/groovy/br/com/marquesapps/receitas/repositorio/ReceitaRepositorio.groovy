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
	
	List<Receita> findByUsuario(Usuario usuario);
	
	Receita       findOneByDescricaoAndUsuario(String descricao, Usuario usuario);
	
	Page<Receita> findByTiporeceitaAndUsuario(TipoReceita tiporeceita, Usuario usuario,Pageable pageable);
	
	List<Receita> findByTiporeceitaAndUsuario(TipoReceita tiporeceita, Usuario usuario)
	
	Long 		  countByTiporeceitaAndUsuario(TipoReceita tiporeceita, Usuario usuario)
	
	@Query(value="SELECT count(*) , tr.descricao , u.id from TipoReceita tr, Receita r, Usuario u where u.id = r.usuario.id and tr.id = r.tiporeceita.id and u = :usuario group by tr.descricao , u.id order by tr.descricao")
	Page<Receita> findByUsuario(@Param("usuario") Usuario usuario, Pageable pageable);
	
	@Query(value="SELECT r FROM Receita r WHERE LOWER(r.descricao) LIKE LOWER(CONCAT('%', :descricao , '%')) and r.usuario=:usuario order by r.descricao asc")
	Page<Receita> findByDescricaoAndUsuario(@Param("descricao") String descricao , @Param("usuario") Usuario usuario, Pageable pageable);
	
	Page<Receita> findByPublicoAndAutorizada(boolean publico, boolean autorizada, Pageable pageable);
}