package com.bolsadeideas.springbootbackendapirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//Configracion por el lado de oauth
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	/*
	 * al inyectar UserDetailsService buscará una implimentacion concreta de este
	 * tipo, como solo tenemos una, entonces usará UsuarioService
	 */
	@Autowired
	private UserDetailsService usuarioService;

	/*
	 * BCryptPasswordEncoder. Codificador por default en spring.
	 * @Bean. el objeto que retorna este metodo, lo registra en el contenedor de
	 * spring
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * registrar en el authenticationManager de sprigSecurity este servicio para
	 * autenticar.
	 */
	@Autowired
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.anyRequest().authenticated()// cualquier otra url(endpoint) debe estar authenticado el usuario
		.and()
		.csrf().disable()//deshabilitamos csrf
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//deshabilitar el manejo de sessiones ya que trabajaremos con tokens. se manejan sessiones cuando trabajamos en un mismo proeycto el back y front a la vez
		
	}

}
