package br.com.marquesapps.receitas.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model
import org.springframework.web.multipart.MultipartFile;

import br.com.marquesapps.receitas.repositorio.ConfiguracaoRepositorio;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class Paginacao{
	
	@Autowired
	private ConfiguracaoRepositorio configuracaoRepositorio
	
	def getPaginacao(def repositorio, Pageable pageable , Model model, Sort orderList , int tipo){
		
		def itensporpagina
		def ordenacao
		
		//Tipo 1 = Pageable , 2 = Tabela configuracoes banco
		//Caso não queria usar os itens por página das configurações para uma determinada página , irá usar
		//O que tiver no objeto pageable (pageable.getPageSize())
		if (tipo==1){
			itensporpagina = pageable.getPageSize()			
		}else{
			def util = new Util()
			def usuario = util.getUsuarioLogado()
			def configuracao = configuracaoRepositorio.findByUsuario(usuario);
			itensporpagina=configuracao.getItensporpagina()
		}
	
		def pagerequest = new PageRequest(pageable.getPageNumber(),itensporpagina, orderList)
		def pageimpl=repositorio.findAll(pagerequest)
		def i , pages=[]
		for (i=0; i < pageimpl.getTotalPages(); i++) {pages.add(i)}
		model.addAttribute("pages", pages);
		model.addAttribute("pageimpl", pageimpl);
	}
}