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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.receitas.domain.Receita
import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio
import br.com.marquesapps.receitas.repositorio.ReceitaRepositorio
import br.com.marquesapps.receitas.repositorio.TipoReceitaRepositorio
import br.com.marquesapps.receitas.utils.Amazon;
import br.com.marquesapps.receitas.utils.Paginacao
import br.com.marquesapps.receitas.utils.Util

@Controller
@PreAuthorize('isAuthenticated()') 
class ReceitaController {
	
	@Autowired
	private ReceitaRepositorio receitaRepositorio
	
	@Autowired
	private Amazon amazon;
	
	@Autowired
	private Paginacao paginacao
	
	@Autowired
	private ConfiguracaoRepositorio configuracaoRepositorio
	
	@Autowired
	private TipoReceitaRepositorio tipoReceitaRepositorio
	
	@Autowired 
	private MessageSource messageSource
	
	@RequestMapping(value="/verreceitas",method = RequestMethod.GET)
	def view(Model model, 
			 @PageableDefault(page=0,size=10) Pageable pageable) {
		def orderList = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		paginacao.getPaginacao(receitaRepositorio, pageable, model, orderList, 2) 
		new ModelAndView("views/receita/view")
	}
	
	@RequestMapping(value="/showreceita/{id}",method=RequestMethod.GET) 
	def show(Model model ,
		     @PathVariable(value="id") Long id) {
		def receita=receitaRepositorio.findOne(id)
		model.addAttribute("receita", receita);
		def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		model.addAttribute("tiporeceitas", tipoReceitaRepositorio.findAll(ordertiporeceita));
		new ModelAndView("views/receita/edit")		
	}
				  
	@RequestMapping(value="/receita" , method = RequestMethod.GET) 
	def create(Model model) {	
		model.addAttribute("receita", new Receita());
		def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		model.addAttribute("tiporeceitas", tipoReceitaRepositorio.findAll(ordertiporeceita));
		new ModelAndView("views/receita/create")
		
	}
	
	@RequestMapping(value="/deletereceita/{id}",method=RequestMethod.GET)
	def delete(	@PathVariable(value="id") Long id , 
				@PageableDefault(page=0,size=10) Pageable pageable,
				Model model,
				@ModelAttribute("receita") Receita receita) {		
		def delete = amazon.fileDelete (receita.getImagem())
		receitaRepositorio.delete(id);	
		def orderList = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		paginacao.getPaginacao(receitaRepositorio, pageable, model, orderList,2) 
		new ModelAndView("views/receita/view")
	}
				  
	@RequestMapping(value="/receita" , method = RequestMethod.POST)
	@Transactional
	def save(@Valid @ModelAttribute("receita") Receita receita, 
			 BindingResult bindingResult,
			 @RequestParam("arquivo") MultipartFile f,
			 Model model) {
		
		if (bindingResult.hasErrors()) {
				return "views/receita/edit" 
		}else{
				
				def receitadescricao
				//Valido descricao
				receitadescricao = receitaRepositorio.findByDescricao(receita.getDescricao())
				if (receitadescricao!=null){
					
						if (receitadescricao.getId()==null){
							bindingResult.rejectValue("descricao","receitaexiste", messageSource.getMessage("receitaexiste", null, LocaleContextHolder.getLocale()))
							return "views/receita/edit"
						}
						
						if (receitadescricao.id!=receita.getId()){
							bindingResult.rejectValue("descricao","receitaexiste", messageSource.getMessage("receitaexiste", null, LocaleContextHolder.getLocale()))
							return "views/receita/edit" 
						}
				}
				
				if (!f.isEmpty()) {
					def midia = amazon.UploadS3(f)
					receita.imagem = midia
				}
				def util = new Util()
				def usuario = util.getUsuarioLogado()
				receita.usuario=usuario
				receita.descricao = receita.descricao.toUpperCase()
			    receitaRepositorio.save(receita)
		}
		
		def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		model.addAttribute("tiporeceitas", tipoReceitaRepositorio.findAll(ordertiporeceita));
		model.addAttribute("message", messageSource.getMessage("dadosinseridossucesso", null, LocaleContextHolder.getLocale()));
		new ModelAndView("views/receita/create")
	}
}
