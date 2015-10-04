package br.com.marquesapps.receitas.controller;

import java.util.List;
import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.receitas.domain.TipoReceita
import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio;
import br.com.marquesapps.receitas.repositorio.TipoReceitaRepositorio
import br.com.marquesapps.receitas.security.domain.Usuario;
import br.com.marquesapps.receitas.utils.Paginacao;
import br.com.marquesapps.receitas.utils.Util

@Controller
@PreAuthorize('isAuthenticated()') 
class TipoReceitaController {
	
	@Autowired
	private TipoReceitaRepositorio tipoReceitaRepositorio
	
	@Autowired
	private Paginacao paginacao
	
	@Autowired
	private ConfiguracaoRepositorio configuracaoRepositorio
	
	@Autowired 
	private MessageSource messageSource
	
	@RequestMapping(value="/vertiporeceitas",method = RequestMethod.GET)
	def view(Model model, 
			 @PageableDefault(page=0,size=10) Pageable pageable) {
		def orderList = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		paginacao.getPaginacao(tipoReceitaRepositorio, pageable, model, orderList, 2) 
		new ModelAndView("views/tiporeceita/view")
	}
	
	@RequestMapping(value="/showtiporeceita/{id}",method=RequestMethod.GET) 
	def show(Model model ,
		     @PathVariable(value="id") Long id) {
				
		def tiporeceita=tipoReceitaRepositorio.findOne(id)
		model.addAttribute("tiporeceita", tiporeceita);
		new ModelAndView("views/tiporeceita/edit")		
	}
				  
	@RequestMapping(value="/tiporeceita" , method = RequestMethod.GET) 
	def create(Model model) {		
		model.addAttribute("tiporeceita", new TipoReceita());
		new ModelAndView("views/tiporeceita/create")
		
	}
	
	@RequestMapping(value="/deletetiporeceita/{id}",method=RequestMethod.GET)
	def delete(	@PathVariable(value="id") Long id , 
				@PageableDefault(page=0,size=10) Pageable pageable,
				Model model) {		
		tipoReceitaRepositorio.delete(id);	
		def orderList = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		paginacao.getPaginacao(tipoReceitaRepositorio, pageable, model, orderList,2) 
		new ModelAndView("views/tiporeceita/view")
	}
				  
	@RequestMapping(value="/tiporeceita" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("tiporeceita") TipoReceita tiporeceita, 
			 BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
				return "views/tiporeceita/edit" 
		}else{
				
				def tiporeceitadescricao
				def util = new Util()
				def usuario = util.getUsuarioLogado()
				//Valido descricao
				tiporeceitadescricao = tipoReceitaRepositorio.findByUsuarioAndDescricao(usuario, tiporeceita.getDescricao())
				if (tiporeceitadescricao!=null){
					
						if (tiporeceitadescricao.getId()==null){
							bindingResult.rejectValue("descricao","tiporeceitaexiste", messageSource.getMessage("tiporeceitaexiste", null, LocaleContextHolder.getLocale()))
							return "views/tiporeceita/edit"
						}
						
						if (tiporeceitadescricao.id!=tiporeceita.getId()){
							bindingResult.rejectValue("descricao","tiporeceitaexiste", messageSource.getMessage("tiporeceitaexiste", null, LocaleContextHolder.getLocale()))
							return "views/tiporeceita/edit" 
						}
				}
				
				tiporeceita.usuario=usuario
				tiporeceita.descricao = tiporeceita.descricao.toUpperCase()
			    tipoReceitaRepositorio.save(tiporeceita)
		}
		
		new ModelAndView("views/tiporeceita/create" ,
			    [message:messageSource.getMessage("dadosinseridossucesso", null, LocaleContextHolder.getLocale())])
	}
}
