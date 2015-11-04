package br.com.marquesapps.receitas.controller;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView

import br.com.marquesapps.receitas.domain.Receita
import br.com.marquesapps.receitas.domain.TipoReceita;
import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio
import br.com.marquesapps.receitas.repositorio.ReceitaRepositorio
import br.com.marquesapps.receitas.repositorio.TipoReceitaRepositorio
import br.com.marquesapps.receitas.utils.Amazon
import br.com.marquesapps.receitas.utils.Configuracoes
import br.com.marquesapps.receitas.utils.Paginacao
import br.com.marquesapps.receitas.utils.Util

@RequestMapping('/receita')
@Controller
@PreAuthorize('isAuthenticated()') 
class ReceitaController {
	
	@Autowired
	private Configuracoes configuracoes
	
	@Autowired
	private Util util
	
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
	
	@RequestMapping(value="/busca",method = RequestMethod.GET)
	def busca(Model model, 
			  @RequestParam("descricao") String descricao,
			  @PageableDefault(page=0,size=10) Pageable pageable) {
		def configuracao=configuracoes.getConfiguracoesUsuario()
		model.addAttribute("configuracao",configuracao);
		model.addAttribute("descricao",descricao);
		def filtro = "&descricao=" + descricao
		model.addAttribute("filtro", filtro);
		def orderList = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		paginacao.getPaginacao(receitaRepositorio,pageable, model, orderList, 2, "usuariodescricao") 
		new ModelAndView("views/receita/viewreceitas")
	}
			 
	@RequestMapping(value="/view",method = RequestMethod.GET)
	def view(Model model, 
			 @PageableDefault(page=0,size=10) Pageable pageable) {
		def configuracao=configuracoes.getConfiguracoesUsuario()
		model.addAttribute("configuracao",configuracao);
		model.addAttribute("totalreceitas", receitaRepositorio.findByUsuario(util.getUsuarioLogado()).size());
		def orderList = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		paginacao.getPaginacao(tipoReceitaRepositorio,pageable, model, orderList, 2, null) 
		new ModelAndView("views/receita/view")
	}
	
	@RequestMapping(value="/viewportipo/{id}",method=RequestMethod.GET)
	def viewportipo(@PathVariable(value="id") Long id,
					Model model, 
			 	    @PageableDefault(page=0,size=10) Pageable pageable) {
		def orderList = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		def configuracao=configuracoes.getConfiguracoesUsuario()
		model.addAttribute("configuracao",configuracao);
		model.addAttribute("tiporeceita", tipoReceitaRepositorio.findOne(id));
		paginacao.getPaginacao(receitaRepositorio,pageable, model, orderList, 2 , null) 
		new ModelAndView("views/receita/viewportipo")
	}
					  
	@RequestMapping(value="/show/{id}",method=RequestMethod.GET) 
	def show(Model model ,
		     @PathVariable(value="id") Long id) {
		def receita=receitaRepositorio.findOne(id)
		def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		def tiporeceita = tipoReceitaRepositorio.findByUsuario(util.getUsuarioLogado() , ordertiporeceita)
		model.addAttribute("tiporeceita",tiporeceita);
		model.addAttribute("receita", receita);
		new ModelAndView("views/receita/edit")		
	}
			 
	@RequestMapping(value="/imprimirreceita/{id}",method=RequestMethod.GET)
	def imprimirreceita(Model model ,
			  @PathVariable(value="id") Long id) {
		 def receita=receitaRepositorio.findOne(id)
		 def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		 def tiporeceita = tipoReceitaRepositorio.findByUsuario(util.getUsuarioLogado() , ordertiporeceita)
		 def configuracao=configuracoes.getConfiguracoesUsuario()
		 model.addAttribute("configuracao",configuracao); 
		 model.addAttribute("tiporeceita",tiporeceita);
		 model.addAttribute("receita", receita);
		 new ModelAndView("views/receita/imprimirreceita")
	}
	
	@RequestMapping(value="/imprimiringredientes/{id}",method=RequestMethod.GET)
	def imprimiringredientes(Model model ,
				@PathVariable(value="id") Long id) {
		   def receita=receitaRepositorio.findOne(id)
		   def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		   def tiporeceita = tipoReceitaRepositorio.findByUsuario(util.getUsuarioLogado() , ordertiporeceita)
		   def configuracao=configuracoes.getConfiguracoesUsuario()
		   model.addAttribute("configuracao",configuracao);
		   model.addAttribute("tiporeceita",tiporeceita);
		   model.addAttribute("receita", receita);
		   new ModelAndView("views/receita/imprimiringredientes")
	}
			 
	@RequestMapping(value="/view/{id}",method=RequestMethod.GET)
	def viewreceita(Model model,
	   	     		@PathVariable(value="id") Long id) {
		 def receita=receitaRepositorio.findOne(id)
		 def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		 def tiporeceita = tipoReceitaRepositorio.findByUsuario(util.getUsuarioLogado() , ordertiporeceita)
		 def configuracao=configuracoes.getConfiguracoesUsuario()
		 model.addAttribute("configuracao",configuracao);
		 model.addAttribute("tiporeceita",tiporeceita);
		 model.addAttribute("receita", receita);
		 new ModelAndView("views/receita/visualizar")
	}
				  
	@RequestMapping(value="/receita" , method = RequestMethod.GET) 
	def create(Model model) {	
		model.addAttribute("receita", new Receita());
		def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		def tiporeceita = tipoReceitaRepositorio.findByUsuario(util.getUsuarioLogado() , ordertiporeceita)
		model.addAttribute("tiporeceita",tiporeceita);
		new ModelAndView("views/receita/create")
		
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.GET)
	def delete(	@PathVariable(value="id") Long id , 
				@PageableDefault(page=0,size=10) Pageable pageable,
				Model model) {	
		def receita = receitaRepositorio.findOne(id)
		if (receita.imagem){
			if (!receita.imagem.isEmpty()){
				def delete = amazon.fileDelete (receita.imagem)
			}
		}
		receitaRepositorio.delete(id);	
		def orderList = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		paginacao.getPaginacao(receitaRepositorio, pageable, model, orderList,2, null) 
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
				
				def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
				def tiporeceita = tipoReceitaRepositorio.findByUsuario(util.getUsuarioLogado() , ordertiporeceita)
				model.addAttribute("tiporeceita",tiporeceita);
				def receitadescricao
				//Valido descricao
				receitadescricao = receitaRepositorio.findOneByDescricaoAndUsuario(receita.getDescricao() , util.getUsuarioLogado() )
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
				receita.usuario=util.getUsuarioLogado() 
				receita.descricao = receita.descricao.toUpperCase()
			    receitaRepositorio.save(receita)
		}
		
		def ordertiporeceita = new Sort(new Order(Sort.Direction.ASC, "descricao"))
		model.addAttribute("message", messageSource.getMessage("dadosinseridossucesso", null, LocaleContextHolder.getLocale()));
		new ModelAndView("views/receita/create")
	}
}
