package com.pitang.systemusercar.security;

import com.pitang.systemusercar.exception.CustomException;
import com.pitang.systemusercar.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Classe que sera executada na ida e depois na volta (Request & Response).
 * Com isso eh possivel fazer um verificacao de acesso
 * ideal tambem para fazer tratamento de erro ou medir o tempo de execucao.
 *
 */
public class JwtFilterAuth extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

		try {

			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;

			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, text/plain, */*");


			Authentication authentication = AuthService.getAuthentication(request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
		} catch (SignatureException e){
			new CustomException("Unauthorized - invalid session", 401).printWriter(res);
		} catch (ExpiredJwtException e) {
			new CustomException("Unauthorized - invalid session", 401).printWriter(res);
		} catch (NestedServletException e) {
			new CustomException("Not found", 404).printWriter(res);
		} catch (CustomException e){
			new CustomException(e.getMessage(), e.getErrorCode()).printWriter(res);
		}

	}
}