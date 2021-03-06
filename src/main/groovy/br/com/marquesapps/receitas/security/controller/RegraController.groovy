package br.com.marquesapps.receitas.security.controller;

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
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

import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio
import br.com.marquesapps.receitas.security.domain.Regra
import br.com.marquesapps.receitas.security.repositorio.RegraRepositorio
import br.com.marquesapps.receitas.utils.Configuracoes
import br.com.marquesapps.receitas.utils.Paginacao

@RequestMapping('/regra')
@Controller
@PreAuthorize('hasAuthority("ADMIN")')
class RegraController {
	
	@Autowired
	private Configuracoes configuracoes
	
	@Autowired
	private RegraRepositorio regraRepositorio
	
	@Autowired
	private Paginacao paginacao
	
	@Autowired
	private ConfiguracaoRepositorio configuracaoRepositorio
	
	@Autowired 
	private MessageSource messageSource
	
	@RequestMapping(value="/view",method = RequestMethod.GET)
	def view(Model model, 
			 @PageableDefault(page=0,size=10) Pageable pageable) {
		def configuracao=configuracoes.getConfiguracoesUsuario()
		model.addAttribute("configuracao",configuracao);
		def orderList = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		paginacao.getPaginacao(regraRepositorio, pageable, model,orderList,2 , null)
		new ModelAndView("views/regra/view")
	}
	
	@RequestMapping(value="/show/{id}",method=RequestMethod.GET) 
	def show(Model model ,
		     @PathVariable(value="id") Long id) {
		def regra=regraRepositorio.findOne(id)
		model.addAttribute("regra", regra);
		new ModelAndView("views/regra/edit")		
	}
				  
	@RequestMapping(value="/regra" , method = RequestMethod.GET) 
	def create(Model model) {		
		model.addAttribute("regra", new Regra());
		new ModelAndView("views/regra/create")
		
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	def delete(@PathVariable(value="id") Long id) {		
		regraRepositorio.delete(id);
		return "redirect:/regra/view";
	}
				  
	@RequestMapping(value="/regra" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("regra") Regra regra, 
			 BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
				return "views/regra/edit" 
		}else{
				
				def regradescricao
				//Valido descricao
				regradescricao = regraRepositorio.findByDescricao(regra.getDescricao())
				if (regradescricao!=null){
					
						if (regradescricao.getId()==null){
							bindingResult.rejectValue("descricao","regraexiste", messageSource.getMessage("regraexiste", null, LocaleContextHolder.getLocale()))
							return "views/regra/edit"
						}
						
						if (regradescricao.id!=regra.getId()){
							bindingResult.rejectValue("descricao","regraexiste", messageSource.getMessage("regraexiste", null, LocaleContextHolder.getLocale()))
							return "views/regra/edit"
						}
				}
			    regra.descricao = regra.descricao.toUpperCase()
				regraRepositorio.save(regra)
		}
		
		new ModelAndView("views/regra/create" ,
			    [message:messageSource.getMessage("dadosinseridossucesso", null, LocaleContextHolder.getLocale())])
	}
}
