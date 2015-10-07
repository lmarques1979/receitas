package br.com.marquesapps.receitas.utils;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.ui.Model

import br.com.marquesapps.receitas.repositorio.ReceitaRepositorio
import br.com.marquesapps.receitas.repositorio.TipoReceitaRepositorio

@Component
public class Paginacao{
	
	@Autowired
	private Util util
	
	def getPaginacao(def repositorio, Pageable pageable , Model model, Sort orderList , int tipo){
		
		def itensporpagina=1
		def ordenacao
		def pageimpl
		def configuracao=model.getAt("configuracao")
		//Tipo 1 = Pageable , 2 = Tabela configuracoes banco
		//Caso não queria usar os itens por página das configurações para uma determinada página , irá usar
		//O que tiver no objeto pageable (pageable.getPageSize())
		if (tipo==1){
			itensporpagina = pageable.getPageSize()			
		}else{
			if (configuracao!=null){
				itensporpagina=configuracao.getItensporpagina()
			}
		}
	
		def pagerequest = new PageRequest(pageable.getPageNumber(),itensporpagina, orderList)
		if (repositorio in TipoReceitaRepositorio){
			pageimpl=repositorio.findByUsuario(util.getUsuarioLogado() , pagerequest)
		}else{
			if (repositorio in ReceitaRepositorio){
				def tiporeceita = model.getAt("tiporeceita")						
				pageimpl=repositorio.findByTiporeceitaAndUsuario(tiporeceita, util.getUsuarioLogado(), pagerequest)
			}else{
				pageimpl=repositorio.findAll(pagerequest)
			}
		}
			
		def i , pages=[]
		for (i=0; i < pageimpl.getTotalPages(); i++) {pages.add(i)}
		model.addAttribute("pages", pages);
		model.addAttribute("pageimpl", pageimpl);
	}
}