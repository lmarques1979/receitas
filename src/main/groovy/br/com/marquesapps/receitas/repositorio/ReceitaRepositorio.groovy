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
	Page<Receita> findByPublicoAndAutorizada(boolean publico, boolean autorizada, Pageable pageable);
	
	@Query(value="SELECT r FROM Receita r WHERE LOWER(r.descricao) LIKE LOWER(CONCAT('%', :descricao , '%')) and r.usuario=:usuario order by r.descricao asc")
	Page<Receita> findByDescricaoAndUsuario(@Param("descricao") String descricao , @Param("usuario") Usuario usuario, Pageable pageable);
	
	@Query(value="SELECT r FROM Receita r WHERE r.publico=1 and r.autorizada=1 and LOWER(r.descricao) LIKE LOWER(CONCAT('%', :descricao , '%')) order by r.descricao asc")
	Page<Receita> findByPublicoAndAutorizada(@Param("descricao") String descricao, Pageable pageable);
	
	@Query(value="SELECT r FROM Receita r WHERE r.publico=1 and r.autorizada=1 and r.tiporeceita.id=:tiporeceitaid order by r.descricao asc")
	Page<Receita> findByPublicoAndAutorizadaAndTipo(@Param("tiporeceitaid") Long tiporeceitaid, Pageable pageable);
}