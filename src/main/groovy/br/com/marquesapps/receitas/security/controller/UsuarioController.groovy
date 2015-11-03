package br.com.marquesapps.receitas.security.controller;

import javax.mail.MessagingException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.data.web.PageableDefault
import org.springframework.mail.MailSender
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.receitas.domain.Configuracao
import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio
import br.com.marquesapps.receitas.security.domain.Regra
import br.com.marquesapps.receitas.security.domain.Usuario
import br.com.marquesapps.receitas.security.domain.UsuarioRegra
import br.com.marquesapps.receitas.security.repositorio.RegraRepositorio
import br.com.marquesapps.receitas.security.repositorio.UsuarioRegraRepositorio
import br.com.marquesapps.receitas.security.repositorio.UsuarioRepositorio
import br.com.marquesapps.receitas.utils.Amazon
import br.com.marquesapps.receitas.utils.Configuracoes;
import br.com.marquesapps.receitas.utils.Paginacao
import br.com.marquesapps.receitas.utils.SmtpMailSender
import br.com.marquesapps.receitas.utils.Util

@RequestMapping('/usuario')
@Controller
@PreAuthorize('permitAll')
class UsuarioController {
	
	@Autowired
	private Configuracoes configuracoes
	
	@Autowired
	private Util util
	
	@Autowired
	private Amazon amazon;
	
	@Autowired
	private SmtpMailSender smtpMailSender;
	
	@Autowired
	private Paginacao paginacao
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio
	
	@Autowired
	private UsuarioRegraRepositorio usuarioregraRepositorio
	
	@Autowired
	private RegraRepositorio regraRepositorio
	
	@Autowired
	private ConfiguracaoRepositorio configuracaoRepositorio
	
	@Autowired
	private MessageSource messageSource
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@RequestMapping(value="/esqueceusenha",method = RequestMethod.POST)
	def esqueceu(@RequestParam("email") String email) throws MessagingException{
		
		def usuario = usuarioRepositorio.findByEmail(email)
		
		if (usuario==null){
			return new ModelAndView("views/usuario/esqueceusenha",
			[errors:messageSource.getMessage("emailnaocadastrado", null, LocaleContextHolder.getLocale())])
		}else{
			
		    Random novasenha = new Random()
			String senha = Math.abs(novasenha.nextInt())
			usuario.setPassword(new BCryptPasswordEncoder().encode(senha))
			def assunto=messageSource.getMessage("novasenhaassunto", null, LocaleContextHolder.getLocale())
			def msg=messageSource.getMessage("novasenha", null, LocaleContextHolder.getLocale()) + "<b>" + senha + "</b>" 
			def ret=smtpMailSender.send(usuario.getEmail() ,assunto,msg)
			usuarioRepositorio.save(usuario)
		}
		
		def msg=[email]
		Object[] args = msg
		
		new ModelAndView("views/usuario/esqueceusenha",
			[message:messageSource.getMessage("emailenviadocomsucesso", args , LocaleContextHolder.getLocale())])
	}
	
	@RequestMapping(value="/esqueceusenha",method = RequestMethod.GET)
	def senha() {
		new ModelAndView("views/usuario/esqueceusenha")
	}
	
	@RequestMapping(value="/view",method = RequestMethod.GET)
	@PreAuthorize('hasAuthority("ADMIN")')
	def view(Model model, 
			 @PageableDefault(page=0,size=10) Pageable pageable) {
		def configuracao=configuracoes.getConfiguracoesUsuario()
		model.addAttribute("configuracao",configuracao);
		def orderList = new Sort(new Order(Sort.Direction.ASC, "primeironome"))
		paginacao.getPaginacao(usuarioRepositorio, pageable, model, orderList,2 , null)
		new ModelAndView("views/usuario/view")
	}
	
	@RequestMapping(value="/show/{id}",method = RequestMethod.GET)
	@PreAuthorize('isAuthenticated()')
	def show(Model model ,
		     @PathVariable(value = "id") Long id) {
		
	   def usuario=usuarioRepositorio.findOne(id)
	
	   if (usuario==null){
		   return new ModelAndView("views/erros/erro404")
	   }else{
	   
	   			def usuariologado = util.getUsuarioLogado()
				if (usuariologado.id!=id && usuariologado.username!='admin'){
					return new ModelAndView("views/error/acessonegado")
				}
	   }	   
	   	   
	   model.addAttribute("usuario", usuario);
	   new ModelAndView("views/usuario/edit")
	}
				  
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	@PreAuthorize('isAuthenticated()')
	def delete(@PathVariable(value="id") Long id, 
		       @PageableDefault(page=0,size=10) Pageable pageable,
			   Model model,
			   HttpServletRequest request, 
			   HttpServletResponse response) {
		def usuario = usuarioRepositorio.findOne(id)
		if (usuario.imagem){
			if (!usuario.imagem.isEmpty()){
				def delete = amazon.fileDelete (usuario.imagem)
			}
		}
		usuarioRepositorio.delete(id);	  
		def usuariologado = util.getUsuarioLogado()
		if(id==usuariologado.getId()){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    new SecurityContextLogoutHandler().logout(request, response, auth);
			return "redirect:/usuario/login?logout";
		}else{
			def configuracao=configuracoes.getConfiguracoesUsuario()
			model.addAttribute("configuracao",configuracao);
			def orderList = new Sort(new Order(Sort.Direction.ASC, "primeironome"))
			paginacao.getPaginacao(usuarioRepositorio, pageable, model, orderList,2, null)
			new ModelAndView("views/usuario/view")
		}
	}
				  
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	def login(){
		return new ModelAndView("views/usuario/login")
    }
	
	@RequestMapping(value = "/logout" , method = RequestMethod.GET)
	def logout(HttpServletRequest request, 
			   HttpServletResponse response){
			   
	   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	   new SecurityContextLogoutHandler().logout(request, response, auth);
	   return "redirect:/usuario/login?logout";
	}
	
	@RequestMapping(value = "/acessonegado")
	@PreAuthorize('permitAll')
	def acessonegado(){
		return new ModelAndView("views/usuario/acessonegado")
	}
	
	@RequestMapping(value="/usuario" , method = RequestMethod.GET) 
	def create(Model model) {
		model.addAttribute("usuario", new Usuario());
		new ModelAndView("views/usuario/create")
	}
	
	@RequestMapping(value="/usuario" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("usuario") Usuario usuario,  
			 BindingResult bindingResult, 
			 @RequestParam("arquivo") MultipartFile f,
			 @RequestParam("confirmapassword") String confirmapassword ) {
		
		if (bindingResult.hasErrors()) {
				return "views/usuario/edit" 
		}else{
		
				def usuariousername, usuarioemail, usuariousernameid , usuarioemailid
				def regra, usuarioregra
								
				//Valido username
				usuariousername = usuarioRepositorio.findByUsername(usuario.getUsername())	
				if (usuariousername!=null){
					
						if (usuario.getId()==null){
							bindingResult.rejectValue("username","userexists", messageSource.getMessage("usernameexiste", null, LocaleContextHolder.getLocale()))
							return "views/usuario/edit"
						}
						
						if (usuariousername.id!=usuario.getId()){
									bindingResult.rejectValue("username","userexists", messageSource.getMessage("usernameexiste", null, LocaleContextHolder.getLocale()))
									return "views/usuario/edit"
					    }						
				}
				
				//Valido email 
				usuarioemail=usuarioRepositorio.findByEmail(usuario.getEmail())
				if (usuarioemail!=null){	
									
						if (usuario.getId()==null){
							bindingResult.rejectValue("email","emailexists", messageSource.getMessage("emailexiste", null, LocaleContextHolder.getLocale()))
							return "views/usuario/edit"
						}
						
						if (usuarioemail.id!=usuario.getId()){
							bindingResult.rejectValue("email","emailexists", messageSource.getMessage("emailexiste", null, LocaleContextHolder.getLocale()))
							return "views/usuario/edit"
						}
						
				}
				
				if(usuario.password!=confirmapassword){
					bindingResult.rejectValue("password","passworddiferent", messageSource.getMessage("senhanaoconfere", null, LocaleContextHolder.getLocale()))
					return "views/usuario/edit"
				}
				
				if (!f.isEmpty()) {
					def midia = amazon.UploadS3(f)
					usuario.imagem = midia
				}
				usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()))
				usuario.username = usuario.username.toLowerCase()
				usuarioRepositorio.save(usuario)
				
				if (usuario.username=='admin'){
					
					regra = regraRepositorio.findByDescricao('ADMIN')
					if (regra==null){
						regra = new Regra()
						regra.setDescricao('ADMIN')
						regra.setAtivo(true)
						regraRepositorio.save(regra)						
					}
				}else{
					regra = regraRepositorio.findByDescricao('USER')
					if (regra==null){
						regra = new Regra()
						regra.setDescricao('USER')
						regra.setAtivo(true)
						regraRepositorio.save(regra)
					}
				}
				
				usuarioregra = usuarioregraRepositorio.findByUsuarioAndRegra(usuario , regra)
				if (usuarioregra==null){
					usuarioregra = new UsuarioRegra()
					usuarioregra.setRegra(regra)
					usuarioregra.setUsuario(usuario)
					usuarioregraRepositorio.save(usuarioregra)
				}
				
				def configuracoes=configuracaoRepositorio.findByUsuario(usuario)
				if (configuracoes==null){
					def configuracao= new Configuracao() 
					configuracao.usuario=usuario
					configuracao.itensporpagina=10
					configuracao.alturaimg=50
					configuracao.larguraimg=50
					configuracao.alturaimgthumbs=24
					configuracao.larguraimgthumbs=24
					configuracao.itensporpagina=10
					configuracaoRepositorio.save(configuracao)
				}
		}
		
		new ModelAndView("views/usuario/create" ,
			             [message:messageSource.getMessage("dadosinseridossucesso", null, LocaleContextHolder.getLocale())])
	}
}
