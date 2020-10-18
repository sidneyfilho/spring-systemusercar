package com.pitang.systemusercar.exception;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Classe resposavel de soltar excecoes de acordo com o que foi solicitado no desafio
 *
 * Login inexistente ou senha inválida: retornar um erro com a mensagem “Invalid login or password”;
 * E-mail já existente: retornar um erro com a mensagem “Email already exists”;
 * Login já existente: retornar um erro com a mensagem “Login already exists”;
 * Campos inválidos: retornar um erro com a mensagem “Invalid fields”;
 * Campos não preenchidos: retornar um erro com a mensagem “Missing fields”;
 * Token não enviado: retornar um erro com a mensagem “Unauthorized”;
 * Token expirado: retornar um erro com a mensagem “Unauthorized - invalid session”;
 * Placa já existente: retornar um erro com a mensagem “License plate already exists”;
 * Campos inválidos: retornar um erro com a mensagem “Invalid fields”;
 * Campos não preenchidos: retornar um erro com a mensagem “Missing fields”.
 *
 */

public class CustomException extends ServletException {

	private String message;
	private int errorCode;

    public CustomException(String message, int errorCode) {
        this.errorCode = errorCode;
        this.message = message;
    }

	public CustomException(CustomException e) {
        this.errorCode = e.getErrorCode();
        this.message = e.getMessage();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void printWriter(ServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print("{ \"message\": \"" + this.getMessage() + "\" , \"errorCode\": " + this.getErrorCode() + " }");
		((HttpServletResponse) response).setStatus(getErrorCode());
		out.flush();
	}

    @Override
    public String toString() {
        return this.errorCode + " : " + this.getMessage();
    }

}
