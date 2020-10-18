package com.pitang.systemusercar.service;

import com.pitang.systemusercar.exception.CustomException;
import com.pitang.systemusercar.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;

/**
 * Classe responsavel de gerar o token JWT e a autenticacao
 *
 */

@Service
public class AuthService {

	static final String HEADER_STRING = "Authorization";
	static final long EXPIRATION_TIME = 1500000;
	static final String KEY_SECRET = "DesafioPitang";

	@Autowired
	private UserService userService;

	public static Authentication getAuthentication(HttpServletRequest request) throws ExpiredJwtException, SignatureException {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			String login = Jwts.parser().setSigningKey(KEY_SECRET).parseClaimsJws(token).getBody().getSubject();

			if (login != null) {
				return new UsernamePasswordAuthenticationToken(login, null, Collections.emptyList());
			}
		}
		return null;
	}

	public String login(String login, String password) throws CustomException {
		userService.validAuth(login, password);
		String token = Jwts.builder().setIssuedAt(new Date(System.currentTimeMillis())).setSubject(login)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, KEY_SECRET).compact();
		return token;
	}

	public User getUser(String token) throws CustomException {
		String userLogin = Jwts.parser().setSigningKey(KEY_SECRET).parseClaimsJws(token).getBody().getSubject();
		User user = userService.findByLogin(userLogin);
		if (user == null) {
			throw new CustomException("Unauthorized - invalid session", 401);
		}
		return user;
	}

	public static Boolean matchedPassword(String password, String userPassword) {
		if (new BCryptPasswordEncoder().matches(password, userPassword)) {
			return true;
		}
		return false;
	}

}
