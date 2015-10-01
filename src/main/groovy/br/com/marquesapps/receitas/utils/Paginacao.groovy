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
	
	def getPaginacao(def repositorio, Pageable pageable , Model model, Sort orderList){
		
		def itensporpagina
		def ordenacao
		def util = new Util()
		def usuario = util.getUsuarioLogado()
		def configuracao = configuracaoRepositorio.findByUsuario(usuario);
		if (configuracao!=null){
			itensporpagina=configuracao.getItensporpagina()
		}else{
			itensporpagina = pageable.getPageSize()
		}
		
		def pagerequest = new PageRequest(pageable.getPageNumber(),itensporpagina, orderList)
		def pageimpl=repositorio.findAll(pagerequest)
		def i , pages=[]
		for (i=0; i < pageimpl.getTotalPages(); i++) {pages.add(i)}
		model.addAttribute("pages", pages);
		model.addAttribute("pageimpl", pageimpl);
	}
}