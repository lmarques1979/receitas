package br.com.marquesapps.receitas.services;

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Service

import br.com.marquesapps.receitas.security.domain.Usuario

@Service
class TipoReceitaService {
	
	@PersistenceContext
	EntityManager em
	
	def buscaByUsuario(Usuario usuario) {
		Query q = em.createQuery("select count(nf) from NotaFiscal nf where nf.planoViagem.id = :idPlano and nf.comprovanteEntrega is null and nf.recusa is null")
		q.setParameter("idPlano", idPlano)
		def countResult = q.getSingleResult()		
		return countResult == 0
	}
	
}