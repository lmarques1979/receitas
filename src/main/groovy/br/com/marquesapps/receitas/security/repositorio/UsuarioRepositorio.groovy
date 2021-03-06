package br.com.marquesapps.receitas.security.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.marquesapps.receitas.security.domain.Usuario;

public interface UsuarioRepositorio extends PagingAndSortingRepository<Usuario, Long> {
	
	Page<Usuario> findAll(Pageable pageable);
	List<Usuario> findByAtivoTrue();
	Usuario findByUsernameAndId(String username, Long id);
	Usuario findByEmailAndId(String email, Long id);
	Usuario findByUsername(String username);
	Usuario findByEmail(String email);
}