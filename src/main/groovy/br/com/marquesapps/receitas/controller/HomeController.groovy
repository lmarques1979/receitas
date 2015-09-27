package br.com.marquesapps.receitas.controller;

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@PreAuthorize('permitAll')
class HomeController {	
	
	@RequestMapping("/")
	def home() {
		new ModelAndView(
        "views/home")
	} 
}
