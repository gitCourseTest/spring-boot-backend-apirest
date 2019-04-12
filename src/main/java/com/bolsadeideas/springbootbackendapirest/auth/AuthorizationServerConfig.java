package com.bolsadeideas.springbootbackendapirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	//se puede inyectar porque en la clase SpringSecurityConfig ya se declaró con la anotacion @Bean
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//se puede inyectar con autowired porque ya se declaró con la anotacion @Bean en SpringSecurityConfig.java
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	
	
	/**Configuracion del authorizationServer.
	 * aqui se configuran permisos de nuestros endPoints de spring security oauth2 **/
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")//endpoint de login /oauth/token/   ruta de login para iniciar seccion en nuestro authorization server. aqui se genra el token
		.checkTokenAccess("isAuthenticated()"); //dar permiso al endpoint que se encarga de validar el token. Aqui verifica el token y su firma
	}
	
	/******************** Configuracion de los clientes que accederan a los servicios *************************************/
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("angularapp")
		.secret(passwordEncoder.encode("12345"))
		.scopes("read","write")
		.authorizedGrantTypes("password", "refresh_token")
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(3600);
	}

	/********Configuracion del server********* */
	//se encarga de todo el proceso de authorization y de validar el token
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)//1. registrar el authenticacionManager
		.tokenStore(tokenStore())
		.accessTokenConverter(accessTokenConverter());//almacena los datos de authenticacion del usuario(username, password...)	
	}
	
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(JwtConfig.LLAVE_SECRETA);//Asignar una clave secreta(con la que se genera el token), sino se asigna se genera uno en automatico
		return jwtAccessTokenConverter;
	}
	
	

}
