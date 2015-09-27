package br.com.marquesapps.receitas.security.controller;

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.receitas.security.domain.Regra
import br.com.marquesapps.receitas.security.repositorio.RegraRepositorio
import br.com.marquesapps.receitas.security.repositorio.UsuarioRegraRepositorio

@Controller
@PreAuthorize('hasAuthority("ADMIN")')
class RegraController {
	
	@Autowired
	private RegraRepositorio regraRepositorio
	
	@Autowired
	private UsuarioRegraRepositorio usuarioregraRepositorio
	
	@Autowired 
	private MessageSource messageSource
	
	@RequestMapping(value="/verregras",method = RequestMethod.GET)
	def view(Model model , @PageableDefault(page=0,size=10) Pageable pageable) {
		Sort sort = new Sort(Sort.Direction.DESC, "descricao")
		def pageabledefault = new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort)
		
		def regras=regraRepositorio.findAll(pageabledefault)
		model.addAttribute("regra", regras);
		new ModelAndView("views/regra/view")
	}
	
	@RequestMapping(value="/showregra/{id}",method=RequestMethod.GET) 
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
	
	@RequestMapping(value="/deleteregra/{id}",method=RequestMethod.GET)
	def delete(@PathVariable(value="id") Long id , Model model) {		
		regraRepositorio.delete(id);	
		def regras = regraRepositorio.findAll()
		model.addAttribute("regra", regras);
		new ModelAndView("views/regra/view")
	}
				  
	@RequestMapping(value="/regra" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid Regra regra, 
			 BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
				return "views/regra/edit" 
		}else{
				def usuarioregra
			    regra.descricao = regra.descricao.toUpperCase()
				regraRepositorio.save(regra)
		}
		
		new ModelAndView("views/regra/create" ,
			    [message:messageSource.getMessage("dadosinseridossucesso", null, LocaleContextHolder.getLocale())])
	}
}
