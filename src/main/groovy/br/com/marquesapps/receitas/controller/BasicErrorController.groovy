package br.com.marquesapps.receitas.controller;

import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ErrorController
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
public class BasicErrorController implements ErrorController{

    private static final String PATH = "/error";
	
	@Autowired
	private MessageSource messageSource
	
    @RequestMapping("/error")
    public ModelAndView error(HttpServletResponse response,
		                      Model model) {		
		 def mensagemerro
		 
		 switch (response.getStatus()) {
			
			case '403':
			 	mensagemerro=messageSource.getMessage("erro403", null , LocaleContextHolder.getLocale())
				break
			case '404':
			 	mensagemerro=messageSource.getMessage("erro404", null , LocaleContextHolder.getLocale())
				break
			case '500':
				mensagemerro=messageSource.getMessage("erro500", null , LocaleContextHolder.getLocale())
				break
			default:
				mensagemerro 		= messageSource.getMessage("errodefault", null , LocaleContextHolder.getLocale())
				break
		 }
		
		 model.addAttribute("codigoerro",response.getStatus());	 
	     model.addAttribute("mensagemerro",mensagemerro);	     	
		 new ModelAndView("views/error/error")
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}