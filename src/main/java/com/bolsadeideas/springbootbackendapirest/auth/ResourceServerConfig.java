package com.bolsadeideas.springbootbackendapirest.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//oauth
@Configuration
@EnableResourceServer // habilitar el servidor de recursos. por el lado de oauth
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	/*
	 * implementar solo un metodo que nos permita implementar todas las reglas de
	 * seguridad hacia nuestros endpoint
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/clientes").permitAll()//todas los endpoint pulbicos
		.anyRequest().authenticated();//cualquier otra url(endpoint) debe estar authenticado el usuario
	}

}
