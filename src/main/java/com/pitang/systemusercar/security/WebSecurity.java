package com.pitang.systemusercar.security;

import com.pitang.systemusercar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe onde configura quais rotas a autenticação deve ser usada
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//Desabilitar CSRF (cross site request forgery)
		http.csrf().disable()

		// Nenhuma sessao sera criada ou usada pelo spring security
		//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

		// Rotas que nao exigem autenticacao
		.authorizeRequests().antMatchers("/api/users**").permitAll()
		.antMatchers("/api/users/**").permitAll()
		.antMatchers("/api/signin").permitAll()
		// Proibir o restante...
		.anyRequest().authenticated()
		// Adicionado filter para autenticacao da requisicao
		.and().addFilterBefore(new JwtFilterAuth(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
		//Permitir que o swagger seja acessado sem autenticacao
		web.ignoring().antMatchers("/v2/api-docs")
			.antMatchers("/swagger-resources/**")
			.antMatchers("/swagger-ui.html")
			.antMatchers("/configuration/**")//
			.antMatchers("/webjars/**")//
			.antMatchers("/public")

			//Banco de dados H2 inseguro (para fins de teste)
			.and()
			.ignoring()
			.antMatchers("/h2-console/**/**");
	}

}
