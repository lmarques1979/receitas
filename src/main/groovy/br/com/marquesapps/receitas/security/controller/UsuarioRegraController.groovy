package br.com.marquesapps.receitas.security.controller;

import javax.servlet.http.HttpSession
import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.receitas.security.domain.Regra
import br.com.marquesapps.receitas.security.domain.Usuario
import br.com.marquesapps.receitas.security.domain.UsuarioRegra
import br.com.marquesapps.receitas.security.repositorio.RegraRepositorio
import br.com.marquesapps.receitas.security.repositorio.UsuarioRegraRepositorio
import br.com.marquesapps.receitas.security.repositorio.UsuarioRepositorio

@Controller 
@PreAuthorize('hasAuthority("ADMIN")')
class UsuarioRegraController {
	
	@Autowired
	private UsuarioRegraRepositorio usuarioregraRepositorio
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio
	
	@Autowired
	private RegraRepositorio regraRepositorio
	
	@Autowired
	private MessageSource messageSource
	
	@RequestMapping(value="/verusuarioregras",method = RequestMethod.GET)
	def view(Model model) {
		def usuarioregras = usuarioregraRepositorio.findAll()
		model.addAttribute("usuarioregra", usuarioregras);
		new ModelAndView("views/usuarioregra/view")
		
	}
	
	@RequestMapping(value="/showusuarioregra/{id}",method = RequestMethod.GET)
	def show(Model model , @PathVariable(value="id") Long id) {
		
		def usuarios = usuarioRepositorio.findByAtivoTrue();
		def regras = regraRepositorio.findByAtivoTrue();
		def usuarioregra=usuarioregraRepositorio.findOne(id);
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("regras", regras);
		model.addAttribute("usuarioregra", usuarioregra);
		new ModelAndView("views/usuarioregra/edit")		
	}
				  
	@RequestMapping(value="/usuarioregra" , method = RequestMethod.GET) 
	def create(Model model) {		
		model.addAttribute("usuarioregra", new UsuarioRegra());
		model.addAttribute("usuarios", usuarioRepositorio.findByAtivoTrue());
		model.addAttribute("regras", regraRepositorio.findByAtivoTrue());		
		return "views/usuarioregra/create"
	}
	
	@RequestMapping(value="/deleteusuarioregra/{id}",method = RequestMethod.GET)
	def delete(Model model , @PathVariable(value="id") Long id) {
		usuarioregraRepositorio.delete(id);
	    def usuarioregras = usuarioregraRepositorio.findAll()
		model.addAttribute("usuarioregra", usuarioregras);
		new ModelAndView("views/usuarioregra/view")
	}
				  
	@RequestMapping(value="/usuarioregra" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid UsuarioRegra usuarioregra,
			 BindingResult bindingResult,
			 Model model) {
		
		if (bindingResult.hasErrors()) {
				return "views/usuarioregra/edit"  
		}else{
				
				def ret
				ret = usuarioregraRepositorio.findByUsuarioAndRegra(usuarioregra.usuario , usuarioregra.regra)
				if (ret!=null){
					def errors =  messageSource.getMessage("usuarioregraexiste", null, LocaleContextHolder.getLocale())
					model.addAttribute("usuarios", usuarioRepositorio.findByAtivoTrue());
					model.addAttribute("regras", regraRepositorio.findByAtivoTrue());
					model.addAttribute("errors", errors);
					return "views/usuarioregra/create"
				}
				usuarioregraRepositorio.save(usuarioregra)			
		}
		
		return "redirect:verusuarioregras";
	}
}
