package br.com.marquesapps.receitas.repositorio;

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

import br.com.marquesapps.receitas.domain.Receita;
import br.com.marquesapps.receitas.domain.TipoReceita
import br.com.marquesapps.receitas.security.domain.Usuario

public interface TipoReceitaRepositorio extends PagingAndSortingRepository<TipoReceita, Long> {
	
	Page<TipoReceita> findAll(Pageable pageable);	
	Iterable<TipoReceita> findAll(Sort sort);
	TipoReceita findByDescricao(String descricao);	
	/*@Query(value="SELECT count(*) , tr.id , tr.imagem, tr.descricao from TipoReceita tr, Receita r, Usuario u where u.id = r.usuario.id and tr.id = r.tiporeceita.id and u = :usuario group by tr.id, tr.imagem, tr.descricao union all SELECT 0 , tr.id , tr.imagem, tr.descricao from TipoReceita tr where tr.id not in (select r.tiporeceita.id from Receita r , Usuario u where  r.tiporeceita.id = tr.id and r.usuario.id = u.id and u.id=:usuario) order by 4")*/
	@Query(value="SELECT count(*) , tr.id , tr.imagem, tr.descricao from TipoReceita tr, Receita r, Usuario u where u.id = r.usuario.id and tr.id = r.tiporeceita.id and u = :usuario group by tr.id, tr.imagem, tr.descricao order by 4")
	Page<TipoReceita> findByUsuario(@Param("usuario") Usuario usuario, Pageable pageable);
	
} 