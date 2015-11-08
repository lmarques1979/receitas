package br.com.marquesapps.receitas.controller;

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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.receitas.domain.TipoReceita
import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio
import br.com.marquesapps.receitas.repositorio.TipoReceitaRepositorio
import br.com.marquesapps.receitas.utils.Amazon
import br.com.marquesapps.receitas.utils.Configuracoes
import br.com.marquesapps.receitas.utils.Paginacao
import br.com.marquesapps.receitas.utils.Util

@RequestMapping('/tiporeceita')
@Controller
@PreAuthorize('hasAuthority("ADMIN")')
class TipoReceitaController {
	
	@Autowired
	private Configuracoes configuracoes
	
	@Autowired
	private Amazon amazon;
	
	@Autowired
	private Util util
	
	@Autowired
	private TipoReceitaRepositorio tipoReceitaRepositorio
	
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
		paginacao.getPaginacao(tipoReceitaRepositorio, pageable, model, orderList, 2, "todos") 
		new ModelAndView("views/tiporeceita/view")
	}
	
	@RequestMapping(value="/show/{id}",method=RequestMethod.GET) 
	def show(Model model ,
		     @PathVariable(value="id") Long id) {				
		def tiporeceita=tipoReceitaRepositorio.findOne(id);
		model.addAttribute("tiporeceita", tiporeceita);
		new ModelAndView("views/tiporeceita/edit")		
	}
				  
	@RequestMapping(value="/tiporeceita" , method = RequestMethod.GET) 
	def create(Model model) {		
		model.addAttribute("tiporeceita", new TipoReceita());
		new ModelAndView("views/tiporeceita/create")
		
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	def delete(	@PathVariable(value="id") Long id , 
				@PageableDefault(page=0,size=10) Pageable pageable,
				Model model) {		
		def tiporeceita = tipoReceitaRepositorio.findOne(id)
		if (tiporeceita.imagem){
			if (!tiporeceita.imagem.isEmpty()){
				def delete = amazon.fileDelete (tiporeceita.imagem)
			}
		}
		tipoReceitaRepositorio.delete(id);	
		return "redirect:/tiporeceita/view";
	}
				  
	@RequestMapping(value="/tiporeceita" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("tiporeceitas") TipoReceita tiporeceita, 
			 BindingResult bindingResult,
			 @RequestParam("arquivo") MultipartFile f) {
		
		if (bindingResult.hasErrors()) {
				return "views/tiporeceita/edit" 
		}else{
				
				def tiporeceitadescricao
				//Valido descricao 
				tiporeceitadescricao = tipoReceitaRepositorio.findByDescricao(tiporeceita.getDescricao())
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
				
				if (!f.isEmpty()) {
					def midia = amazon.UploadS3(f)
					tiporeceita.imagem = midia
				}
				
				tiporeceita.descricao = tiporeceita.descricao.toUpperCase()
			    tipoReceitaRepositorio.save(tiporeceita)
		}
		
		new ModelAndView("views/tiporeceita/create" ,
			    [message:messageSource.getMessage("dadosinseridossucesso", null, LocaleContextHolder.getLocale())])
	}
}
