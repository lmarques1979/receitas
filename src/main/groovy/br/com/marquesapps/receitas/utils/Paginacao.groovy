package br.com.marquesapps.receitas.utils;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.ui.Model
import br.com.marquesapps.receitas.repositorio.ReceitaRepositorio
import br.com.marquesapps.receitas.repositorio.TipoReceitaRepositorio
import org.springframework.data.repository.query.Param

@Component
public class Paginacao{
	
	@Autowired
	private Util util
	
	def getPaginacao(def repositorio, Pageable pageable , Model model, Sort orderList , int tipo, String busca){
		
		def itensporpagina=1
		def ordenacao
		def pageimpl , pageimpl2
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
			if (busca){
				if (!busca.isEmpty()){
					if(busca=="todos"){
						pageimpl=repositorio.findAll(pagerequest)
					}
				}
			}
		}else{
			if (repositorio in ReceitaRepositorio){
				
				if (busca){
					if (!busca.isEmpty()){
						if(busca=="usuariodescricao"){
							def descricao = model.getAt("descricao")
							pageimpl=repositorio.findByDescricaoAndUsuario(descricao, util.getUsuarioLogado(), pagerequest)
						}
						if(busca=="naoautorizada"){
							pageimpl=repositorio.findByPublicoAndAutorizada(true, false , pagerequest)
						}
						if(busca=="publicas"){
							def descricao = model.getAt("descricao")
							pageimpl=repositorio.findByPublicoAndAutorizada(descricao , pagerequest)
						}						
						if(busca=="buscatipopublica"){
							def tiporeceitaid = model.getAt("tiporeceitaid")
							pageimpl=repositorio.findByPublicoAndAutorizadaAndTipo(tiporeceitaid , pagerequest)
						}
						if(busca=="usuario"){
							pageimpl=repositorio.findByUsuario(util.getUsuarioLogado() , pagerequest)
						}
					}
				}else{
					def tiporeceita = model.getAt("tiporeceita")
					pageimpl=repositorio.findByTiporeceitaAndUsuario(tiporeceita, util.getUsuarioLogado(), pagerequest)
				}
			}else{
				pageimpl=repositorio.findAll(pagerequest)
			}
		}
			
		def i , pages=[]
		for (i=0; i < pageimpl.getTotalPages(); i++) {pages.add(i)}
		model.addAttribute("pages", pages);
		model.addAttribute("pageimpl", pageimpl);
		model.addAttribute("total", pageimpl.getTotalElements());
		
	}
}