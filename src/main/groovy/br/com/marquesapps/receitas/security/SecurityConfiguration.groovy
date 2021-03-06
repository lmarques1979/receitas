package br.com.marquesapps.receitas.security;

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
	private static PasswordEncoder encoder
	
	@Autowired
	private UserDetailsService customUserDetailsService
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	   
		http.authorizeRequests().antMatchers("/usuario/login").permitAll();
		http.authorizeRequests().antMatchers("/resources/**").permitAll();
		http.formLogin().loginPage("/usuario/login")
				.usernameParameter("username")
				.passwordParameter("password").permitAll()
				.defaultSuccessUrl("/")
				.and().logout().invalidateHttpSession(true)
				.logoutUrl("/usuario/logout").logoutSuccessUrl("/usuario/login").permitAll();
		http.exceptionHandling().accessDeniedPage("/error/acessonegado");

	}
 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(customUserDetailsService)
			.passwordEncoder(passwordEncoder());
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		if(encoder == null) {
			encoder = new BCryptPasswordEncoder();
		}

		return encoder;
	}
}