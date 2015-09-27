package br.com.marquesapps.receitas;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver

@SpringBootApplication
@ComponentScan(["br.com.marquesapps.receitas","br.com.marquesapps.utils", "br.com.marquesapps.receitas.security"])
class Main extends WebMvcConfigurerAdapter{
	
	@Autowired
	private MessageSource messageSource
	
	static void main(String[] args) {
		SpringApplication.run Main, args
	}
	 
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("pt", "BR"));
		return slr;
	}
 
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	/**********************************************************************
	*Setar os campos de validação para ler dos arquivos messages.properties
	**********************************************************************/
	public LocalValidatorFactoryBean getValidator(){
		LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean()
		factory.setValidationMessageSource(messageSource)
		return factory
	}
	
}