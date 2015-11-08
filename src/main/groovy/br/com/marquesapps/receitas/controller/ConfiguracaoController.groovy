package br.com.marquesapps.receitas.controller;

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
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

import br.com.marquesapps.receitas.domain.Configuracao
import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio
import br.com.marquesapps.receitas.utils.Util

@RequestMapping('/configuracao')
@Controller
@PreAuthorize('isAuthenticated()') 
class ConfiguracaoController {
	
	@Autowired
	private ConfiguracaoRepositorio configuracaoRepositorio
	
	@Autowired 
	private MessageSource messageSource
	
	@RequestMapping(value="/view",method = RequestMethod.GET)
	def view(Model model) {
		def util = new Util()
		def usuario = util.getUsuarioLogado()
		def configuracao=configuracaoRepositorio.findByUsuario(usuario)
		model.addAttribute("configuracao", configuracao);
		new ModelAndView("views/configuracao/view")
	}
	
	@RequestMapping(value="/show/{id}",method=RequestMethod.GET) 
	def show(Model model ,
		     @PathVariable(value="id") Long id) {				
		def configuracao=configuracaoRepositorio.findOne(id)
		model.addAttribute("configuracao", configuracao);
		new ModelAndView("views/configuracao/edit")		
	}
				  
	@RequestMapping(value="/configuracao" , method = RequestMethod.GET) 
	def create(Model model) {		
		model.addAttribute("configuracao", new Configuracao());
		new ModelAndView("views/configuracao/create")
		
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	def delete(@PathVariable(value="id") Long id , Model model) {		
		configuracaoRepositorio.delete(id);	
		return "redirect:/configuracao/view";
	}
				  
	@RequestMapping(value="/configuracao" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("configuracao") Configuracao configuracao, 
			 BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
				return "views/configuracao/edit" 
		}else{
				
				def configuracaousuario
				def util = new Util()
				def usuario = util.getUsuarioLogado()
				//Valido usuario
				configuracaousuario = configuracaoRepositorio.findByUsuario(usuario)
				if (configuracaousuario!=null){
					
						if (configuracaousuario.getId()==null){
							bindingResult.rejectValue("itensporpagina","configuracaousuarioexiste", messageSource.getMessage("configuracaousuarioexiste", null, LocaleContextHolder.getLocale()))
							return "views/configuracao/edit"
						}
						
						if (configuracaousuario.id!=configuracao.getId()){
							bindingResult.rejectValue("itensporpagina","configuracaousuarioexiste", messageSource.getMessage("configuracaousuarioexiste", null, LocaleContextHolder.getLocale()))
							return "views/configuracao/edit" 
						}
				}
				configuracao.usuario = usuario
				configuracaoRepositorio.save(configuracao)
		}
		
		new ModelAndView("views/configuracao/create" ,
			    [message:messageSource.getMessage("dadosinseridossucesso", null, LocaleContextHolder.getLocale())])
	}
}
